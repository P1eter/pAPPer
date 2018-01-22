package com.example.pieter.papper;

import android.util.Log;

import java.io.BufferedReader;
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
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
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
        System.out.println("Creating socket to '" + host + "' on port " + portNumber);

        try {
            socket = new Socket(host, portNumber);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            // TODO: make this a nice log statement
            System.out.println("Failed to close connection, with error: " + e);
        }

        while (true) {
            if (!sendQueue.isEmpty()) {
                send(sendQueue.poll());
            }
        }
    }

    private void send(String message) {
        try {
            out.println(message);
        } catch (Exception e) {
            Log.d("NetworkSender", "Cannot send message!", e);
        }
    }

    public void talk(String statement) {
        sendQueue.add("say " + statement);
    }

    private void close() {
        try {
            socket.close();
        } catch (IOException e) {
            // TODO: make this a nice log statement
            System.out.println("Failed to close connection, with error: " + e);
        }
    }
}
