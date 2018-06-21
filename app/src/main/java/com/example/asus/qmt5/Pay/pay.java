package com.example.asus.qmt5.Pay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.asus.qmt5.R;
import com.example.asus.qmt5.Scan.Handinput;
import com.example.asus.qmt5.Scan.ShowHouseMessage;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

import static com.example.asus.qmt5.Data.data.url;

public class pay extends AppCompatActivity {
String Target=url+"/servlet/ZhifuServlet";
String Tag="pay";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        Intent intent=getIntent();
       String house_ID= intent.getStringExtra("house_ID");
       String money= intent.getStringExtra("money");
        OkHttpUtils
                .post()
                .url(Target)
                .addParams("house_ID",house_ID)
                .addParams("money",money)
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
            if("success".equals(response)){
                Toast.makeText(pay.this,"支付成功",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(pay.this,"支付失败",Toast.LENGTH_SHORT).show();
            }
        }

    }
}
