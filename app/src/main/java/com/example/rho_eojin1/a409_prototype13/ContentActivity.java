package com.example.rho_eojin1.a409_prototype13;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ContentActivity extends AppCompatActivity {
    String articleID;

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
        setContentView(R.layout.activity_content);

        Intent intent = getIntent();
        articleID = intent.getExtras().getString("ArticleID");
        Log.e("GetExtras", articleID);
        long tStart = intent.getExtras().getLong("tStart");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        PlaceholderFragment placeholderFragment = new PlaceholderFragment(articleID);

        mSectionsPagerAdapter.addFragment(placeholderFragment, articleID);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        long tEnd = System.currentTimeMillis();
        Log.e("tEnd", String.valueOf(tEnd));
        long tDelta = tEnd - tStart;
        Log.e("elapsedtime", String.valueOf(tDelta) + "msec");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_content, menu);
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
        ListView content_list_view;
        ContentListAdapter content_list_adapter;
        String articleID;
        ArrayList<ContentListElement> content_list;
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment(String articleID) {
            this.articleID = articleID;
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(String articleID) {
            PlaceholderFragment fragment = new PlaceholderFragment(articleID);
            Bundle args = new Bundle();
            args.putString(ARG_SECTION_NUMBER, articleID);
            fragment.setArguments(args);
            fragment.articleID = articleID;
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_content, container, false);

            content_list = new ArrayList<ContentListElement>();

            content_list_view = (ListView) rootView.findViewById(R.id.listView2);
            content_list_adapter = new ContentListAdapter(rootView.getContext(), R.layout.content_list_element, content_list);
            content_list_adapter.notifyDataSetInvalidated();
            content_list_view.setAdapter(content_list_adapter);

            DBHelperContent dbHelperContent = DBHelperContent.getInstance(rootView.getContext());

            SQLiteDatabase dbContent = dbHelperContent.getReadableDatabase();

            Log.e("print", "get here?");
            //Log.e("print", DBHelperContent.getInstance(this).tmp);

            Cursor cursor = dbHelperContent.selectArticleID(dbContent, articleID);

            if (cursor.moveToFirst()) {
                while (cursor.isAfterLast() == false) {
                    String type = cursor.getString(cursor.getColumnIndex(dbHelperContent.ARTICLETYPE));
                    String content = cursor.getString(cursor.getColumnIndex(dbHelperContent.CONTENT));
                    Log.e("Type", type);
                    Log.e("Content", content);
                    Log.e("Why_Many", String.valueOf(cursor.getPosition()));

                    content_list.add(new ContentListElement(type, content));

                    cursor.moveToNext();
                }
            }

            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        final List<Fragment> mFragmentList = new ArrayList<>();
        final List<String> mFragmentIDList = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        public void addFragment(Fragment fragment, String ArticleID) {
            mFragmentList.add(fragment);
            mFragmentIDList.add(ArticleID);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentIDList.get(position);
        }
    }
}
