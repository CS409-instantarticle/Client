package com.example.rho_eojin1.a409_prototype13;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ContentActivity2 extends AppCompatActivity {
    ArrayList<ContentListElement> content_list;
    ListView content_list_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content2);

        Intent intent = getIntent();
        String articleID = intent.getExtras().getString("ArticleID");
        Log.e("GetExtras", articleID);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);

        DBHelperContent dbHelperContent = DBHelperContent.getInstance(getApplicationContext());
        SQLiteDatabase dbContent = dbHelperContent.getReadableDatabase();
        Cursor cursor = dbHelperContent.selectArticleID(dbContent, articleID);

        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() == false) {

                int index = cursor.getInt(cursor.getColumnIndex(dbHelperContent.ARTICLEINDEX));
                String type = cursor.getString(cursor.getColumnIndex(dbHelperContent.ARTICLETYPE));
                String content = cursor.getString(cursor.getColumnIndex(dbHelperContent.CONTENT));

                Log.e("Index", String.valueOf(index));
                Log.e("Type", type);
                Log.e("Content", content);
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
                    int height= imageView.getHeight();
                    int width = imageView.getWidth();

                    //Log.e("layout Width", String.valueOf(viewWidth));
                    /*
                    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    imageView.setLayoutParams(new ViewGroup.LayoutParams(
                            viewWidth,
                            (int) (height * (double) viewWidth / width))
                    );
                    */


                    imageView.setLayoutParams(
                            new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));

                    imageView.setScaleType(ImageView.ScaleType.FIT_START);
                    imageView.setAdjustViewBounds(true);


                    linearLayout.addView(imageView);
                    final String image_URL = content;
                    ThumbnailTask thumbnailTask = new ThumbnailTask(getApplicationContext(), imageView);
                    thumbnailTask.execute(image_URL);
                }

                else {
                    return;
                }
                cursor.moveToNext();
            }
        }
    }
}
