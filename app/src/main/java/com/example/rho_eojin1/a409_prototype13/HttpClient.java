package com.example.rho_eojin1.a409_prototype13;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rho-Eojin1 on 2017. 5. 8..
 */

public class HttpClient extends AsyncTask<Void,Void,Void>{
    //ArrayList<MainListElement> main_list;
    Context context;
    String strUrl;
    String result;
    boolean initial;
    MainFragment main_fragment;

    HttpClient(Context context, String strUrl, boolean initial, MainFragment main_fragment) {
        this.context = context;
        this.strUrl = strUrl; //탐색하고 싶은 URL이다.
        this.initial = initial;
        this.main_fragment = main_fragment;
        Log.e("called","Now");
        //this.main_list = main_list;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    protected Void doInBackground(Void... voids) {
        try{
            URL Url = new URL(strUrl); // URL화 한다.
            HttpURLConnection conn = (HttpURLConnection) Url.openConnection(); // URL을 연결한 객체 생성.
            conn.setRequestMethod("GET"); // get방식 통신
            conn.setDoOutput(false); // 쓰기모드 지정
            conn.setDoInput(true); // 읽기모드 지정
            conn.setUseCaches(false); // 캐싱데이터를 받을지 안받을지
            conn.setDefaultUseCaches(false); // 캐싱데이터 디폴트 값 설정

            //strCookie = conn.getHeaderField("Set-Cookie"); //쿠키데이터 보관

            InputStream is = conn.getInputStream(); //input스트림 개방

            StringBuilder builder = new StringBuilder(); //문자열을 담기 위한 객체
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8")); //문자열 셋 세팅
            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line+ "\n");
            }

            result = builder.toString();
            //Log.e("Builder", builder.toString());


        }catch(MalformedURLException | ProtocolException exception) {
            exception.printStackTrace();
        }catch(IOException io){
            io.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Log.e("HttpClient", result);

        String tmpJSONstr = String.valueOf(result);

        JSONArray tmpJSONArray;

        DBHelperMain dbHelper = DBHelperMain.getInstance(context);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        if(this.initial)
            dbHelper.onUpgrade(db, 1, 1);

        DBHelperContent dbHelperContent = DBHelperContent.getInstance(context);

        SQLiteDatabase dbContent = dbHelperContent.getWritableDatabase();

        if(this.initial)
            dbHelperContent.onUpgrade(dbContent, 1, 1);

        try {
            tmpJSONArray = new JSONArray(tmpJSONstr);
            Log.e("Json num",String.valueOf(tmpJSONArray.length()));
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
                        values.put(dbHelper.ARTICLE_MAIN_INDEX, Integer.parseInt(oneArticle.getString(dbHelper.ARTICLE_MAIN_INDEX)));
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

        main_fragment.max_index += 30;
        main_fragment.UpdateList();

        super.onPostExecute(aVoid);
    }
}
