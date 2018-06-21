package com.example.asus.qmt5.Scan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.asus.qmt5.Lock.Lock;
import com.example.asus.qmt5.LoginandRegister.Login_password;
import com.example.asus.qmt5.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import okhttp3.Call;
import static com.example.asus.qmt5.Data.data.url;


/**
 * Created by 唇角上扬~勾勒一抹笑 on 2018/5/8.
 */

public class Kaisuoma extends AppCompatActivity {
    String i="4";
    String Tag="kaisuoma";
    String kaisuoTarget=url+"/xiangmu/servlet/suijiServlet";
    private TextView kaisuoma;
    String houseID;
    private Button jump;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kaisuoma);
        kaisuoma=(TextView)findViewById(R.id.kaisuoma);
        jump=(Button)findViewById(R.id.lockjump) ;
        jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Kaisuoma.this, Lock.class);
                startActivity(intent);
            }
        });
        Intent intent=getIntent();
         houseID=intent.getStringExtra("codedata");
        Log.e(Tag, "houseID="+houseID);
        OkHttpUtils
                .post()
                .url(kaisuoTarget)
                .addParams("house_ID",houseID)
                .addParams("i",i)
                .build()
                .execute(new MyCall());

    }
    private class MyCall extends StringCallback {

        @Override

        public void onError(Call call, Exception e, int id)

        {


            Log.e(Tag, "onError"+e.getMessage());
            e.printStackTrace();

        }


        public void onResponse(String response, int id)
        {
            Log.e(Tag, "onResponse：complete=" + response);
            kaisuoma.setText(response);

        }

    }


}
