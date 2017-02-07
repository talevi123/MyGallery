package com.tal.mygallery.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by tal on 28/12/16.
 */
public class PagerGridAdapter extends FragmentPagerAdapter {

    private String[] tabTitles = new String[]{"Facebook", "Phone"};
    private List<Fragment> fragments;

    public PagerGridAdapter(FragmentManager fm, List<Fragment> fragments ) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
      return this.fragments.get(position);
    }

    @Override
    public int getCount() {
        return this.fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

}
