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
    int local_min_index = 0;
    static int min_index = 0;
    static int max_index = -1;
    private boolean db_init = false;

    public MainFragment(Context context, String sectionName) {
        this.context = context;
        this.sectionName = sectionName;
        main_list = new ArrayList<MainListElement>();
        min_index = 0;
        Log.e("index_2_created", String.valueOf(max_index));
    }

    public void getInitDB(){
        if(db_init == false){
            HttpClient newClient = new HttpClient(this.context, "http://kaist.tk:1234/ArticleSection/" + sectionName + "/99999999", true, this);
            newClient.execute();
            db_init = true;
        }else {
            return;
        }
    }

    public void UpdateList()
    {
        if(main_list_adapter == null)
            return;
        Log.e("index_2_", String.valueOf(local_min_index) + "," + String.valueOf(max_index));

        DBHelperMain dbHelperMain = DBHelperMain.getInstance(this.context);
        SQLiteDatabase dbMain = dbHelperMain.getReadableDatabase();
        Cursor cursor;

        Log.e("Why section name wrong?", sectionName);
        cursor = dbHelperMain.selectIndex(dbMain, sectionName, min_index - 1, local_min_index);

        /*if (sectionName.equals("홈")) {
            cursor = dbHelperMain.selectIndex(dbMain, min_index - 1, local_min_index);
        } else {
            cursor = dbHelperMain.selectSectionIndex(dbMain, sectionName, min_index - 1, local_min_index);
        }*/

        if(min_index < local_min_index)
            local_min_index = min_index - 1;
        else
            return;

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            String article_id = cursor.getString(cursor.getColumnIndex(dbHelperMain.ARTICLEID));
            String title = cursor.getString(cursor.getColumnIndex(dbHelperMain.ARTICLETITLE));
            String thumbnail = cursor.getString(cursor.getColumnIndex(dbHelperMain.THUMBNAILIMAGEURL));
            String press = cursor.getString(cursor.getColumnIndex(dbHelperMain.PRESS));

            //Log.e("SectionArticleTitle", title);
            main_list.add(new MainListElement(article_id,title,thumbnail,press));
            cursor.moveToNext();
        }

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
                intent.putExtra("ArticleID",ID);
                intent.putExtra("SectionName",sectionName);
                long tStart = System.currentTimeMillis();
                intent.putExtra("tStart",tStart);
                startActivity(intent);
            }
        });

        main_list_view.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {}
            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                //Log.e("NUM1",String.valueOf(i));
                //Log.e("NUM1",String.valueOf(i2));
                //Log.e("NUM1",String.valueOf(i1));
                //Log.e("NUM1",String.valueOf(last_requested));
                //Log.e("NUM1",String.valueOf(min_index));
                if(i == i2 - i1 && last_requested != min_index && i2 != 0) {
                    //Log.e("NUM2",String.valueOf(i));
                    //Log.e("NUM2",String.valueOf(i2));
                    //Log.e("NUM2",String.valueOf(i1));
                    //Log.e("NUM2",String.valueOf(last_requested));
                    //Log.e("NUM2",String.valueOf(min_index));

                    last_requested = min_index;
                    //Log.e("Requested : ", "http://imgeffect.kaist.ac.kr:1234/ArticleList/" + String.valueOf(min_index - 30));
                    HttpClient newClient = new HttpClient(context, "http://kaist.tk:1234/ArticleSection/" + sectionName + "/" + String.valueOf(min_index - 30), false, MainFragment.this);
                    newClient.execute();
                }
            }
        });


        return rootView;
    }
}
