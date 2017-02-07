package com.tal.mygallery.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by tal on 28/12/16.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "myGallery.db";
    private static final int DATABASE_VERSION = 1;

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_FACEBOOK_GALLERY);
        db.execSQL(CREATE_PHONE_GALLERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    public static final String TABLE_PHONE_GALLERY = "phone_gallery";
    public static final String TABLE_FACEBOOK_GALLERY = "facebook_gallery";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PICTURE = "picture_path";
    public static final String COLUMN_CREATED_TIME = "created_time";


    private static final String CREATE_FACEBOOK_GALLERY = "CREATE TABLE " + TABLE_FACEBOOK_GALLERY +
             "( " +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_PICTURE + " TEXT," +
            COLUMN_CREATED_TIME + " TEXT" +
            " )";


    private static final String CREATE_PHONE_GALLERY = "CREATE TABLE " + TABLE_PHONE_GALLERY +
            "( " +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_PICTURE + " TEXT," +
            COLUMN_CREATED_TIME + " TEXT" +
            " )";

}
