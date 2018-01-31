/**
 * Pieter Kronemeijer (11064838)
 *
 * This class handles the detection of available robots on the network.
 */

package com.example.pieter.papper;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;

import java.util.ArrayList;


public class DiscoveryListener implements NsdManager.DiscoveryListener {
    private static final String TAG = "DiscoveryListener";
    private static final String SERVICE_TYPE = "_naoqi._tcp";
    private static final String DEFAULT_ROBOT_ENTRY = "No available robots";
    private NsdManager mNsdManager;
    public ArrayList<String> availableRobots = new ArrayList<>();

    DiscoveryListener(Context baseContext) {
        availableRobots.add(DEFAULT_ROBOT_ENTRY);

        mNsdManager = (NsdManager) baseContext.getSystemService(Context.NSD_SERVICE);
        mNsdManager.discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, this);
    }

    @Override
    public void onDiscoveryStarted(String regType) {
        Log.d(TAG, "Service discovery started");
    }

    @Override
    public void onServiceFound(NsdServiceInfo service) {
        // check if only default list entry is present
        if (availableRobots.size() > 0 && availableRobots.get(0).equals(DEFAULT_ROBOT_ENTRY)) {
            availableRobots.clear();
        }

        availableRobots.add(service.getServiceName());
    }

    @Override
    public void onServiceLost(NsdServiceInfo service) {
        availableRobots.remove(service.getServiceName());

        if (availableRobots.size() == 0) {
            availableRobots.add(DEFAULT_ROBOT_ENTRY);
        }

        Log.d(TAG, "Service lost: " + service);
    }

    @Override
    public void onDiscoveryStopped(String serviceType) {
        Log.d(TAG, "Discovery stopped: " + serviceType);
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
