package com.example.asus.qmt5.House;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.asus.qmt5.Pay.pay;
import com.example.asus.qmt5.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import static com.example.asus.qmt5.Data.data.url;

public class TuiFang extends AppCompatActivity {
    TextView shuchu_time;
    TextView money;
    String Tag = "TuiFang";
    String Target = url+ "/timeServlet";
    private Button  zhifu;
    String house_ID;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tui_fang);
        zhifu=(Button)findViewById(R.id.zhifu);
        Button dacha_back = (Button) findViewById(R.id.dacha_back);
        dacha_back.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    });

    Intent intent=getIntent();
    String process_time=intent.getStringExtra("jishiqi1");
        Log.e(Tag, process_time);
        shuchu_time=findViewById(R.id.shuchu_time);
        shuchu_time.setText(process_time);



        //String process_time=getIntent().getStringExtra("jishiqi1");
        //Log.e(Tag, process_time);

        house_ID=getIntent().getStringExtra("house_ID");
        Log.e(Tag, house_ID);


        OkHttpUtils
                .post()
                .url(Target)
                //.addParams("process_time",process_time)
                .addParams("house_ID",house_ID)
                .build()
                .execute(new MyCall());//回调方法

            }

        class MyCall extends StringCallback {

            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                Log.d(Tag, "e=" + e.getMessage());
                e.printStackTrace();//打印
            }

            @Override
            public void onResponse(final String response, int id) {//服务器返回的结果
                double price= Double.parseDouble(response);
                Log.e(Tag, response);
                /* double time=Double.parseDouble(response);
                Log.e(Tag,response);
                double sum=price*time;
                Log.e(Tag, String.valueOf(sum));*/
                money=findViewById(R.id.money);
                money.setText(response);

                zhifu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(TuiFang.this,pay.class);
                        intent.putExtra("money",response);
                        intent.putExtra("house_ID",house_ID);
                        startActivity(intent);
                    }
                });

            }
}


    }


