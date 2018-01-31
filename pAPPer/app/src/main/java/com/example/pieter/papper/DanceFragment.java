package com.example.pieter.papper;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 */
public class DanceFragment extends Fragment implements View.OnClickListener {
    private final NetworkSender networkSender = NetworkSender.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dance, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.dance_headbang_button).setOnClickListener(this);
        view.findViewById(R.id.dance_vacuum_button).setOnClickListener(this);
        view.findViewById(R.id.dance_picture_button).setOnClickListener(this);
        view.findViewById(R.id.dance_sax_button).setOnClickListener(this);
        view.findViewById(R.id.dance_guitar_button).setOnClickListener(this);
        view.findViewById(R.id.dance_football_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dance_vacuum_button:
                networkSender.dance("vacuum");
                break;
            case R.id.dance_headbang_button:
                networkSender.dance("headbang");
                break;
            case R.id.dance_picture_button:
                networkSender.dance("take_picture");
                break;
            case R.id.dance_sax_button:
                networkSender.dance("saxophone");
                break;
            case R.id.dance_guitar_button:
                networkSender.dance("guitar");
                break;
            case R.id.dance_football_button:
                networkSender.dance("football");
                break;
        }
    }
}
