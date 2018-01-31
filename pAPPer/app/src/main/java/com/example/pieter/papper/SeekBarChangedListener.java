package com.example.pieter.papper;

import android.app.Activity;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by pieter on 31-1-18.
 */

public class SeekBarChangedListener implements SeekBar.OnSeekBarChangeListener {
    private Activity activity;

    public SeekBarChangedListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        TextView volumeTextview = activity.findViewById(R.id.volume_textview);
        volumeTextview.setText("Volume: " + String.valueOf(i * 10) + "%");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}