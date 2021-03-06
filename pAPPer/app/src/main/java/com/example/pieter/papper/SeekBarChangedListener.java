/**
 * Pieter Kronemeijer (11064838)
 *
 * This class is a listener for the volume seekbar. It sets the text in the TextView
 * that shows the current volume setting.
 */

package com.example.pieter.papper;

import android.app.Activity;
import android.widget.SeekBar;
import android.widget.TextView;


class SeekBarChangedListener implements SeekBar.OnSeekBarChangeListener {
    private final Activity activity;

    SeekBarChangedListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        TextView volumeTextview = activity.findViewById(R.id.volume_textview);
        volumeTextview.setText(activity.getString(R.string.volume_textview_text, i * 10, "%"));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}