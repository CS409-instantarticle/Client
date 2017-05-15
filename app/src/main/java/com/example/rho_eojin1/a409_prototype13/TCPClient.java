package com.example.rho_eojin1.a409_prototype13;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 * Created by root on 2017-05-15.
 */

public class TCPClient extends AsyncTask<Void,Void,Void>{

    public int swap_endian (int value)
    {
        int b1 = (value >>  0) & 0xff;
        int b2 = (value >>  8) & 0xff;
        int b3 = (value >> 16) & 0xff;
        int b4 = (value >> 24) & 0xff;

        return b1 << 24 | b2 << 16 | b3 << 8 | b4 << 0;
    }

    Context context;
    String Host;
    int port;
    String result;

    Socket socket = null;
    InputStream inputStream = null;
    OutputStream outputStream = null;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    TCPClient(Context context, String Host, int port) {
        this.context = context;
        this.Host = Host;
        this.port = port;
    }

    protected Void doInBackground(Void... voids) {
        try{
            /* Create new socket */
            try {
                this.socket = new Socket(this.Host, this.port);
                this.inputStream = socket.getInputStream();
                this.outputStream = socket.getOutputStream();
            }
            catch(java.io.IOException e)
            {
            /* Do nothing */
                Log.e("Socket", "fail");
            }

            /* Protocol header declaration */
            byte[] header = new byte[8];

            /* Type 1 query for debugging */
            header[0] = 1;

            /* Set checksum value */
            header[1] = (byte)0xef;
            header[2] = (byte)0xcd;
            header[3] = (byte)0xab;

            /* Set query length (26 for type 0 query) */
            int length = swap_endian(5);
            byte[] lengthToByte = java.nio.ByteBuffer.allocate(4).putInt(length).array();

            /* Length has converted to the little endian */
            System.arraycopy(lengthToByte, 0, header, 4, 4);

            /* Send header to the server first */
            outputStream.write(header, 0, 8);

            byte[] direction = new byte[1];
            direction[0] = (byte)0;

            int index = swap_endian(0);
            byte[] ldxToByte = java.nio.ByteBuffer.allocate(4).putInt(index).array();

            /* Send a direction and index */
            outputStream.write(direction, 0, 1);
            outputStream.write(ldxToByte, 0, 4);

            /* Get server response (read header first) */
            int readByte = inputStream.read(header, 0, 8);
            System.arraycopy(header,4,lengthToByte,0,4);
            length = swap_endian(ByteBuffer.wrap(lengthToByte).getInt()) - 4;

            byte new_index[] = new byte[4];
            inputStream.read(new_index, 0, 4);

            /* Get server response (read payload) */
            byte[] json_buffer = new byte[length];
            int readOffset = 0;

            while(readOffset < length)
            {
                int read_amount = inputStream.read(json_buffer, readOffset, length - readOffset);
                /* Break if read amount <= -1, but not yet implemented */
                readOffset += read_amount;
            }

            result = new String(json_buffer);

        }catch(MalformedURLException | ProtocolException exception) {
            exception.printStackTrace();
        }catch(IOException io){
            io.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Log.e("TcpClient", result);


        String tmpJSONstr = String.valueOf(result);

        JSONArray tmpJSONArray;

        DBHelperMain dbHelper = DBHelperMain.getInstance(context);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        dbHelper.onUpgrade(db, 1, 1);

        DBHelperContent dbHelperContent = DBHelperContent.getInstance(context);

        SQLiteDatabase dbContent = dbHelperContent.getWritableDatabase();

        dbHelperContent.onUpgrade(dbContent, 1, 1);

        try {
            tmpJSONArray = new JSONArray("[" + tmpJSONstr + "]");
            Log.e("Jsontest",tmpJSONArray.getJSONObject(0).getString("ArticleTitle"));

            long rowID;
            if (tmpJSONArray != null) {
                int size = tmpJSONArray.length();
                JSONObject oneArticle;
                for (int i = 0; i < size; i++) {
                    oneArticle = tmpJSONArray.getJSONObject(i);
                    if (oneArticle != null) {
                        ContentValues values = new ContentValues();
                        values.put(dbHelper.ARTICLEID, oneArticle.getString(dbHelper.ARTICLEID));
                        values.put(dbHelper.ARTICLETITLE, oneArticle.getString(dbHelper.ARTICLETITLE));
                        values.put(dbHelper.PRESS, oneArticle.getString(dbHelper.PRESS));
                        values.put(dbHelper.THUMBNAILIMAGEURL, oneArticle.getString(dbHelper.THUMBNAILIMAGEURL));
                        values.put(dbHelper.LINK, oneArticle.getString(dbHelper.LINK));
                        values.put(dbHelper.SECTIONNAME, oneArticle.getString(dbHelper.SECTIONNAME));
                        rowID = dbHelper.insertAll(db, values);
                        Log.e("sqlite_test", String.valueOf(rowID));

                        JSONArray contentsArray = oneArticle.getJSONArray(dbHelper.CONTENTS);
                        Log.e("Jsontest2",contentsArray.getJSONObject(0).getString("ArticleType"));
                        Log.e("Jsontest2",contentsArray.toString());


                        if (contentsArray != null){
                            int contents_size = contentsArray.length();
                            JSONObject oneContent;
                            for (int j = 0; j < contents_size; j++) {
                                oneContent = contentsArray.getJSONObject(j);
                                Log.e("sqlite_test2", oneContent.toString());

                                if (oneContent != null) {
                                    ContentValues contentValues = new ContentValues();

                                    contentValues.put(dbHelperContent.ARTICLEID, oneArticle.getString(dbHelperContent.ARTICLEID));
                                    contentValues.put(dbHelperContent.ARTICLETYPE, oneContent.getString(dbHelperContent.ARTICLETYPE));
                                    if (oneContent.getString(dbHelperContent.ARTICLETYPE).equals("image")){
                                        CacheMediaTask cacheTask = new CacheMediaTask(context);
                                        cacheTask.execute(oneContent.getString(dbHelperContent.CONTENT));
                                    }
                                    contentValues.put(dbHelperContent.ARTICLEINDEX, oneContent.getString(dbHelperContent.ARTICLEINDEX));
                                    contentValues.put(dbHelperContent.CONTENT, oneContent.getString(dbHelperContent.CONTENT));
                                    rowID= dbHelperContent.insertAll(dbContent, contentValues);
                                }
                            }
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        super.onPostExecute(aVoid);
    }
}
