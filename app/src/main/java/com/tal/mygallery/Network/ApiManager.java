package com.tal.mygallery.Network;

import com.tal.mygallery.DB.DBManager;
import com.tal.mygallery.Moduls.Photo;
import com.tal.mygallery.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tal on 27/12/16.
 */
public class ApiManager {

    private static final String BASE_URL = "https://graph.facebook.com/";
    private static final String ALBUM_URL = "me/albums?fields=id&access_token=";
    private static final String PHOTO_URL = "/photos?fields=created_time,source&access_token=";

    public static JSONArray getGallery(String accessToken) {
        String result = ConnectionManager.sendGetRequest(BASE_URL + ALBUM_URL + accessToken);
        if (result != null) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.optJSONArray("data");
                return jsonArray;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void getPhotosFromAlbum(String photosUrl) {
        String result = ConnectionManager.sendGetRequest(photosUrl);
        if (result != null) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.optJSONArray("data");
                if (jsonArray != null) {
                    for (int i = 0; i< jsonArray.length(); i++) {
                        Photo photo = new Photo(jsonArray.optJSONObject(i));
                        DBManager.getInstance(MyApplication.getInstance()).addFacebookPhoto(photo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static String buildUrl(String albumId, String accessToken) {
        return BASE_URL + albumId + PHOTO_URL + accessToken;
    }

}