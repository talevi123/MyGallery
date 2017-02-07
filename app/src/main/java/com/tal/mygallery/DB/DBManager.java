package com.tal.mygallery.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.tal.mygallery.Moduls.Photo;

/**
 * Created by tal on 28/12/16.
 */
public class DBManager {

    private static DBManager instance;

    private static final Object key = new Object();
    private SQLiteDatabase db;
    private SQLiteHelper dbHelper;


    public static DBManager getInstance(Context context) {
        if (instance == null) {
            instance = new DBManager(context);
        }
        return instance;
    }

    public DBManager(Context context) {
        dbHelper = new SQLiteHelper(context);
        open();
    }

    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void addFacebookPhoto(Photo photo) {
        ContentValues cv = new ContentValues();
        cv.put(SQLiteHelper.COLUMN_PICTURE, photo.getPhotoPath());
        cv.put(SQLiteHelper.COLUMN_CREATED_TIME, photo.getCreateTime());
        synchronized (key) {
            db.insert(SQLiteHelper.TABLE_FACEBOOK_GALLERY, null, cv);
        }
    }

    public void addPhonePhoto(Photo photo) {
        ContentValues cv = new ContentValues();
        cv.put(SQLiteHelper.COLUMN_PICTURE, photo.getPhotoPath());
        cv.put(SQLiteHelper.COLUMN_CREATED_TIME, photo.getCreateTime());
        db.insert(SQLiteHelper.TABLE_PHONE_GALLERY, null, cv);
    }

    public Cursor getAllFacebookPhotos() {
        Cursor cursor = db.query(SQLiteHelper.TABLE_FACEBOOK_GALLERY, null, null, null, null, null, null);
        cursor.moveToFirst();
        return cursor;
    }

    public Cursor getAllPhonePhotos() {
        Cursor cursor = db.query(SQLiteHelper.TABLE_PHONE_GALLERY, null, null, null, null, null, null);
        cursor.moveToFirst();
        return cursor;
    }

    public void deleteGallery() {
        db.delete(SQLiteHelper.TABLE_FACEBOOK_GALLERY, null, null);
        db.delete(SQLiteHelper.TABLE_PHONE_GALLERY, null, null);
    }
}
