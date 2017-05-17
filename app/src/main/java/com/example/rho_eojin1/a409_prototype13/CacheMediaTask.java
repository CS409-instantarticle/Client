package com.example.rho_eojin1.a409_prototype13;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Rho-Eojin1 on 2017. 5. 8..
 */

public class CacheMediaTask extends AsyncTask<String, Void, Void> {
    private Context context;

    public CacheMediaTask(Context context){
        this.context = context;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    protected Void doInBackground(String... urls) {
        try {
            String image_URL = urls[0];
            URL url = new URL(image_URL);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);

            urlConnection.connect();

            String fileName = Uri.parse(image_URL).getLastPathSegment();
            if(fileName.equals("None")){
                return null;
            }

            File file = new File(this.context.getCacheDir(), fileName);
            Log.e("fileName", file.getAbsolutePath());

            //File file = new File(getApplicationContext().getFilesDir(), fileName);

            FileOutputStream fileOutput = new FileOutputStream(file);

            //read data from connection
            InputStream inputStream = urlConnection.getInputStream();

            int totalSize = urlConnection.getContentLength();
            //in bytes
            int downloadedSize = 0;

            byte[] buffer = new byte[1024];
            int bufferLength = 0;


            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fileOutput.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
            }
            fileOutput.close();

            Log.e("complete", "done");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
