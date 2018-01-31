package com.example.pieter.papper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import static java.lang.Math.min;

//import NetworkSender;


public class MainActivity extends AppCompatActivity implements OnConnectionChangedListener {
    private String[] TABTEXTS = {"Talk", "Walk", "Dance"};
//    private WifiP2pManager mManager;
//    private WifiP2pManager.Channel mChannel;
//    private BroadcastReceiver mReceiver;
//    private IntentFilter mIntentFilter;
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

//        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
//        mChannel = mManager.initialize(this, getMainLooper(), null);
//        mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this);
//
//        mIntentFilter = new IntentFilter();
//        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
//        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
//        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
//        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        makeTabs();

//        startRegistration();
//
//        searchPeers();

//        new sendstuffclass().execute();

        initializeDiscoveryListener();


//        networkSender = NetworkSender.getInstance();
//        new Thread(networkSender).start();
//
//        networkSender.talk("Hi! My name is Pepper!");
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    //    /* register the broadcast receiver with the intent values to be matched */
//    @Override
//    protected void onResume() {
//        super.onResume();
//        registerReceiver(mReceiver, mIntentFilter);
//    }
//
//    /* unregister the broadcast receiver */
//    @Override
//    protected void onPause() {
//        super.onPause();
//        unregisterReceiver(mReceiver);
//    }

//    void searchPeers() {
//        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
//            @Override
//            public void onSuccess() {
//
//            }
//
//            @Override
//            public void onFailure(int reasonCode) {
//
//            }
//        });
//    }


    @Override
    public void onConnectionChanged(boolean connected) {
//        if (connected) {
//            connectIcon.setIcon(R.drawable.connect_symbol_green);
//        } else {
//            connectIcon.setIcon(R.drawable.connect_symbol_red);
//        }
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

//    private void startRegistration() {
//        //  Create a string map containing information about your service.
//        Map record = new HashMap();
//        record.put("listenport", String.valueOf(SERVER_PORT));
//        record.put("buddyname", "John Doe" + (int) (Math.random() * 1000));
//        record.put("available", "visible");
//
//        // Service information.  Pass it an instance name, service type
//        // _protocol._transportlayer , and the map containing
//        // information other devices will want once they connect to this one.
//        WifiP2pDnsSdServiceInfo serviceInfo =
//                WifiP2pDnsSdServiceInfo.newInstance("_test", "_presence._tcp", record);
//
//        // Add the local service, sending the service info, network channel,
//        // and listener that will be used to indicate success or failure of
//        // the request.
//        mManager.addLocalService(mChannel, serviceInfo, new WifiP2pManager.ActionListener() {
//            @Override
//            public void onSuccess() {
//                System.out.println("ADDED TO SERVICE DISCOVERY");
//                // Command successful! Code isn't necessarily needed here,
//                // Unless you want to update the UI or add logging statements.
//            }
//
//            @Override
//            public void onFailure(int arg0) {
//                // Command failed.  Check for P2P_UNSUPPORTED, ERROR, or BUSY
//            }
//        });
//    }

}
