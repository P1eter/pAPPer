/**
 * Pieter Kronemeijer (11064838)
 *
 * This fragment shows the user a list of available robots to connect to and
 * starts the connection process.
 */

package com.example.pieter.papper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;


public class RobotSelectFragment extends DialogFragment implements View.OnClickListener {
    private static final int PORT = 1717;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_robot_select, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.connect_button).setOnClickListener(this);
        view.findViewById(R.id.disconnect_button).setOnClickListener(this);

        Bundle bundle = getArguments();
        Spinner robotSpinner = view.findViewById(R.id.robot_select_spinner);

        ArrayList<String> robotList = bundle.getStringArrayList("robots");
        if (robotList == null) {
            robotList = new ArrayList<>();
//            robotList.add(getString(R.string.default_robot_spinner_entry));
            robotList.add("Pepper");
        }

        // give the adapter a list of all available services to connect to
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, robotList);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        robotSpinner.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        NetworkSender networkSender = NetworkSender.getInstance();

        switch (view.getId()) {
            case R.id.connect_button:
                Spinner robotSpinner = getView().findViewById(R.id.robot_select_spinner);

                // default value
                String host = "Pepper";

                if (!robotSpinner.getSelectedItem().equals(getString(R.string.default_robot_spinner_entry))) {
                    host = robotSpinner.getSelectedItem().toString();
                }

                networkSender.setHost(host);
                networkSender.setPort(PORT);
                new Thread(networkSender).start();
                break;
            case R.id.disconnect_button:
                networkSender.closeConnection();
                break;
        }

        // close this dialog fragment
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }


}
