/**
 * Pieter Kronemeijer (11064838)
 *
 * This is the tab that handles the talking of the robot.
 */

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


public class TalkFragment extends Fragment implements View.OnClickListener {
    private final NetworkSender networkSender = NetworkSender.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_talk, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.talk_button).setOnClickListener(this);

        // set listener on SeekBar that sets the correct volume value in the TextView
        SeekBar volumeSeekBar = view.findViewById(R.id.volume_seekBar);
        volumeSeekBar.setOnSeekBarChangeListener(new SeekBarChangedListener(getActivity()));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.talk_button:
                handleTalk();
        }
    }

    /**
     * This function extracts the message, the volume and the 'Animated Speech' option from the
     * correct resources and builds a server readable string from these values.
     */
    private void handleTalk() {
        EditText talkEditText = getView().findViewById(R.id.talk_editText);
        String message = talkEditText.getText().toString();

        SeekBar volumeSeekBar = getView().findViewById(R.id.volume_seekBar);
        String volume = String.valueOf(volumeSeekBar.getProgress());

        Switch animatedSpeechSwitch = getView().findViewById(R.id.animated_speech_switch);
        int animatedSpeech = animatedSpeechSwitch.isChecked() ? 1 : 0;

        // don't send if message is empty
        if (!message.isEmpty()){
            networkSender.talk(volume + " " + animatedSpeech + " " + message);
        }
    }
}
