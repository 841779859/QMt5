package com.example.asus.qmt5.Scan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.asus.qmt5.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kaisuoma);
        kaisuoma=(TextView)findViewById(R.id.kaisuoma);
        Intent intent=getIntent();
        String houseID=intent.getStringExtra("codedata");
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
