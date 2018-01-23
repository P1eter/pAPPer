package com.example.pieter.papper;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


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

        Button talk_button = view.findViewById(R.id.talk_button);
        talk_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.talk_button:
                EditText talk_et = getView().findViewById(R.id.talk_editText);
                String message = talk_et.getText().toString();
                if (!message.isEmpty()){
                    networkSender.talk(message);
                }
        }
    }
}
