package com.example.pieter.papper;

import io.github.controlwear.virtual.joystick.android.JoystickView;

/**
 * Created by pieter on 31-1-18.
 */

public class OrientationJoystickChangedListener implements JoystickView.OnMoveListener {
    private final JoystickCombinator jsc = JoystickCombinator.getInstance();

    @Override
    public void onMove(int angle, int strength) {
        int theta = angle != 0 ? 1 : -1;

        jsc.setTheta(theta * (strength / 100.f));
    }
}
