package com.example.rho_eojin1.a409_prototype13;

/**
 * Created by Rho-Eojin1 on 2017. 5. 8..
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class ContentFragment extends Fragment {
    ListView content_list_view;
    ContentListAdapter content_list_adapter;
    String articleID;
    ArrayList<ContentListElement> content_list;
    Context context;

    public ContentFragment(Context context, String articleID) {
        this.context = context;
        this.articleID = articleID;
    }

    private boolean checkDupIndex(int index){
        for(int i = 0; i < content_list.size(); ++i){
            if (content_list.get(i).getIndex() == index){
                return false;
            }
        }
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_content, container, false);

        content_list = new ArrayList<ContentListElement>();

        content_list_view = (ListView) rootView.findViewById(R.id.listView2);
        content_list_view.setDivider(null);
        content_list_adapter = new ContentListAdapter(rootView.getContext(), R.layout.content_list_element, content_list);
        content_list_adapter.notifyDataSetInvalidated();
        content_list_view.setAdapter(content_list_adapter);

        DBHelperContent dbHelperContent = DBHelperContent.getInstance(rootView.getContext());

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

                if (this.checkDupIndex(index)) {
                    content_list.add(new ContentListElement(index, type, content));
                }

                cursor.moveToNext();
            }
        }

        return rootView;
    }
}