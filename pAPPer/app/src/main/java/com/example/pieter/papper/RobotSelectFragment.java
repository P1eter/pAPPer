package com.example.pieter.papper;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class RobotSelectFragment extends DialogFragment implements View.OnClickListener {
    String TAG = "RobotSelectFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_robot_select, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.connect_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        NetworkSender networkSender = NetworkSender.getInstance();

        if (networkSender.isRunning()) {
            networkSender.closeConnection();
        }

        networkSender.setHost("Pepper"); //192.168.0.103 = laptop
        networkSender.setPort(1717);
        new Thread(networkSender).start();

//        if (!networkSender.isRunning()) {
//            Log.d(TAG, "starting new thread");
//            new Thread(networkSender).start();
//        } else {
//            Log.d(TAG, "opening new connection");
//            networkSender.closeConnection();
//            networkSender.run();
////            networkSender.openConnection();
//        }


    }
}
