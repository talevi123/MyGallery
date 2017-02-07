package com.tal.mygallery.Moduls;

import android.database.Cursor;

import com.tal.mygallery.DB.SQLiteHelper;

import org.json.JSONObject;

/**
 * Created by tal on 27/12/16.
 */
public class Photo{
    public int id;
    public String photo_path;
    public String createTime;

    public static Photo createPhoto(Object object) {
        if (object instanceof Photo) {
            return (Photo) object;
        }
        else if (object instanceof Cursor) {
            return new Photo((Cursor) object);
        } else {
            return new Photo((JSONObject) object);
        }
    }

    public Photo (String photo_path, String createTime) {
        this.photo_path = photo_path;
        this.createTime = createTime;
    }

    public Photo(JSONObject jsonObject) {
        this.photo_path = jsonObject.optString("source");
        this.createTime = jsonObject.optString("created_time");
    }

    public Photo(Cursor cursor) {
        this.id = cursor.getInt(cursor.getColumnIndex(SQLiteHelper.COLUMN_ID));
        this.photo_path = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_PICTURE));
        this.createTime = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_CREATED_TIME));
    }

    public int getId() { return id;}

    public String getPhotoPath() {
        return photo_path;
    }

    public String getCreateTime() {
        return createTime;
    }

}
