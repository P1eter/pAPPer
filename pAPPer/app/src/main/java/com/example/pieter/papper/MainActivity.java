package com.example.pieter.papper;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import static java.lang.Math.min;

public class MainActivity extends AppCompatActivity implements OnConnectionChangedListener {
    private String[] TABTEXTS = {"Talk", "Walk", "Dance"};
    private NetworkSender networkSender = NetworkSender.getInstance();
    private Toolbar toolbar;
    private MenuItem connectIcon;
    private DiscoveryListener mDiscoveryListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        makeTabs();

        initializeDiscoveryListener();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onConnectionChanged(boolean connected) {
        if (connected) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    connectIcon.setIcon(R.drawable.connect_symbol_green);
                }
            });
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    connectIcon.setIcon(R.drawable.connect_symbol_red);
                }
            });
        }
    }

    private void makeTabs() {
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        TabPagerAdapter tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        ViewPager viewPager = findViewById(R.id.tab_view_pager);
        viewPager.setAdapter(tabPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < min(tabLayout.getTabCount(), TABTEXTS.length); i++) {
            tabLayout.getTabAt(i).setText(TABTEXTS[i]);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actions, menu);
        connectIcon = menu.findItem(R.id.options_menuitem);
        networkSender.setCallback(this);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.options_menuitem:
                Bundle arguments = new Bundle();
                arguments.putStringArrayList("robots", mDiscoveryListener.availableRobots);

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                RobotSelectFragment fragment = new RobotSelectFragment();
                fragment.setArguments(arguments);
                fragment.show(ft, "dialog");
                return true;
        }
        return false;
    }

    public void initializeDiscoveryListener() {

        // Instantiate a new DiscoveryListener
        System.out.println("made discoverylistener");
        mDiscoveryListener = new DiscoveryListener(getBaseContext());
    }
}
