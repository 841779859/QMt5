package com.example.asus.qmt5.House;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.asus.qmt5.R;


public class Help extends AppCompatActivity {
private Button tv;
    private Button hkp1;
    private Button hkp2;
    private Button hkp3;
    private Button hkp4;
    private Button hkp5;
    private Button hkp6;
    private Button hkp7;
    final int CODE=0x717;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);


        ImageView tkp1 = (ImageView) findViewById(R.id.tkp1);
        ImageView tkp2 = (ImageView) findViewById(R.id.tkp2);
        ImageView tkp3 = (ImageView) findViewById(R.id.tkp3);
        tkp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Help.this, DoorActivity.class);
                startActivityForResult(intent,CODE);
            }
        });
        tkp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Help.this, WeiXianActivity.class);
                startActivityForResult(intent,CODE);
            }

        });
        tkp3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Help.this, WcActivity.class);
                startActivityForResult(intent,CODE);
            }
        });



        hkp1 = (Button) findViewById(R.id.hkp1);
        hkp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                Yajin dialog = new Yajin();
                dialog.show(fm, "");
            }
        });
        hkp2 = (Button) findViewById(R.id.hkp2);
        hkp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                Renzheng dialog = new Renzheng();
                dialog.show(fm, "");
            }
        });
        hkp3 = (Button) findViewById(R.id.hkp3);
        hkp3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                Guize dialog = new Guize();
                dialog.show(fm, "");
            }
        });
        hkp4 = (Button) findViewById(R.id.hkp4);
        hkp4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                Xieyi dialog = new Xieyi();
                dialog.show(fm, "");
            }
        });
        hkp5 = (Button) findViewById(R.id.hkp5);
        hkp5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
            FragmentManager fm = getSupportFragmentManager();
            Relation dialog = new Relation();
            dialog.show(fm, "");
            }
        });

        hkp7=(Button)findViewById(R.id.hkp7);
        hkp7.setOnClickListener(new View.OnClickListener() {
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
