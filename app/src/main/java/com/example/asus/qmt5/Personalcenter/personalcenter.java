package com.example.asus.qmt5.Personalcenter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.asus.qmt5.LoginandRegister.Login_password;
import com.example.asus.qmt5.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

import static com.example.asus.qmt5.Data.data.url;

public class personalcenter extends AppCompatActivity {
private Button out;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String phonenumber;
    String Tag="personalcenter";
    String Target=url+"/servlet/OutServlet";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalcenter);
        out=(Button)findViewById(R.id.login_out);
        SharedPreferences pref=getSharedPreferences("userinfo",MODE_PRIVATE);
        phonenumber=pref.getString("phonenumber","");
        Log.e(Tag,"phone s="+phonenumber);


        out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resetsp();
                Intent intent=new Intent(personalcenter.this, Login_password.class);
                startActivity(intent);
                personalcenter.this.finish();
            }
        });



    }

    private void resetsp() {
        OkHttpUtils.post()
                .url(Target)
                .addParams("phonenumber",phonenumber)
                .build()
                .execute(new MyCall());
            sp= PreferenceManager.getDefaultSharedPreferences(this);
            editor=sp.edit();
            editor.putBoolean("main",false);
            editor.apply();
    }

    private class MyCall extends StringCallback {

        @Override

        public void onError(Call call, Exception e, int id)

        {

            Log.e(Tag, "onError"+e.getMessage());
            e.printStackTrace();


        }


        public void onResponse(String response, int id)
        { Log.e(Tag,"onresponse="+response);
          if("success".equals(response)){
              Toast.makeText(personalcenter.this,"成功退出",Toast.LENGTH_SHORT).show();
          }
        }
    }
}
