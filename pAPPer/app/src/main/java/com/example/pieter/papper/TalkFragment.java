package com.example.pieter.papper;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class TalkFragment extends Fragment implements View.OnClickListener {
    private NetworkSender networkSender = NetworkSender.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_talk, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.talk_button).setOnClickListener(this);
        SeekBar volume_seekbar = view.findViewById(R.id.volume_seekBar);
        volume_seekbar.setOnSeekBarChangeListener(new SeekBarChangedListener());
    }

    private class SeekBarChangedListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            TextView volume_textview = getView().findViewById(R.id.volume_textview);
            volume_textview.setText("Volume: " +  String.valueOf(i));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.talk_button:
                EditText talk_et = getView().findViewById(R.id.talk_editText);
                String message = talk_et.getText().toString();

                SeekBar volume_seekbar = getView().findViewById(R.id.volume_seekBar);
                String volume = String.valueOf(volume_seekbar.getProgress());

                if (!message.isEmpty()){
                    networkSender.talk(volume + message);
                }
        }
    }
}
