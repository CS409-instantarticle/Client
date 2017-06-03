package com.example.rho_eojin1.a409_prototype13;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.ArrayList;

public class ContentActivity2 extends AppCompatActivity {
    String articleID;
    String sectionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content2);

        Intent intent = getIntent();
        articleID = intent.getExtras().getString("ArticleID");

        sectionName = intent.getExtras().getString("SectionName");
        long tStart = intent.getExtras().getLong("tStart");
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);

        DBHelperContent dbHelperContent = DBHelperContent.getInstance(getApplicationContext());
        SQLiteDatabase dbContent = dbHelperContent.getReadableDatabase();

        DisplayMetrics display = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(display);
        int width = display.widthPixels;
        int height = (3*width)/4;


        //Log.e("ContentPage", articleID);
        //Log.e("ContentPage", sectionName);
        Cursor cursor = dbHelperContent.selectArticleID(dbContent, sectionName, articleID);

        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() == false) {

                int index = cursor.getInt(cursor.getColumnIndex(dbHelperContent.ARTICLEINDEX));
                String type = cursor.getString(cursor.getColumnIndex(dbHelperContent.ARTICLETYPE));
                String content = cursor.getString(cursor.getColumnIndex(dbHelperContent.CONTENT));

                //Log.e("Index", String.valueOf(index));
                //Log.e("Type", type);
                //Log.e("ContentPageContent", content);
                //Log.e("Why_Many", String.valueOf(cursor.getPosition()));

                if (type.equals("text")) {
                    TextView textView = new TextView(getApplicationContext());
                    linearLayout.addView(textView);
                    textView.setText(content);
                }

                else if (type.equals("strapline")) {
                    TextView textView = new TextView(getApplicationContext());
                    textView.setPaintFlags(textView.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
                    textView.setTextSize(16);
                    textView.setTextColor(Color.BLACK);
                    linearLayout.addView(textView);
                    textView.setText(content);
                }

                else if (type.equals("url")) {
                    TextView textView = new TextView(getApplicationContext());
                    textView.setTextColor(Color.BLUE);
                    linearLayout.addView(textView);
                    textView.setText(content);
                }

                else if (type.equals("image")) {
                    ImageView imageView = new ImageView(getApplicationContext());

                    /*imageView.setLayoutParams(
                            new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT));*/
                    imageView.setLayoutParams(new LinearLayout.LayoutParams(width,height));
                    imageView.setAdjustViewBounds(true);
                    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

                    linearLayout.addView(imageView);
                    final String image_URL = content;
                    ThumbnailTask thumbnailTask = new ThumbnailTask(getApplicationContext(), imageView);
                    thumbnailTask.execute(image_URL);
                }else if (type.equals("video")){
                    Uri uri = Uri.parse(content);
                    VideoView videoView = new VideoView(getApplicationContext());
                    videoView.setLayoutParams(new LinearLayout.LayoutParams(width,height));
                    linearLayout.addView(videoView);
                    MediaController mediaController = new MediaController(this);
                    mediaController.setAnchorView(videoView);
                    videoView.setMediaController(mediaController);
                    videoView.setVideoURI(uri);
                }

                else {
                    break;
                    //Log.e("Who the fuck put link in the article", articleID);
                    /*
                    long tEnd = System.currentTimeMillis();
                    Log.e("Elapsed Time", String.valueOf(tEnd - tStart) + " ms");

                    String path = "http://naver-mbc-h.smartmediarep.com/smc/naver/multi/eng/M12_T9201705300024/2f6d62632f6d6263706c75732f617263686976652f73706f7274732f323031372f30352f33302f47413230313730353330303030352f43413230313730353330303032332f43413230313730353330303032335f7433332e6d7034/0-0-0/content.mp4?solexpire=1496160923&soltoken=f52a9f575360730e1fcd36905e4f5cf4&soltokenrule=c29sZXhwaXJlfHNvbHBhdGhsZW58c29sdXVpZA==&soluriver=2&soluuid=a5aafafb-47b7-4a78-8ba9-62d2f605e55a&itemtypeid=33&tid=rmcPlayer_14961177368823009";
                    Uri uri = Uri.parse(path);
                    VideoView videoView = new VideoView(getApplicationContext());
                    videoView.setLayoutParams(new LinearLayout.LayoutParams(width,height));
                    linearLayout.addView(videoView);
                    MediaController mediaController = new MediaController(this);
                    mediaController.setAnchorView(videoView);
                    videoView.setMediaController(mediaController);
                    videoView.setVideoURI(uri);
                    */
                }
                cursor.moveToNext();

            }
        }



    }
}
