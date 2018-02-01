/**
 * Pieter Kronemeijer (11064838)
 *
 * This class is a listener for the joystick that handles orientation. It sends the
 * correct values to the JoystickCombinator.
 */

package com.example.pieter.papper;

import io.github.controlwear.virtual.joystick.android.JoystickView;


class OrientationJoystickChangedListener implements JoystickView.OnMoveListener {
    private final JoystickCombinator jsc = JoystickCombinator.getInstance();

    @Override
    public void onMove(int angle, int strength) {
        int theta = (angle != 0) ? 1 : -1;

        jsc.setTheta(theta * (strength / 100.f));
    }
}
