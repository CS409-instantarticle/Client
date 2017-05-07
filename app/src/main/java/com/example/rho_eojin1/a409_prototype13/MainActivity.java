package com.example.rho_eojin1.a409_prototype13;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MainActivity extends AppCompatActivity {
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        PlaceholderFragment placeholderFragment = new PlaceholderFragment();


        HttpClient newClient = new HttpClient(getApplicationContext(), "http://192.249.31.112:3000/posts");
        newClient.execute();

        List<String> sec_names = new ArrayList<>();
        sec_names.add("홈");
        sec_names.add("정치");
        sec_names.add("경제");
        sec_names.add("사회");
        sec_names.add("IT");
        sec_names.add("생활");
        sec_names.add("세계");

        for(int i=0; i < sec_names.size(); ++i) {
            Log.e("Init_section", sec_names.get(i));
            mSectionsPagerAdapter.addFragment(placeholderFragment.newInstance(sec_names.get(i)), sec_names.get(i));
        }

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        ListView main_list_view;
        MainListAdapter main_list_adapter;
        ArrayList<MainListElement> main_list;

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        static String ARG_SECTION_NAME = "section_name";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(String sectionName) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putString(ARG_SECTION_NAME, sectionName);
            fragment.setArguments(args);
            fragment.ARG_SECTION_NAME = sectionName;
            return fragment;
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


            main_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    MainListElement clickedElement = (MainListElement) adapterView.getAdapter().getItem(position);
                    String ID = clickedElement.getArticleID();
                    Intent intent = new Intent(rootView.getContext(), ContentActivity.class);
                    Log.e("intent_called","sd");
                    intent.putExtra("ArticleID",ID);

                    long tStart = System.currentTimeMillis();
                    intent.putExtra("tStart", tStart);
                    startActivity(intent);
                }
            });

            DBHelperMain dbHelperMain = DBHelperMain.getInstance(rootView.getContext());

            SQLiteDatabase dbMain = dbHelperMain.getReadableDatabase();

            Cursor cursor = dbHelperMain.selectListElement(dbMain);

            if (cursor.moveToFirst()) {
                while (cursor.isAfterLast() == false) {
                    String article_id = cursor.getString(cursor.getColumnIndex(dbHelperMain.ARTICLEID));
                    String title = cursor.getString(cursor.getColumnIndex(dbHelperMain.ARTICLETITLE));
                    String thumbnail = cursor.getString(cursor.getColumnIndex(dbHelperMain.THUMBNAILIMAGEURL));
                    String press = cursor.getString(cursor.getColumnIndex(dbHelperMain.PRESS));

                    main_list.add(new MainListElement(article_id,title,thumbnail,press));
                    cursor.moveToNext();
                }
            }
            cursor.close();

            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        final List<Fragment> mFragmentList = new ArrayList<>();
        final List<String> mFragmentTitleList = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        public List<Fragment> getFragmentList(){
            return mFragmentList;
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
