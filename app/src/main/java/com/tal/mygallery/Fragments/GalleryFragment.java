package com.tal.mygallery.Fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.tal.mygallery.Activites.PhotoActivity;
import com.tal.mygallery.Adapters.GridAdapter;
import com.tal.mygallery.DB.DBManager;
import com.tal.mygallery.Moduls.Photo;
import com.tal.mygallery.R;

/**
 * Created by tal on 29/12/16.
 */
public class GalleryFragment extends Fragment {

    public static final String KEY_PAGE_NAME = "page_name";

    GridView gridView;
    GridAdapter gridAdapter;
    String message;

    public static final GalleryFragment newInstance(String pageName) {
        GalleryFragment galleryFragment = new GalleryFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_PAGE_NAME, pageName);
        galleryFragment.setArguments(bundle);
        return galleryFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        message = getArguments().getString(KEY_PAGE_NAME);

        gridView = (GridView) view.findViewById(R.id.grid);

        initPhoto();

        return view;
    }

    public void updateAdapter(int type) {
        Cursor cursor;
        if (getString(type).equals("Facebook")) {
            cursor = DBManager.getInstance(getActivity()).getAllFacebookPhotos();
            if (gridAdapter == null) {
                gridAdapter = new GridAdapter(getActivity(), cursor, R.string.facebook);
                gridView.setAdapter(gridAdapter);
            }
            else {
                gridAdapter.swapCursor(cursor);
            }
        } else {
            cursor = DBManager.getInstance(getActivity()).getAllPhonePhotos();
            if (gridAdapter == null) {
                gridAdapter = new GridAdapter(getActivity(), cursor, R.string.phone);
                gridView.setAdapter(gridAdapter);
            } else {
                gridAdapter.swapCursor(cursor);
            }
        }


    }

    public void initPhoto() {
        if (gridView != null) {
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    Photo photo = Photo.createPhoto(gridView.getItemAtPosition(position));
                    Intent intent = new Intent(getActivity(), PhotoActivity.class);
                    intent.putExtra("image", photo.getPhotoPath());
                    intent.putExtra("created_time", photo.getCreateTime());
                    if (message.equals(getString(R.string.facebook))) {
                        intent.putExtra("Type", getString(R.string.facebook));
                    } else {
                        intent.putExtra("Type", getString(R.string.phone));
                    }
                    startActivity(intent);
                }
            });
        }
    }

}
