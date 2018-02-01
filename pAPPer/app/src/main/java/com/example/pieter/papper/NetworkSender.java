/**
 * Pieter Kronemeijer (11064838)
 *
 * This singleton class handles all the communication with the robot
 */

package com.example.pieter.papper;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.PriorityQueue;

import static java.lang.Thread.sleep;


class NetworkSender implements Runnable {
    private static final String TAG = "NetworkSender";
    private static NetworkSender instance;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private boolean connectionOpen = false;
    private int port;
    private String host;
    private final PriorityQueue<String> sendQueue = new PriorityQueue<>();
    private OnConnectionChangedListener onConnectionChangedListener;

    private NetworkSender() {
        // empty private constructor of singleton class
    }

    static NetworkSender getInstance() {
        if (instance == null) {
            instance = new NetworkSender();
        }
        return instance;
    }

    @Override
    public void run() {
        if (!openConnection()) {
            return;
        }

        while (checkConnection()) {
            if (!sendQueue.isEmpty()) {
                send(sendQueue.poll());

                // limit the update rate to prevent network flooding (server runs even slower)
                try {
                    sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        // connection can still be open here if an error has occurred
        if (connectionOpen) {
            closeConnection();
        }
    }

    private void send(String message) {
        Log.d(TAG, "sending... \"" + message + "\"");
        try {
            out.write(message + "\n");
            out.flush();
        } catch (NullPointerException e) {
            Log.d(TAG, "Cannot send message!", e);
        }
    }

    void talk(String statement) {
        sendQueue.add("talk " + statement);
    }

    void move(float x, float y, float theta) {
        sendQueue.add("move " + x + " " + y + " " + theta);
    }

    void wakeUp(boolean wakeOrSleep) {
        sendQueue.add("wake " + (wakeOrSleep ? 1 : 0));
    }

    void dance(String dance) {
        sendQueue.add("danc " + dance);
    }

    void autonomousLife(boolean value) {
        sendQueue.add("autl " + (value ? 1 : 0));
    }

    private boolean openConnection() {
        // make sure previous connection is closed before opening a new one
        if (connectionOpen && !closeConnection()) {
            return false;
        }

        try {
            socket = new Socket(host, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            // clear send queue of old commands
            sendQueue.clear();
            connectionOpen = true;
            // invoke callback method notifying an opened connection
            onConnectionChangedListener.onConnectionChanged(true);
        } catch (IOException e) {
            Log.e(TAG, "Failed to open connection", e);
            return false;
        }
        Log.d(TAG, "Opened connection");
        return true;
    }

    boolean closeConnection() {
        // check if connection might be closed already by host
        if (!connectionOpen) {
            return true;
        }

        try {
            socket.close();
            Log.i(TAG, "Host closed connection, disconnecting...");
            sendQueue.clear();

            // clear all data from the previous connection
            socket = null;
            in = null;
            out = null;
            connectionOpen = false;
            // invoke callback method notifying that the connection has closed
            onConnectionChangedListener.onConnectionChanged(false);
            return true;
        } catch (IOException e) {
            Log.e(TAG, "Failed to close socket", e);
            return false;
        }
    }

    /**
     * Check if the connection is still open and valid.
     */
    private boolean checkConnection() {
        try {
            if (connectionOpen && in.ready() && in.readLine() == null) {
                Log.d(TAG, "Connection was closed by host");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    void setPort(int port) {
        this.port = port;
    }

    void setHost(String host) {
        this.host = host;
    }

    void setCallback(OnConnectionChangedListener c) {
        this.onConnectionChangedListener = c;
    }
}
