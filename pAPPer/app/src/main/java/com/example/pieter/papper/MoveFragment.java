package com.example.pieter.papper;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.up_imagebutton:
                networkSender.move(X_VELOCITY, 0.f, 0.f);
                break;
            case R.id.down_imagebutton:
                networkSender.move(-X_VELOCITY, 0.f, 0.f);
                break;
            case R.id.left_imagebutton:
                networkSender.move(0.f, 0.f, -THETA_VELOCITY);
                break;
            case R.id.right_imagebutton:
                networkSender.move(0.f, 0.f, THETA_VELOCITY);
                break;
        }
    }
}
