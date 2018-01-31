package com.example.pieter.papper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by pieter on 31-1-18.
 */

// fragmentpager, since FragmentStatePagerAdapter is for more and heavier tabs
public class TabPagerAdapter extends FragmentPagerAdapter {
    int nTabs;

    public TabPagerAdapter(FragmentManager fm, int nTabs) {
        super(fm);
        this.nTabs = nTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return new TalkFragment();
            case 1:
                return new MoveFragment();
            case 2:
                return new DanceFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return nTabs;
    }
}