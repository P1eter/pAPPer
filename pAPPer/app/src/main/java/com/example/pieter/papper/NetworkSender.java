package com.example.pieter.papper;

import android.util.Log;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.PriorityQueue;

/**
 * Created by pkronemeijer on 22-1-18.
 */

public class NetworkSender implements Runnable {
    private static NetworkSender instance;
    private static final String TAG = "NetworkSender";
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private boolean connectionOpen = false;
    // TODO: make these variables parameters
    private final int portNumber = 1717;
    private final String host = "Pepper";
    private PriorityQueue<String> sendQueue = new PriorityQueue<>();

    private NetworkSender() {
        // empty constructor of singleton class
    }

    public static NetworkSender getInstance() {
        if (instance == null) {
            // call the private constructor
            instance = new NetworkSender();
        }
        return instance;
    }

    @Override
    public void run() {
        openConnection();

        while (checkConnection()) {
            if (!sendQueue.isEmpty()) {
                Log.d(TAG, "Sending: " + sendQueue.peek());
                send(sendQueue.poll());
            }
        }
    }

    private void send(String message) {
//        out.println()
//        out.println(message);
//        out.write(message);
//        out.che

        try {
            Log.d(TAG, "1");
            out.write(message);
            Log.d(TAG, "2");
        } catch (Exception e) {
            Log.d(TAG, "Cannot send message!", e);
        }
    }

    public void talk(String statement) {
        sendQueue.add("talk " + statement);
    }

    public void move(float x, float y, float theta) {
        sendQueue.add("move " + x + " " + y + " " + theta);
    }

    private void openConnection() {
        System.out.println("Creating socket to '" + host + "' on port " + portNumber);

        try {
            socket = new Socket(host, portNumber);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            connectionOpen = true;
        } catch (IOException e) {
            Log.e(TAG, "Failed to close connection", e);
            connectionOpen = false;
        }
    }

    private void closeConnection() {
        try {
            Log.i(TAG, "Host closed connection, disconnecting...");
            socket.close();
            connectionOpen = false;
        } catch (IOException e) {
            Log.e(TAG, "Failed to close socket, with error: ", e);
        }
    }

    private void getMessages() {
        try {
            String data = in.readLine();
            Log.d(TAG, data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkConnection() {
        if (connectionOpen) {
            getMessages();
        } else {
            return false;
        }
        return true;

//        try {
//            Log.d(TAG, "1");
//            if (in.readLine() == null) {
//                Log.d(TAG, "2");
//                throw new IOException();
//            }
//        } catch (IOException e) {
//            // Connection closed
//            closeConnection();
//            return false;
//        }
//        Log.d(TAG, "3");

//        try {
//            Log.d(TAG, "checkConnection1(): " + in.ready());
//            while (in.ready()) {
//                Log.d(TAG, "readline: " + in.readLine());
//            }
//            Log.d(TAG, "checkConnection2(): " + in.ready());
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        }

    }
}
