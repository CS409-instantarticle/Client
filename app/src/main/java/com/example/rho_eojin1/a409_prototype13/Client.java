package com.example.rho_eojin1.a409_prototype13;

/**
 * Created by Rho-Eojin1 on 2017. 5. 8..
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends AsyncTask<Void, Void, Void> {
    Context context;
    String dstAddress;
    int dstPort;
    String response = "";
    //TextView textResponse;

    Client(Context context, String addr, int port) {
        this.context = context;
        dstAddress = addr;
        dstPort = port;
        //this.textResponse = textResponse;
    }

    @Override
    protected Void doInBackground(Void... arg0) {

        Socket socket = null;

        try {
            socket = new Socket(dstAddress, dstPort);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
                    4096);
            byte[] buffer = new byte[4096];

            int bytesRead;
            InputStream inputStream = socket.getInputStream();

			/*
             * notice: inputStream.read() will block if no data return
			 */
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
                //Log.e("SocketResponse", byteArrayOutputStream.toString("UTF-8"));
                response += byteArrayOutputStream.toString("UTF-8");
            }
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //response = "UnknownHostException: " + e.toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //response = "IOException: " + e.toString();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        Log.e("SocketResponsePost", response);

        String tmpJSONstr = String.valueOf(response);

        JSONArray tmpJSONArray;

        DBHelperMain dbHelper = DBHelperMain.getInstance(context);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        dbHelper.onUpgrade(db, 1, 1);

        DBHelperContent dbHelperContent = DBHelperContent.getInstance(context);

        SQLiteDatabase dbContent = dbHelperContent.getWritableDatabase();

        dbHelperContent.onUpgrade(dbContent, 1, 1);

        try {
            tmpJSONArray = new JSONArray(tmpJSONstr);
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

                        /*
                        // must be erased
                        rowID = dbHelper.insertAll(db, values);
                        Log.e("sqlite_test", String.valueOf(rowID));

                        rowID = dbHelper.insertAll(db, values);
                        Log.e("sqlite_test", String.valueOf(rowID));
                        // must be erased
                        */

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
                                    contentValues.put(dbHelperContent.ARTICLEINDEX, oneContent.getString(dbHelperContent.ARTICLEINDEX));
                                    contentValues.put(dbHelperContent.CONTENT, oneContent.getString(dbHelperContent.CONTENT));
                                    rowID= dbHelperContent.insertAll(dbContent, values);
                                }
                            }
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        super.onPostExecute(result);
    }
}
