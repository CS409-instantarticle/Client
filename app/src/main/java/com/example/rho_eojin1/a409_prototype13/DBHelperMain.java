package com.example.rho_eojin1.a409_prototype13;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rho-Eojin1 on 2017. 5. 8..
 */

public class DBHelperMain extends SQLiteOpenHelper {
    private static DBHelperMain sInstance;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "News.db";
    Map<String, String> TABLE_NAME_MAP = new HashMap<String, String>();
    ArrayList<String> TABLE_NAMES;
    public static final String ARTICLEID = "ArticleID";
    public static final String ARTICLETITLE = "ArticleTitle";
    public static final String ARTICLE_MAIN_INDEX = "ArticleMainIndex";
    public static final String PRESS = "Press";
    public static final String THUMBNAILIMAGEURL = "ThumbnailImageURL";
    public static final String LINK = "Link";
    public static final String SECTIONNAME = "SectionName";
    public static final String CONTENTS = "Contents";

    public static synchronized DBHelperMain getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DBHelperMain(context.getApplicationContext());
            Log.e("DBHelperInit","DBHelperInit");
        }
        return sInstance;
    }

    public DBHelperMain(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        TABLE_NAME_MAP.put("홈","main_table_home");
        TABLE_NAME_MAP.put("정치","main_table_politics");
        TABLE_NAME_MAP.put("경제","main_table_economy");
        TABLE_NAME_MAP.put("사회","main_table_society");
        TABLE_NAME_MAP.put("IT","main_table_it");
        TABLE_NAME_MAP.put("생활","main_table_living");
        TABLE_NAME_MAP.put("세계","main_table_world");
        TABLE_NAMES = new ArrayList<String>(TABLE_NAME_MAP.values());
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        for (int i=0; i<TABLE_NAMES.size(); ++i){
            sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAMES.get(i) + " (" +
                    ARTICLE_MAIN_INDEX + " INTEGER PRIMARY KEY," +
                    ARTICLEID + " TEXT," +
                    ARTICLETITLE + " TEXT," +
                    PRESS + " TEXT," +
                    THUMBNAILIMAGEURL + " TEXT," +
                    LINK + " TEXT," +
                    SECTIONNAME + " TEXT" + " );" +
                    "CREATE INDEX article_index_ ON " + TABLE_NAMES.get(i) + " (" + ARTICLE_MAIN_INDEX + ");");
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (int i=0; i<TABLE_NAMES.size(); ++i) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAMES.get(i));
        }
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public Cursor selectAll(SQLiteDatabase db, String table_section){
        String[] projection = {
                this.ARTICLE_MAIN_INDEX,
                this.ARTICLEID,
                this.ARTICLETITLE,
                this.PRESS,
                this.THUMBNAILIMAGEURL,
                this.LINK,
                this.SECTIONNAME
        };

        return db.query(
                this.TABLE_NAME_MAP.get(table_section),                     // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                // The sort order
        );
    }

    public Cursor selectIndex(SQLiteDatabase db, String table_section, int min_index, int max_index){
        Log.e("select_index", this.TABLE_NAME_MAP.get(table_section));
        String[] projection = {
                this.ARTICLE_MAIN_INDEX,
                this.ARTICLEID,
                this.ARTICLETITLE,
                this.PRESS,
                this.THUMBNAILIMAGEURL,
                this.LINK,
                this.SECTIONNAME
        };

        return db.query(
                this.TABLE_NAME_MAP.get(table_section),                     // The table to query
                projection,                               // The columns to return
                this.ARTICLE_MAIN_INDEX + " >= " + String.valueOf(min_index) + " AND " + this.ARTICLE_MAIN_INDEX + " <= " + String.valueOf(max_index),
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                // The sort order
        );
    }

    /*public Cursor selectSectionIndex(SQLiteDatabase db, String section, int min_index, int max_index){
        String[] projection = {
                this.ARTICLE_MAIN_INDEX,
                this.ARTICLEID,
                this.ARTICLETITLE,
                this.PRESS,
                this.THUMBNAILIMAGEURL,
                this.LINK,
                this.SECTIONNAME
        };

        return db.query(
                this.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                this.SECTIONNAME + " = '" + section + "' AND " + this.ARTICLE_MAIN_INDEX + " >= " + String.valueOf(min_index) + " AND " + this.ARTICLE_MAIN_INDEX + " <= " + String.valueOf(max_index),
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                // The sort order
        );
    }*/

    public Cursor selectListElement(SQLiteDatabase db, String table_section){
        String[] projection = {
                this.ARTICLEID,
                this.ARTICLETITLE,
                this.PRESS,
                this.THUMBNAILIMAGEURL
        };

        return db.query(
                this.TABLE_NAME_MAP.get(table_section),                     // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                // The sort order
        );
    }

    public long insertAll(SQLiteDatabase db, String table_section, ContentValues values){
        return db.insert(this.TABLE_NAME_MAP.get(table_section), null, values);
    }

    /*public Cursor selectSection(SQLiteDatabase db, String section){
        String[] projection = {
                this.ARTICLEID,
                this.ARTICLETITLE,
                this.PRESS,
                this.THUMBNAILIMAGEURL
        };

        String selection = this.SECTIONNAME + " = ?";
        String[] selectionArgs = { section };

        return db.query(
                this.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                // The sort order
        );
    }*/
}
