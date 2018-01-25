package com.example.pieter.papper;

import android.util.Log;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.PriorityQueue;

import static java.lang.Thread.sleep;

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
    private int port;
    private String host;
    private PriorityQueue<String> sendQueue = new PriorityQueue<>();

    private NetworkSender() {
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
        if (!openConnection()) {
            return;
        }

        System.out.println("NetworkSender: run()");
        System.out.println(socket.getChannel());
        System.out.println(socket.getInetAddress());
        System.out.println(socket.getLocalAddress());
        System.out.println(socket.getLocalPort());
        System.out.println(socket.getLocalSocketAddress());
        System.out.println(socket.getPort());
        System.out.println(socket.getRemoteSocketAddress());

        while (checkConnection()) {
            if (!sendQueue.isEmpty()) {
                Log.d(TAG, "Sending: " + sendQueue.peek());
                send(sendQueue.poll());

                try {
                    sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        if (connectionOpen) {
            closeConnection();
        }
    }

    private void send(String message) {
        Log.d(TAG, "sending... \"" + message + "\"");
        try {
            Log.d(TAG, "1");
            out.write(message + "\n");
            out.flush();
            Log.d(TAG, "2");
        } catch (NullPointerException e) {
            Log.d(TAG, "Cannot send message!", e);
        }
    }

    public void talk(String statement) {
        sendQueue.add("talk " + statement);
    }

    public void move(float x, float y, float theta) {
        sendQueue.add("move " + x + " " + y + " " + theta);
    }

    public void wakeUp(boolean wakeOrSleep) {
        sendQueue.add("wake " + (wakeOrSleep ? 1 : 0));
    }

    public void dance(String dance) {
        sendQueue.add("danc " + dance);
    }

    public boolean openConnection() {
        System.out.println("Creating socket to '" + host + "' on port " + port);

        if (connectionOpen && !closeConnection()) {
//            connectionOpen = true;
            return false;
        }

        try {
            socket = new Socket(host, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            sendQueue.clear();
            connectionOpen = true;
        } catch (IOException e) {
            // TODO: make toast
            Log.e(TAG, "Failed to open connection", e);
            return false;
        }
        Log.d(TAG, "Opened connection");
        return true;
    }

    public boolean closeConnection() {
        try {
            socket.close();
            Log.i(TAG, "Host closed connection, disconnecting...");
            sendQueue.clear();

            socket = null;
            in = null;
            out = null;
            connectionOpen = false;
            return true;
        } catch (IOException e) {
            Log.e(TAG, "Failed to close socket", e);
//            connectionOpen = true;
            return false;
        }
    }

    private void getMessages() {
        try {
//            Log.d(TAG, "Waiting for response...");
//            String data = in.readLine();
//            Log.d(TAG, data);
            if (in.ready() && in.readLine() == null) {
                Log.d(TAG, "response: null");
//                connectionOpen = false;
            }
        } catch (IOException e) {
//            connectionOpen = false;
            e.printStackTrace();
        } catch (NullPointerException e) {
//            connectionOpen = false;
            e.printStackTrace();
        }
    }

    private boolean checkConnection() {
        try {
            if (connectionOpen && in.ready() && in.readLine() == null) {
                Log.d(TAG, "response: null");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;

//        catch (IOException e) {
//            e.printStackTrace();
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//        }

//        if (connectionOpen) {
////            getMessages();
//            try {
//                if (in.ready() && in.readLine() == null) {
//                    Log.d(TAG, "response: null");
//                    connectionOpen = false;
//                }
//            } catch (IOException e) {
//                connectionOpen = false;
//                e.printStackTrace();
//            } catch (NullPointerException e) {
//                connectionOpen = false;
//                e.printStackTrace();
//            }
//        } else {
//            return false;
//        }
//        return true;

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

    public void setPort(int port) {
        this.port = port;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public boolean isRunning() {
        return connectionOpen;
    }
}
