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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rho-Eojin1 on 2017. 5. 7..
 */

public class DBHelperContent extends SQLiteOpenHelper {
    private static DBHelperContent sInstance;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Contents.db";
    Map<String, String> TABLE_NAME_MAP = new HashMap<String, String>();
    ArrayList<String> TABLE_NAMES;
    //public static final String TABLE_NAME = "content_table";
    public static final String ARTICLEID = "ArticleID";
    public static final String ARTICLETYPE = "ArticleType";
    public static final String ARTICLEINDEX = "ArticleIndex";
    public static final String CONTENT = "content";
    public static final String LINKURL="url";
    public static final String IMAGETAG = "tag";
    // 추가할 변수들 : link URL, 비디오 등

    public static synchronized DBHelperContent getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DBHelperContent(context.getApplicationContext());
            Log.e("DBHelperContentInit","DBHelperContentInit");
        }
        return sInstance;
    }

    public DBHelperContent (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        TABLE_NAME_MAP.put("홈","content_table_home");
        TABLE_NAME_MAP.put("정치","content_table_politics");
        TABLE_NAME_MAP.put("경제","content_table_economy");
        TABLE_NAME_MAP.put("사회","content_table_society");
        TABLE_NAME_MAP.put("IT","content_table_it");
        TABLE_NAME_MAP.put("생활","content_table_living");
        TABLE_NAME_MAP.put("세계","content_table_world");
        TABLE_NAMES = new ArrayList<String>(TABLE_NAME_MAP.values());
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        for (int i=0; i<TABLE_NAMES.size(); ++i) {
            sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAMES.get(i) + " (" +
                    ARTICLEID + " TEXT, " +
                    ARTICLETYPE + " TEXT, " +
                    ARTICLEINDEX + " INTEGER, " +
                    CONTENT + " TEXT " +
                    /*
                     + "," +
                    LINKURL + "TEXT, " +
                    IMAGETAG + "TEXT " +
                    */
                    " )");
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e("Delete called?","yes");
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
                this.ARTICLEID,
                this.ARTICLETYPE,
                this.ARTICLEINDEX,
                this.CONTENT
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
        //Log.e("insert call", this.TABLE_NAME_MAP.get(table_section));
        return db.insert(this.TABLE_NAME_MAP.get(table_section), null, values);
    }

    public Cursor selectArticleID(SQLiteDatabase db, String table_section, String articleID){
        //Log.e("select call", this.TABLE_NAME_MAP.get(table_section));
        //Log.e("select call", String.valueOf(articleID));

        String[] projection = {
                this.ARTICLEID,
                this.ARTICLETYPE,
                this.ARTICLEINDEX,
                this.CONTENT
        };

        String selection = this.ARTICLEID + " = ?";
        String[] selectionArgs = { articleID };

        String sortOrder = this.ARTICLEINDEX + " ASC";

        return db.query(
                this.TABLE_NAME_MAP.get(table_section),                     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                // The sort order
        );
    }
}
