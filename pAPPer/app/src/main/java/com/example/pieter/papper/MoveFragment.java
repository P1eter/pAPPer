package com.example.pieter.papper;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.ToggleButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class MoveFragment extends Fragment implements View.OnClickListener {
    private NetworkSender networkSender = NetworkSender.getInstance();
    private float X_VELOCITY = 0.5f;
    private float Y_VELOCITY = 0.5f;
    private float THETA_VELOCITY = 0.5f;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_move, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        view.findViewById(R.id.up_imagebutton).setOnClickListener(this);
//        view.findViewById(R.id.down_imagebutton).setOnClickListener(this);
//        view.findViewById(R.id.left_imagebutton).setOnClickListener(this);
//        view.findViewById(R.id.right_imagebutton).setOnClickListener(this);
//        view.findViewById(R.id.stop_button).setOnClickListener(this);
        view.findViewById(R.id.stiffness_onoff_switch).setOnClickListener(this);
        view.findViewById(R.id.to_fullscreen_move_button).setOnClickListener(this);
        view.findViewById(R.id.autonomous_life_switch).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        float x = 0.f;
        float y = 0.f;
        float theta = 0.f;
        switch (view.getId()) {
//            case R.id.up_imagebutton:
//                x = X_VELOCITY;
////                networkSender.move(X_VELOCITY, 0.f, 0.f);
//                break;
//            case R.id.down_imagebutton:
//                x = -X_VELOCITY;
////                networkSender.move(-X_VELOCITY, 0.f, 0.f);
//                break;
//            case R.id.left_imagebutton:
//                theta = -THETA_VELOCITY;
////                networkSender.move(0.f, 0.f, -THETA_VELOCITY);
//                break;
//            case R.id.right_imagebutton:
//                theta = THETA_VELOCITY;
////                networkSender.move(0.f, 0.f, THETA_VELOCITY);
//                break;
//            case R.id.stop_button:
//                break;
            case R.id.stiffness_onoff_switch:
                Switch tb = view.findViewById(R.id.stiffness_onoff_switch);
                networkSender.wakeUp(tb.isChecked());
                return;
            case R.id.to_fullscreen_move_button:
                Intent intent = new Intent(getActivity(), MoveFullscreenActivity.class);
                startActivity(intent);
                break;
            case R.id.autonomous_life_switch:
                Switch autonomousLifeSwitch = view.findViewById(R.id.autonomous_life_switch);
                networkSender.autonomousLife(autonomousLifeSwitch.isChecked());
        }

//        networkSender.move(x, y, theta);
    }
}
