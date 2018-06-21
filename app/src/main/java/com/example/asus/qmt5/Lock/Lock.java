package com.example.asus.qmt5.Lock;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.asus.qmt5.House.Hours;
import com.example.asus.qmt5.Map.map;
import com.example.asus.qmt5.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

import static com.example.asus.qmt5.Data.data.url;

public class Lock extends AppCompatActivity {
    private EditText et;
    private Button sure;

private String house_ID="00000001";
String Target=url+"/servlet/YanzhengServlet";
String Tag="Lock";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lock);
        et=(EditText)findViewById(R.id.ed);
        sure=(Button)findViewById(R.id.sure);

        Log.e(Tag,"code="+ et.getText().toString());
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkHttpUtils
                        .post()
                        .url(Target)
                        .addParams("house_ID",house_ID)
                        .addParams("code", et.getText().toString())
                        .build()
                        .execute(new MyCall());
                Log.e(Tag,"code="+ et.getText().toString());
            }
        });

    }
    private class MyCall extends StringCallback {

        @Override

        public void onError(Call call, Exception e, int id)

        {

            Log.e(Tag, "onError "+e.getMessage());
            e.printStackTrace();


        }

        public void onResponse(String response, int id)
        {
            Log.e(Tag, "onResponse：complete=" + response);
            if("same".equals(response)){
                Toast.makeText(Lock.this,"您已打开门锁，现在开始计费", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(Lock.this, Hours.class);
                intent.putExtra("house_ID",house_ID);
                startActivity(intent);
            }else if("different".equals(response)){
                Toast.makeText(Lock.this,"开锁码错误", Toast.LENGTH_SHORT).show();
            }else if("chaoshi".equals(response)){
                Toast.makeText(Lock.this,"开锁码超时，请重新获取", Toast.LENGTH_SHORT).show();
            }


        }

    }
}
