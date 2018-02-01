/**
 * Pieter Kronemeijer (11064838)
 *
 * This class is a listener for the joystick that handles movement. It sends the
 * correct values to the JoystickCombinator.
 */

package com.example.pieter.papper;

import io.github.controlwear.virtual.joystick.android.JoystickView;


public class MoveJoystickChangedListener implements JoystickView.OnMoveListener {
    private final JoystickCombinator jsc = JoystickCombinator.getInstance();

    @Override
    public void onMove(int angle, int strength) {
        jsc.setX((float) Math.sin(Math.toRadians(angle)) * (strength / 100.f));
        jsc.setY((float) -Math.cos(Math.toRadians(angle)) * (strength / 100.f));
    }
}
