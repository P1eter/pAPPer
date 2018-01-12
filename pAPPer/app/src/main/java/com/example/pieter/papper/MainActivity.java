package com.example.pieter.papper;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    String[] TABTEXTS = {"Talk", "Walk", "Dance"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        makeTabs();
    }

    private void makeTabs() {
        TabLayout tabLayout = findViewById(R.id.tab_layout);
//        tabLayout.;
//        tabLayout.;
//        tabLayout.;
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        TabPagerAdapter tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        ViewPager viewPager = findViewById(R.id.tab_view_pager);
        viewPager.setAdapter(tabPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(i).setText(TABTEXTS[i]);
        }
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
            System.out.println("NEXT FRAGMENT");
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

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.options_menuitem:
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                RobotSelectFragment fragment = new RobotSelectFragment();
                fragment.show(ft, "dialog");
                return true;
        }
        return false;
    }
}
