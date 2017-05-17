package com.example.rho_eojin1.a409_prototype13;

/**
 * Created by Rho-Eojin1 on 2017. 5. 8..
 */

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment {
    ListView main_list_view;
    MainListAdapter main_list_adapter;
    ArrayList<MainListElement> main_list;
    String sectionName;
    Context context;

    public MainFragment(Context context, String sectionName) {
        this.context = context;
        this.sectionName = sectionName;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        main_list = new ArrayList<MainListElement>();

        main_list_view = (ListView) rootView.findViewById(R.id.listView);
        main_list_adapter = new MainListAdapter(rootView.getContext(), R.layout.main_list_element, main_list);
        main_list_adapter.notifyDataSetInvalidated();
        main_list_view.setAdapter(main_list_adapter);

        Cursor cursor;

        DBHelperMain dbHelperMain = DBHelperMain.getInstance(this.context);

        SQLiteDatabase dbMain = dbHelperMain.getReadableDatabase();

        if (sectionName.equals("홈")) {
            cursor = dbHelperMain.selectAll(dbMain);
        }else {
            cursor = dbHelperMain.selectSection(dbMain, sectionName);
        }

        int i = 0;
        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                Log.e("Json Num count", String.valueOf(i));
                ++i;
                String article_id = cursor.getString(cursor.getColumnIndex(dbHelperMain.ARTICLEID));
                String title = cursor.getString(cursor.getColumnIndex(dbHelperMain.ARTICLETITLE));
                String thumbnail = cursor.getString(cursor.getColumnIndex(dbHelperMain.THUMBNAILIMAGEURL));
                String press = cursor.getString(cursor.getColumnIndex(dbHelperMain.PRESS));

                main_list.add(new MainListElement(article_id,title,thumbnail,press));
                cursor.moveToNext();
            }
        }
        cursor.close();

        main_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                MainListElement clickedElement = (MainListElement) adapterView.getAdapter().getItem(position);
                String ID = clickedElement.getArticleID();
                Intent intent = new Intent(rootView.getContext(), ContentActivity2.class);
                Log.e("intent_called","sd");
                intent.putExtra("ArticleID",ID);

                //long tStart = System.currentTimeMillis();
                //intent.putExtra("tStart", tStart);
                startActivity(intent);
            }
        });

        return rootView;
    }
}
