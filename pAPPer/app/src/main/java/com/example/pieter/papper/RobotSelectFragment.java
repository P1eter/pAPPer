package com.example.pieter.papper;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.lang.reflect.Array;
import java.util.ArrayList;


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

        Bundle bundle = getArguments();
        view.findViewById(R.id.connect_button).setOnClickListener(this);
        view.findViewById(R.id.disconnect_button).setOnClickListener(this);

        Spinner robotSpinner = view.findViewById(R.id.robot_select_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, bundle.getStringArrayList("robots"));

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        robotSpinner.setAdapter(adapter);
    }


    @Override
    public void onClick(View view) {
        NetworkSender networkSender = NetworkSender.getInstance();

        if (networkSender.isRunning()) {
            networkSender.closeConnection();
        }

        switch (view.getId()) {
            case R.id.connect_button:
                networkSender.setHost("Pepper"); //192.168.0.103 = laptop
                networkSender.setPort(1717);
                new Thread(networkSender).start();
                break;
            case R.id.disconnect_button:
                // disconnection already happened
                break;
        }

        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();



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
