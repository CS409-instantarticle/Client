package com.example.rho_eojin1.a409_prototype13;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

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

public class ThumbnailTask extends AsyncTask<String, Void, Void> {
    private Context context;
    private ImageView imageView;
    File file;

    public ThumbnailTask(Context context, ImageView imageView){
        this.context = context;
        this.imageView = imageView;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        imageView.setImageURI(Uri.fromFile(file));
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    protected Void doInBackground(String... urls) {
        String image_URL = urls[0];
        String fileName = Uri.parse(image_URL).getLastPathSegment();
        file = new File(this.context.getCacheDir(), fileName);
        Log.e("fileName", file.getAbsolutePath());
        if (file.exists()){
            Log.e("File_Exists","True");
            return null;
        }else {
            Log.e("File_Exists","False");
            try {
                URL url = new URL(image_URL);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestMethod("GET");
                urlConnection.setDoOutput(true);

                urlConnection.connect();

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
}
