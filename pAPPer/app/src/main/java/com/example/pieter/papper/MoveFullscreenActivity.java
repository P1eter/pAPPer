package com.example.pieter.papper;

import android.content.pm.ActivityInfo;
import android.net.Network;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class MoveFullscreenActivity extends AppCompatActivity {
    private NetworkSender networkSender = NetworkSender.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_move_fullscreen);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        JoystickView move_joystick = findViewById(R.id.movement_joystick);
        JoystickView orientation_joystick = findViewById(R.id.orientation_joystick);

        move_joystick.setOnMoveListener(new JoystickView.OnMoveListener() {
            private JoystickCombinator jsc = JoystickCombinator.getInstance();

            @Override
            public void onMove(int angle, int strength) {
                jsc.setX((float) Math.sin(Math.toRadians(angle)) * (strength / 100.f));
                jsc.setY((float) -Math.cos(Math.toRadians(angle)) * (strength / 100.f));
            }
        });

        orientation_joystick.setOnMoveListener(new JoystickView.OnMoveListener() {
            private JoystickCombinator jsc = JoystickCombinator.getInstance();

            @Override
            public void onMove(int angle, int strength) {
                int theta = angle != 0 ? 1 : -1;

                jsc.setTheta(theta * (strength / 100.f));
            }
        });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
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

    public void closeFullScreen(View view) {
        finish();
    }
}
