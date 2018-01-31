package com.example.pieter.papper;

import io.github.controlwear.virtual.joystick.android.JoystickView;

/**
 * Created by pieter on 31-1-18.
 */

public class MoveJoystickChangedListener implements JoystickView.OnMoveListener {
    private final JoystickCombinator jsc = JoystickCombinator.getInstance();

    @Override
    public void onMove(int angle, int strength) {
        jsc.setX((float) Math.sin(Math.toRadians(angle)) * (strength / 100.f));
        jsc.setY((float) -Math.cos(Math.toRadians(angle)) * (strength / 100.f));
    }
}