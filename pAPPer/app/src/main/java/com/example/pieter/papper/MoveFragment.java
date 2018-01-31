package com.example.pieter.papper;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;


/**
 * A simple {@link Fragment} subclass.
 */
public class MoveFragment extends Fragment implements View.OnClickListener {
    private final NetworkSender networkSender = NetworkSender.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_move, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.stiffness_onoff_switch).setOnClickListener(this);
        view.findViewById(R.id.to_fullscreen_move_button).setOnClickListener(this);
        view.findViewById(R.id.autonomous_life_switch).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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
    }
}
