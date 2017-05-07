package com.example.rho_eojin1.a409_prototype13;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Rho-Eojin1 on 2017. 5. 8..
 */

public class DBHelperMain extends SQLiteOpenHelper {
    private static DBHelperMain sInstance;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "News.db";
    public static final String TABLE_NAME = "main_table";
    public static final String ARTICLEID = "ArticleID";
    public static final String ARTICLETITLE = "ArticleTitle";
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
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                ARTICLEID + " TEXT," +
                ARTICLETITLE + " TEXT," +
                PRESS + " TEXT," +
                THUMBNAILIMAGEURL + " TEXT," +
                LINK + " TEXT," +
                SECTIONNAME + " TEXT" + " )");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public Cursor selectAll(SQLiteDatabase db){
        String[] projection = {
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
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                // The sort order
        );
    }

    public Cursor selectListElement(SQLiteDatabase db){
        String[] projection = {
                this.ARTICLEID,
                this.ARTICLETITLE,
                this.PRESS,
                this.THUMBNAILIMAGEURL
        };

        return db.query(
                this.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                // The sort order
        );
    }

    public long insertAll(SQLiteDatabase db, ContentValues values){
        return db.insert(this.TABLE_NAME, null, values);
    }

    public Cursor selectSection(SQLiteDatabase db, String section){
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
    }
}
