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


public class Yajin extends DialogFragment {

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.activity_yajin, null);
        Button ss2 = view.findViewById(R.id.ss2);
        ss2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),Qb.class);
                startActivity(intent);
            }
        });
        Button ss3 = view.findViewById(R.id.ss3);
        ss3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),Tk.class);
                startActivity(intent);
            }
        });
        Button ss4 = view.findViewById(R.id.ss4);
        ss4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),Th.class);
                startActivity(intent);
            }
        });
        Button ss5 = view.findViewById(R.id.ss5);
        ss5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),Cz.class);
                startActivity(intent);
            }
        });

        return new AlertDialog.Builder(getContext())
                .setView(view)

                .create();
    }
}
