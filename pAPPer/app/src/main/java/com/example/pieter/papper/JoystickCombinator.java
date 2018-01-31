/**
 * Pieter Kronemeijer (11064838)
 *
 * This singleton class gets movement data from the left joystick and orientation data
 * from the right joystick, and combines both into one command for the robot.
 */

package com.example.pieter.papper;


public class JoystickCombinator {
    private NetworkSender networkSender = NetworkSender.getInstance();
    private static JoystickCombinator instance;
    private float x = 0.f;
    private float y = 0.f;
    private float theta = 0.f;

    private JoystickCombinator() {
        // empty private constructor for singleton class
    }

    public static JoystickCombinator getInstance() {
        if (instance == null) {
            instance = new JoystickCombinator();
        }
        return instance;
    }

    /**
     * Sets the speed in the x direction (forward) and sends value to the robot.
     */
    public void setX(float x) {
        if (this.x != x) {
            this.x = x;
            this.update();
        }
    }

    /**
     * Sets the speed in the y direction (left) and sends value to the robot.
     */
    public void setY(float y) {
        if (this.y != y) {
            this.y = y;
            this.update();
        }
    }

    /**
     * Sets the turning speed (counter clockwise) and sends value to the robot.
     */
    public void setTheta(float theta) {
        if (this.theta != theta) {
            this.theta = theta;
            this.update();
        }
    }

    /**
     * Send a move command to the robot.
     */
    private void update() {
        networkSender.move(this.x, this.y, this.theta);
    }
}

