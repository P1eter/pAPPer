package com.example.pieter.papper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.min;


public class MainActivity extends AppCompatActivity {
    private String[] TABTEXTS = {"Talk", "Walk", "Dance"};
    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private BroadcastReceiver mReceiver;
    private IntentFilter mIntentFilter;
    private int SERVER_PORT = 1717;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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


        NetworkSend networkSend = new NetworkSend();
        networkSend.execute();
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

    private class NetworkSend extends AsyncTask<Void, Void, Void> {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;

        public void send(String message) {
            out.println(message);
        }

        public void close() {
            try {
                socket.close();
            } catch (IOException e) {
                // TODO: make this a nice log statement
                System.out.println("Failed to close connection, with error: " + e);
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // TODO: make these variables parameters
            final String host = "Pepper";
            final int portNumber = 1717;
            System.out.println("Creating socket to '" + host + "' on port " + portNumber);

            while (true) {
                try {
                    socket = new Socket(host, portNumber);
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    out = new PrintWriter(socket.getOutputStream(), true);


//                    System.out.println("server says:" + br.readLine());

//                    BufferedReader userInputBR = new BufferedReader(new InputStreamReader(System.in));
//                    String userInput = userInputBR.readLine();
                    String userInput = "OMG ... HET ... WERKT!!!ONE!1";
                    send(userInput);

//                    out.println(userInput);
//
                    System.out.println("server says:" + in.readLine());
//
                    System.out.println("server says:" + in.readLine());

//                    if ("exit".equalsIgnoreCase(userInput)) {
//                        socket.close();
//                        break;
//                    }
                    break;
                } catch (IOException e) {
                    // TODO: make this a log statement with more info :)
                    System.out.println("something bad happened, SHIT: " + e);
                    break;
                }
            }

            return null;
        }
    }


    public void initializeDiscoveryListener() {

        // Instantiate a new DiscoveryListener
        System.out.println("made discoverylistener");
        DiscoveryListener mDiscoveryListener = new DiscoveryListener();
    }







    private class DiscoveryListener implements NsdManager.DiscoveryListener {
        private static final String TAG = "DiscoveryListener";
        private NsdManager mNsdManager = (NsdManager) getBaseContext().getSystemService(Context.NSD_SERVICE);
        private String SERVICE_TYPE = "_naoqi._tcp";

        DiscoveryListener() {
            mNsdManager.discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, this);
            System.out.println("DiscoveryListener() IS MADE");
        }

        // Called as soon as service discovery begins.
        @Override
        public void onDiscoveryStarted(String regType) {
            Log.d(TAG, "Service discovery started");
        }

        @Override
        public void onServiceFound(NsdServiceInfo service) {
            System.out.println(service.getHost());
            System.out.println(service.getPort());
            System.out.println(service.getServiceName());
            System.out.println(service.getServiceType());
            System.out.println(service.toString());

//            // A service was found! Do something with it.
//            Log.d(TAG, "Service discovery success" + service);
//            if (!service.getServiceType().equals(SERVICE_TYPE)) {
//                // Service type is the string containing the protocol and
//                // transport layer for this service.
//                Log.d(TAG, "Unknown Service Type: " + service.getServiceType());
//            } else if (service.getServiceName().equals(mServiceName)) {
//                // The name of the service tells the user what they'd be
//                // connecting to. It could be "Bob's Chat App".
//                Log.d(TAG, "Same machine: " + mServiceName);
//            } else if (service.getServiceName().contains("NsdChat")){
//                mNsdManager.resolveService(service, mResolveListener);
//            }
        }

        @Override
        public void onServiceLost(NsdServiceInfo service) {
            // When the network service is no longer available.
            // Internal bookkeeping code goes here.
            Log.e(TAG, "service lost" + service);
        }

        @Override
        public void onDiscoveryStopped(String serviceType) {
            Log.i(TAG, "Discovery stopped: " + serviceType);
        }

        @Override
        public void onStartDiscoveryFailed(String serviceType, int errorCode) {
            Log.e(TAG, "Discovery failed: Error code:" + errorCode);
            mNsdManager.stopServiceDiscovery(this);
        }

        @Override
        public void onStopDiscoveryFailed(String serviceType, int errorCode) {
            Log.e(TAG, "Discovery failed: Error code:" + errorCode);
            mNsdManager.stopServiceDiscovery(this);
        }
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
