/**
 * Pieter Kronemeijer (11064838)
 *
 * This adapter handles the switching of the tabs. It extends FragmentPagerAdapter instead
 * of FragmentStatePagerAdapter, since the tabs are lightweight.
 */

package com.example.pieter.papper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


class TabPagerAdapter extends FragmentPagerAdapter {
    private final int nTabs;

    TabPagerAdapter(FragmentManager fm, int nTabs) {
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