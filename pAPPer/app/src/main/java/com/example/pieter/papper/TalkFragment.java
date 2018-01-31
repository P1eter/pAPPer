package com.example.pieter.papper;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;


/**
 * A simple {@link Fragment} subclass.
 */
public class TalkFragment extends Fragment implements View.OnClickListener {
    private final NetworkSender networkSender = NetworkSender.getInstance();

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

        SeekBar volumeSeekbar = view.findViewById(R.id.volume_seekBar);
        volumeSeekbar.setOnSeekBarChangeListener(new SeekBarChangedListener(getActivity()));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.talk_button:
                EditText talkEditText = getView().findViewById(R.id.talk_editText);
                String message = talkEditText.getText().toString();

                SeekBar volumeSeekbar = getView().findViewById(R.id.volume_seekBar);
                String volume = String.valueOf(volumeSeekbar.getProgress());

                Switch animatedSpeechSwitch = getView().findViewById(R.id.animated_speech_switch);
                int animatedSpeech = animatedSpeechSwitch.isChecked() ? 1 : 0;

                if (!message.isEmpty()){
                    networkSender.talk(volume + " " + animatedSpeech + " " + message);
                }
        }
    }
}
