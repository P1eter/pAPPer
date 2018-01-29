package com.example.pieter.papper;

import android.util.Log;

/**
 * Created by pieter on 29-1-18.
 */

public class JoystickCombinator {
    private final String TAG = "JoystickCombinator";
    private NetworkSender networkSender = NetworkSender.getInstance();
    private static JoystickCombinator instance;
    private float x = 0.f;
    private float y = 0.f;
    private float theta = 0.f;

    private JoystickCombinator() {
    }

    public static JoystickCombinator getInstance() {
        if (instance == null) {
            instance = new JoystickCombinator();
        }
        return instance;
    }

    public void setX(float x) {
        if (this.x != x) {
            this.x = x;
            this.update();
        }
    }

    public void setY(float y) {
        if (this.y != y) {
            this.y = y;
            this.update();
        }
    }

    public void setTheta(float theta) {
        if (this.theta != theta) {
            this.theta = theta;
            this.update();
        }
    }

    private void update() {
//        Log.d(TAG, "update(): " + x + " " + y + " " + theta);
        networkSender.move(this.x, this.y, this.theta);
    }
}

