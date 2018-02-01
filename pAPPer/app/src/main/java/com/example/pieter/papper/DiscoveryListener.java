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


class DiscoveryListener implements NsdManager.DiscoveryListener {
    private static final String TAG = "DiscoveryListener";
    private static final String SERVICE_TYPE = "_naoqi._tcp";
    private static String DEFAULT_ROBOT_ENTRY;
    private NsdManager mNsdManager;
    final ArrayList<String> availableRobots = new ArrayList<>();

    DiscoveryListener(Context baseContext) {
        DEFAULT_ROBOT_ENTRY = baseContext.getString(R.string.default_robot_spinner_entry);
        availableRobots.add(DEFAULT_ROBOT_ENTRY);

        try {
            mNsdManager = (NsdManager) baseContext.getSystemService(Context.NSD_SERVICE);
            mNsdManager.discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, this);
        } catch (NullPointerException e) {
            Log.e(TAG, "NsdManager not found", e);
        }
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
        if (mNsdManager != null) {
            mNsdManager.stopServiceDiscovery(this);
        }
    }

    @Override
    public void onStopDiscoveryFailed(String serviceType, int errorCode) {
        Log.e(TAG, "Discovery failed: Error code:" + errorCode);
        if (mNsdManager != null) {
            mNsdManager.stopServiceDiscovery(this);
        }
    }
}
