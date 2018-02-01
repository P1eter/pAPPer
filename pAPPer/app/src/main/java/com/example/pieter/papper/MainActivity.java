/**
 * Pieter Kronemeijer (11064838)
 *
 * This is a wrapper class around the tabs in the app. It also initializes
 * the network discovery and sets the menu item.
 */

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
    private final String[] TABTEXTS = {"Talk", "Walk", "Dance"};
    private final NetworkSender networkSender = NetworkSender.getInstance();
    private MenuItem connectIcon;
    private DiscoveryListener mDiscoveryListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.tool_bar);
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

        TabPagerAdapter tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(),
                tabLayout.getTabCount());
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

        // callback for 'onConnectionChanged', to set connection icon
        networkSender.setCallback(this);
        connectIcon = menu.findItem(R.id.connect_menuitem);

        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.connect_menuitem:
                toConnectFragment();
                return true;
        }
        return false;
    }

    private void toConnectFragment() {
        // put available robots as argument to the connection fragment
        Bundle arguments = new Bundle();
        arguments.putStringArrayList("robots", mDiscoveryListener.availableRobots);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        RobotSelectFragment fragment = new RobotSelectFragment();
        fragment.setArguments(arguments);
        fragment.show(ft, "dialog");
    }

    public void initializeDiscoveryListener() {
        mDiscoveryListener = new DiscoveryListener(getBaseContext());
    }
}
