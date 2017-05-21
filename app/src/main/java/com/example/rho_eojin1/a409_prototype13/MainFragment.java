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
import android.widget.AbsListView;
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
    int last_requested = 0;
    int min_index = 0;
    static int max_index = -1;

    public MainFragment(Context context, String sectionName) {
        this.context = context;
        this.sectionName = sectionName;
        main_list = new ArrayList<MainListElement>();
    }

    public void UpdateList()
    {
        DBHelperMain dbHelperMain = DBHelperMain.getInstance(this.context);
        SQLiteDatabase dbMain = dbHelperMain.getReadableDatabase();
        Cursor cursor;

        if (sectionName.equals("홈")) {
            cursor = dbHelperMain.selectIndex(dbMain, min_index, max_index);
        } else {
            cursor = dbHelperMain.selectSectionIndex(dbMain, sectionName, min_index, max_index);
        }

        if(min_index < max_index)
            min_index = max_index + 1;
        else
            return;

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            String article_id = cursor.getString(cursor.getColumnIndex(dbHelperMain.ARTICLEID));
            String title = cursor.getString(cursor.getColumnIndex(dbHelperMain.ARTICLETITLE));
            String thumbnail = cursor.getString(cursor.getColumnIndex(dbHelperMain.THUMBNAILIMAGEURL));
            String press = cursor.getString(cursor.getColumnIndex(dbHelperMain.PRESS));

            main_list.add(new MainListElement(article_id,title,thumbnail,press));
            cursor.moveToNext();
        }

        if(main_list_adapter != null)
            main_list_adapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        UpdateList();
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        main_list_view = (ListView) rootView.findViewById(R.id.listView);
        main_list_adapter = new MainListAdapter(rootView.getContext(), R.layout.main_list_element, main_list);
        main_list_view.setAdapter(main_list_adapter);

        main_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                MainListElement clickedElement = (MainListElement) adapterView.getAdapter().getItem(position);
                String ID = clickedElement.getArticleID();
                Intent intent = new Intent(rootView.getContext(), ContentActivity2.class);
                Log.e("intent_called","sd");
                intent.putExtra("ArticleID",ID);
                startActivity(intent);
            }
        });

        main_list_view.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {}
            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                Log.e("SKT",String.valueOf(i) + "," + String.valueOf(i1) + "," + String.valueOf(i2));
                if(i == i2 - i1 && last_requested < i2 / 30 && i2 % 30 == 0) {
                    HttpClient newClient = new HttpClient(context, "http://imgeffect.kaist.ac.kr:1234/ArticleList/" + String.valueOf((last_requested + 1) * 30), false, MainFragment.this);
                    newClient.execute();
                    last_requested += 1;
                    Log.e("AKT",String.valueOf(i) + "," + String.valueOf(i1) + "," + String.valueOf(i2) + "," + String.valueOf(max_index));
                }
            }
        });

        return rootView;
    }
}
