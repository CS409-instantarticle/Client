package com.example.rho_eojin1.a409_prototype13;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

public class ContentActivity2 extends AppCompatActivity {
    ArrayList<ContentListElement> content_list;
    ListView content_list_view;
    ContentListAdapter content_list_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content2);

        Intent intent = getIntent();
        String articleID = intent.getExtras().getString("ArticleID");
        Log.e("GetExtras", articleID);

        content_list = new ArrayList<ContentListElement>();


        content_list_view = (ListView) findViewById(R.id.listView3);
        content_list_adapter = new ContentListAdapter(getApplicationContext(), R.layout.content_list_element, content_list);
        content_list_adapter.notifyDataSetInvalidated();
        content_list_view.setAdapter(content_list_adapter);

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
                Log.e("Why_Many", String.valueOf(cursor.getPosition()));

                content_list.add(new ContentListElement(index, type, content));

                cursor.moveToNext();
            }
        }
    }
}
