package com.example.asus.qmt5.House;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.example.asus.qmt5.R;


public class Guize extends DialogFragment {

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.activity_guize, null);
        Button jia1 = view.findViewById(R.id.jia1);
        jia1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),Tuifangguize.class);
                startActivity(intent);
            }
        });
        Button jia2 = view.findViewById(R.id.jia2);
        jia2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),Jifeiguize.class);
                startActivity(intent);
            }
        });
        return new AlertDialog.Builder(getContext())
                .setView(view)

                .create();
    }
}
