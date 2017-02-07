package com.tal.mygallery.Activites;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Toast;

import com.tal.mygallery.Adapters.PagerGridAdapter;
import com.tal.mygallery.DB.DBManager;
import com.tal.mygallery.Fragments.GalleryFragment;
import com.tal.mygallery.R;
import com.tal.mygallery.Services.GalleryManager;
import com.tal.mygallery.Services.MyResultReceiver;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements MyResultReceiver.Receiver {

    private ViewPager viewPager;
    private TabLayout tabs;
    private PagerGridAdapter pagerAdapter;
    private MyResultReceiver resultReceiver;
    private List<Fragment> fragments;
    private static final int FACEBOOK_FRAGMENT = 0;
    private static final int GALLERY_FRAGMENT = 1;
    int currentFragmentPosition = FACEBOOK_FRAGMENT;
    private String facebookT;
    private String phoneT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String accessToken;
        Bundle bd = getIntent().getExtras();
        if(bd != null) {
            accessToken = bd.getString("accessToken");
            PreferenceManager.getDefaultSharedPreferences(this).edit().putString("Access", accessToken).apply();
        }

        accessToken = PreferenceManager.getDefaultSharedPreferences(this).getString("Access", null);

        DBManager.getInstance(this).deleteGallery();
        startDownload(accessToken);

        setViewPager();
    }

    public void startDownload(String accessToken) {
        if (accessToken != null) {
            resultReceiver = new MyResultReceiver(new Handler(), this);
            Bundle bundle = new Bundle();
            bundle.putParcelable(GalleryManager.KEY_RECEIVER, resultReceiver);
            bundle.putString(GalleryManager.KEY_ACCESS_TOKEN, accessToken);
            bundle.putString(GalleryManager.KEY_API_METHOD_FACEBOOK, GalleryManager.REQUEST_FACEBOOK_GALLERY);
            bundle.putString(GalleryManager.KEY_API_METHOD_PHONE, GalleryManager.REQUEST_PHONE_GALLERY);
            Intent serviceIntent = new Intent(this, GalleryManager.class);
            serviceIntent.putExtras(bundle);
            startService(serviceIntent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        menu.add(getString(R.string.phone_sum) + DBManager.getInstance(this).getAllPhonePhotos().getCount());
        menu.add(getString(R.string.phone_time) + phoneT);
        menu.add(getString(R.string.facebook_sum) + DBManager.getInstance(this).getAllFacebookPhotos().getCount());
        menu.add(getString(R.string.facebook_time) + facebookT );
        return super.onPrepareOptionsMenu(menu);
    }

    private void setViewPager() {
        tabs = (TabLayout) findViewById(R.id.tab_host);
        tabs.setTabGravity(TabLayout.GRAVITY_FILL);


        viewPager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new PagerGridAdapter(getSupportFragmentManager(), getFragments());
        viewPager.setAdapter(pagerAdapter);
        tabs.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentFragmentPosition = position;
                switch (currentFragmentPosition) {
                    case GALLERY_FRAGMENT:
                        //int c = (DBManager.getInstance(MyApplication.getInstance()).getAllPhonePhotos()).getCount() == 1 ? GalleryManager.NULL_CURSOR : 1;
                        ((GalleryFragment) fragments.get(GALLERY_FRAGMENT)).updateAdapter(R.string.phone);
                        break;
                    case FACEBOOK_FRAGMENT:
                        ((GalleryFragment) fragments.get(FACEBOOK_FRAGMENT)).updateAdapter(R.string.facebook);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private List<Fragment> getFragments() {
        fragments = new ArrayList<>();
        fragments.add(new GalleryFragment().newInstance("Facebook"));
        fragments.add(new GalleryFragment().newInstance("Phone"));
        return fragments;
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle bundle) {
        int event = bundle.getInt(GalleryManager.KEY_IMAGE_DONE);
        switch (event) {
            case GalleryManager.FbImageReady :
                if (currentFragmentPosition == FACEBOOK_FRAGMENT) {
                    ((GalleryFragment) fragments.get(FACEBOOK_FRAGMENT)).updateAdapter(R.string.facebook);
                }
                    break;
            case GalleryManager.FbImageDownloadDone :
                Toast.makeText(this,"Download from facebook is done",Toast.LENGTH_SHORT).show();
                break;
            case GalleryManager.GalleryImageReady :
                if (currentFragmentPosition == GALLERY_FRAGMENT) {
                    ((GalleryFragment) fragments.get(GALLERY_FRAGMENT)).updateAdapter(R.string.phone);
                }
                break;
            case GalleryManager.GalleryImageDownloadDone :
                Toast.makeText(this,"Download from phone gallery is done",Toast.LENGTH_SHORT).show();
                break;
        }

        String totalTime = bundle.getString(GalleryManager.KEY_TIME_DONE);
        String type = bundle.getString(GalleryManager.KEY_TYPE);

        if (type != null && type.equals(getString(R.string.facebook))) {
            facebookT = totalTime + getString(R.string.ms);
        } else if (type != null && type.equals(getString(R.string.phone))) {
            phoneT = totalTime + getString(R.string.ms);
        }
    }

}

