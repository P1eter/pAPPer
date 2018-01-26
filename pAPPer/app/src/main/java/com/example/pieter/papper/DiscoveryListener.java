package com.example.pieter.papper;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by pieter on 26-1-18.
 */

//public class DiscoveryListener implements NsdManager.DiscoveryListener {
//}

public class DiscoveryListener implements NsdManager.DiscoveryListener {
    private static final String TAG = "DiscoveryListener";
    private NsdManager mNsdManager;
    private String SERVICE_TYPE = "_naoqi._tcp";
    public ArrayList<String> availableRobots = new ArrayList<>();

    DiscoveryListener(Context baseContext) {
        availableRobots.add("No available robots");
        mNsdManager = (NsdManager) baseContext.getSystemService(Context.NSD_SERVICE);
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
        System.out.println("found a service!");
        System.out.println(service.getHost());
        System.out.println(service.getPort());
        System.out.println(service.getServiceName());
        System.out.println(service.getServiceType());
        System.out.println(service.toString());

        if (availableRobots.size() > 0 && availableRobots.get(0) == "No available robots") {
            availableRobots.clear();
        }

        availableRobots.add(service.getServiceName().toString());

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
        availableRobots.remove(service.getHost().toString());
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
