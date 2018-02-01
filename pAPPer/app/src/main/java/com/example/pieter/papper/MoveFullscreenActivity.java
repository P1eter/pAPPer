/**
 * Pieter Kronemeijer (11064838)
 *
 * This activity has two joysticks for handling movement and rotation of the robot.
 */

package com.example.pieter.papper;

import android.content.pm.ActivityInfo;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import io.github.controlwear.virtual.joystick.android.JoystickView;


public class MoveFullscreenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_move_fullscreen);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        JoystickView moveJoystick = findViewById(R.id.movement_joystick);
        moveJoystick.setOnMoveListener(new MoveJoystickChangedListener());

        JoystickView orientationJoystick = findViewById(R.id.orientation_joystick);
        orientationJoystick.setOnMoveListener(new OrientationJoystickChangedListener());
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // joysticks can only be used in landscape mode
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        // this resets the flags needed for the activity to be fullscreen once they have been
        // cleared by a focus change event
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    public void closeFullScreen(@SuppressWarnings("unused") View view) {
        finish();
    }
}
