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


public class Renzheng extends DialogFragment {

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.activity_renzheng, null);
        Button sa1 = view.findViewById(R.id.sa1);
        sa1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),Smrz.class);
                startActivity(intent);
            }
        });
        Button sa2 = view.findViewById(R.id.sa2);
        sa2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),Ckrzzt.class);
                startActivity(intent);
            }
        });
        Button sa3 = view.findViewById(R.id.sa3);
        sa3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),Rzcs.class);
                startActivity(intent);
            }
        });
        Button sa4 = view.findViewById(R.id.sa4);
        sa4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),Rzshbgyy.class);
                startActivity(intent);
            }
        });

        return new AlertDialog.Builder(getContext())
                .setView(view)

                .create();
    }
}
