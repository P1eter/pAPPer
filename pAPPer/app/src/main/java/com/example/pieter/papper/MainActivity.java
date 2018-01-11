package com.example.pieter.papper;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void makeTabs() {
        TabLayout tabLayout = findViewById(R.id.tab_layout);
//        tabLayout.;
//        tabLayout.;
//        tabLayout.;
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        ViewPager viewPager = findViewById(R.id.tab_view_pager);
        TabPagerAdapter tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(tabPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }



    // fragmentpager, since FragmentStatePagerAdapter is for more and heavier tabs
    private class TabPagerAdapter extends FragmentPagerAdapter {
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



}
