package com.example.asus.qmt5.House;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.asus.qmt5.R;


public class Th extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_th);
        Button jia13=(Button)findViewById(R.id.jia13);
        jia13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Button jia6=(Button)findViewById(R.id.jia6);
        jia6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View phone) {
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_DIAL);   //android.intent.action.DIAL
                intent.setData(Uri.parse("tel:18270849776"));
                startActivity(intent);
            }
        });
    }
}
