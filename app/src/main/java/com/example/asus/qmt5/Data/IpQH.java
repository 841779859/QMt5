package com.example.asus.qmt5.Data;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.asus.qmt5.Map.map;
import com.example.asus.qmt5.QiDong.QiDongActivity;
import com.example.asus.qmt5.R;

public class IpQH extends AppCompatActivity {

    public EditText ip;
    public Button QD;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip_qh);
        ip=(EditText)findViewById(R.id.IP);
        QD=(Button)findViewById(R.id.button);
        ip.setText(data.url);
        QD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               data.url=ip.getText().toString();
                Intent intent = new Intent(IpQH.this, QiDongActivity.class);  //进入主界面
                startActivity(intent);  //开始跳转
                finish();
            }
        });
    }
}
