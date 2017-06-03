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

import java.net.URLEncoder;
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
    int last_max_requested = 0;
    int local_min_index = 0;
    int local_max_index = 0;
    int min_index = 0;
    int max_index = -1;
    boolean db_init = false;

    private String ToEng()
    {
        if(this.sectionName.equals("홈"))
            return "Home";
        else if(this.sectionName.equals("정치"))
            return "Politics";
        else if(this.sectionName.equals("경제"))
            return "Economy";
        else if(this.sectionName.equals("사회"))
            return "Society";
        else if(this.sectionName.equals("생활"))
            return "Life";
        else if(this.sectionName.equals("세계"))
            return "World";
        else if(this.sectionName.equals("IT"))
            return "IT";
        else
            return null;
    }

    public MainFragment(Context context, String sectionName) {
        this.context = context;
        this.sectionName = sectionName;
        main_list = new ArrayList<MainListElement>();
        min_index = 0;
        Log.e("index_2_created", String.valueOf(max_index));
    }

    public void getInitDB(){
        if(db_init == false){
            HttpClient newClient = new HttpClient(this.context, "http://kaist.tk:1234/ArticleSection/" + ToEng() + "/99999999", true, this);
            //newClient.execute();
            LimitHTTP.getInstance().addHttp(newClient);
            Log.e("cancel","Limit http called");
            //db_init = true;
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

        if(min_index < local_min_index) {
            cursor = dbHelperMain.selectIndex(dbMain, sectionName, min_index - 1, local_min_index);
            local_min_index = min_index - 1;

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
        }
        else if(max_index > local_max_index) {
            cursor = dbHelperMain.selectIndex(dbMain, sectionName, local_max_index + 1, max_index);
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                String article_id = cursor.getString(cursor.getColumnIndex(dbHelperMain.ARTICLEID));
                String title = cursor.getString(cursor.getColumnIndex(dbHelperMain.ARTICLETITLE));
                String thumbnail = cursor.getString(cursor.getColumnIndex(dbHelperMain.THUMBNAILIMAGEURL));
                String press = cursor.getString(cursor.getColumnIndex(dbHelperMain.PRESS));

                //Log.e("SectionArticleTitle", title);
                main_list.add(0, new MainListElement(article_id,title,thumbnail,press));
                cursor.moveToNext();
            }

            local_max_index = max_index;
        }
        else
            return;

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
            public void onScrollStateChanged(AbsListView absListView, int i) {
                /* Scroll to the top */
                if(absListView.getChildAt(0).getTop() == 0 && last_max_requested != max_index)
                {
                    last_max_requested = max_index;
                    Log.e("E","AKR");
                    HttpClient newClient = new HttpClient(context, "http://kaist.tk:1234/ArticleSection/" + ToEng() + "/" + String.valueOf(max_index + 12), false, MainFragment.this);
                    newClient.execute();
                }
            }
            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                Log.e("NUM1",String.valueOf(i));
                Log.e("NUM1",String.valueOf(i2));
                Log.e("NUM1",String.valueOf(i1));
                Log.e("NUM1",String.valueOf(last_requested));
                Log.e("NUM1",String.valueOf(min_index));
                Log.e("NUM1",String.valueOf(local_min_index));
                Log.e("NUM1",String.valueOf(max_index));
                if(i == i2 - i1 && last_requested != min_index && i2 != 0) {
                    //Log.e("NUM2",String.valueOf(i));
                    //Log.e("NUM2",String.valueOf(i2));
                    //Log.e("NUM2",String.valueOf(i1));
                    //Log.e("NUM2",String.valueOf(last_requested));
                    //Log.e("NUM2",String.valueOf(min_index));

                    last_requested = min_index;
                    //Log.e("Requested : ", "http://imgeffect.kaist.ac.kr:1234/ArticleList/" + String.valueOf(min_index - 30));
                    HttpClient newClient = new HttpClient(context, "http://kaist.tk:1234/ArticleSection/" + ToEng() + "/" + String.valueOf(min_index - 1), false, MainFragment.this);
                    newClient.execute();
                }
            }
        });


        return rootView;
    }
}
