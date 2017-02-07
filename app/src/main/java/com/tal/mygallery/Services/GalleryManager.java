package com.tal.mygallery.Services;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.provider.MediaStore;

import com.tal.mygallery.DB.DBManager;
import com.tal.mygallery.Moduls.Photo;
import com.tal.mygallery.MyApplication;
import com.tal.mygallery.Network.ApiManager;
import com.tal.mygallery.R;

import org.json.JSONArray;

import java.io.File;
import java.util.Date;

/**
 * Created by tal on 04/01/17.
 */
public class GalleryManager extends Service {

    public static final String REQUEST_FACEBOOK_GALLERY = "facebook_gallery";
    public static final String REQUEST_PHONE_GALLERY = "phone_gallery";
    public static final String KEY_RECEIVER = "receiver";
    public static final String KEY_API_METHOD_FACEBOOK = "api_method_facebook";
    public static final String KEY_API_METHOD_PHONE = "api_method_phone";
    public static final String KEY_ACCESS_TOKEN = "accessToken";
    public static final String KEY_TYPE = "type";

    public static final String KEY_IMAGE_DONE = "imageDone";
    public static final String KEY_TIME_DONE = "timeDone";
    public static final int FbImageReady = 0;
    public static final int GalleryImageReady = 1;
    public static final int FbImageDownloadDone = 2;
    public static final int GalleryImageDownloadDone = 3;

    public ResultReceiver resultReceiver;

    public GalleryManager() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            resultReceiver = intent.getParcelableExtra(KEY_RECEIVER);

            if (intent.getStringExtra(KEY_API_METHOD_FACEBOOK).equals(REQUEST_FACEBOOK_GALLERY)) {
                final String accesstoken = intent.getStringExtra(KEY_ACCESS_TOKEN);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Long startT = System.currentTimeMillis();
                        JSONArray jsonArray = ApiManager.getGallery(accesstoken);
                        if (jsonArray != null) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String albumId = jsonArray.optJSONObject(i).optString("id");
                                String getPhotosUrl = ApiManager.buildUrl(albumId, accesstoken);
                                ApiManager.getPhotosFromAlbum(getPhotosUrl);
                                sendEvent(FbImageReady);
                            }
                            String sum = String.valueOf(System.currentTimeMillis() - startT);
                            timeInsert(sum, getString(R.string.facebook));
                            sendEvent(FbImageDownloadDone);
                        }
                    }
                }).start();
            }
            if (intent.getStringExtra(KEY_API_METHOD_PHONE).equals(REQUEST_PHONE_GALLERY)) {
                getPhoneGallery();
            }

        }
        return START_NOT_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public void getPhoneGallery() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    long startT = System.currentTimeMillis();
                    final String[] columns = { MediaStore.Images.Media.DATA};

                    final Cursor cursor = getContentResolver().query(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, null);
                    final int count = cursor.getCount();
                    final String[] arrPath = new String[count];


                    for (int i = 0; i < count; i++) {
                        cursor.moveToPosition(i);
                        arrPath[i] = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                        File file = new File(arrPath[i]);
                        Date date = new Date(file.lastModified());
                        String create = date.toString();
                        Photo photo = new Photo(arrPath[i], create);
                        DBManager.getInstance(MyApplication.getInstance()).addPhonePhoto(photo);
                        sendEvent(GalleryImageReady);
                    }
                    String sum = String.valueOf(System.currentTimeMillis() - startT);
                    timeInsert(sum, getString(R.string.phone));
                    sendEvent(GalleryImageDownloadDone);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void sendEvent(int event) {
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_IMAGE_DONE, event);
        resultReceiver.send(Activity.RESULT_OK, bundle);
    }

    private void timeInsert(String time, String type) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TIME_DONE, time);
        bundle.putString(KEY_TYPE, type);
        resultReceiver.send(Activity.RESULT_OK, bundle);
    }

}
