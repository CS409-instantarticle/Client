package com.example.rho_eojin1.a409_prototype13;

/**
 * Created by Rho-Eojin1 on 2017. 5. 8..
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Rho-Eojin1 on 2017. 5. 7..
 */

public class DBHelperContent extends SQLiteOpenHelper {
    private static DBHelperContent sInstance;
    public static final int DATABASE_VERSION = 1;
    public String tmp;
    public static final String DATABASE_NAME = "News.db";
    public static final String TABLE_NAME = "content_table";
    public static final String ARTICLEID = "ArticleID";
    public static final String ARTICLETYPE = "ArticleType";
    public static final String ARTICLEINDEX = "ArticleIndex";
    public static final String CONTENT = "content";

    public static synchronized DBHelperContent getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DBHelperContent(context.getApplicationContext());
            Log.e("DBHelperContentInit","DBHelperContentInit");
        }
        return sInstance;
    }

    public DBHelperContent (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                ARTICLEID + " TEXT, " +
                ARTICLETYPE + " TEXT, " +
                ARTICLEINDEX + " INTEGER, " +
                CONTENT + " TEXT" + " )");
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
                this.ARTICLETYPE,
                this.ARTICLEINDEX,
                this.CONTENT
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

    public Cursor selectArticleID(SQLiteDatabase db, String articleID){
        String[] projection = {
                this.ARTICLEID,
                this.ARTICLETYPE,
                this.ARTICLEINDEX,
                this.CONTENT
        };

        String selection = this.ARTICLEID + " = ?";
        String[] selectionArgs = { articleID };

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
