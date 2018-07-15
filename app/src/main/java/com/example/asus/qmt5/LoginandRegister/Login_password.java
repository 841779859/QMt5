package com.example.asus.qmt5.LoginandRegister;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.qmt5.LoginandRegister.bean.LoginInfo;

import com.example.asus.qmt5.Map.map;
import com.example.asus.qmt5.R;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.MediaType;

import static com.example.asus.qmt5.Data.data.url;


/**
 * Created by 唇角上扬~勾勒一抹笑 on 2018/5/6.
 */

public class Login_password extends AppCompatActivity {
    private ImageView back;//返回
    private TextView register;//注册
    private EditText inputphonenumber;//手机号
    String phonenumber;
    private Button usecode;//验证码登录
    private EditText inputpassword;//密码
    String password;
    private TextView useitem;//使用条款
    private Button login;//登录
    private CheckBox remember;//记住密码
    private String loginTarget=url+"/servlet/LoginServlet";
    String Tag="Login_password";
    String phoneID;

    String useraddress ;
    int login_status;
    SharedPreferences sp,spf;
    SharedPreferences.Editor editor;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       spf=PreferenceManager.getDefaultSharedPreferences(this);
        editor=spf.edit();//初始化
        if(spf.getBoolean("main",true)){
         Intent intent=new Intent(Login_password.this,map.class);
         startActivity(intent);
         Login_password.this.finish();
        }
        setContentView(R.layout.login_password);
            back = (ImageView) findViewById(R.id.iv_return1);
            register = (TextView) findViewById(R.id.password_register);
            inputphonenumber = (EditText) findViewById(R.id.password_usernumber);
            phonenumber = inputphonenumber.getText().toString();
            usecode = (Button) findViewById(R.id.password_sms_code);
            inputpassword = (EditText) findViewById(R.id.login_password);
            password = inputpassword.getText().toString();
            useitem = (TextView) findViewById(R.id.tv_termOfService3);
            login = (Button) findViewById(R.id.password_common_login);
            remember = (CheckBox) findViewById(R.id.remember);
            sp = this.getSharedPreferences("userInfo",  MODE_PRIVATE);

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            //判断记住密码多选框的状态
            if (sp.getBoolean("ISCHECK", false)) {
                //设置默认是记录密码状态 
                remember.setChecked(true);
                inputphonenumber.setText(sp.getString("PHONENUMBER", ""));
                inputpassword.setText(sp.getString("PASSWORD", ""));
            }

            remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (remember.isChecked()) {
                        System.out.println("记住密码已选中");
                        sp.edit().putBoolean("ISCHECK", true).commit();
                    } else {
                        System.out.println("记住密码没有选中");
                        sp.edit().putBoolean("ISCHECK", false).commit();
                    }
                }
            });
            usecode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Login_password.this, Login_phonenumber.class);
                    startActivity(intent);
                }
            });
            useitem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Login_password.this, Item.class);
                    startActivity(intent);
                }
            });

            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Login_password.this, Register.class);
                    startActivity(intent);
                }
            });
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    phonenumber = inputphonenumber.getText().toString();
                    Log.e(Tag, "phonenumber=" + phonenumber);
                    password = inputpassword.getText().toString();


                    if (remember.isChecked()) {
                        //记住用户名、密码、  
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("PHONENUMBER", phonenumber);
                        editor.putString("PASSWORD", password);
                        editor.commit();
                    }


                    //获取phoneid
                    phoneID = Secure.getString(getApplicationContext().getContentResolver(),//getContext().getContentResolver(),
                            Secure.ANDROID_ID);//getContext().getContentResolver()返回的是ContentResolver ，ContentResolver负责获取ContentProvider提供的数据
                    //获取登陆时间
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date(System.currentTimeMillis());
                    String lastdatetime = format.format(date);

                    //获取手机IP
                    //获取wifi服务
                    WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    //判断wifi是否开启
                    if (!wifiManager.isWifiEnabled()) {

                        wifiManager.setWifiEnabled(true);
                    }
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    int ipAddress = wifiInfo.getIpAddress();
                    useraddress = intToIp(ipAddress);
                    login_status = 1;
                    if ("".equals(phonenumber) || "".equals(password)) {
                        Toast.makeText(Login_password.this, "手机号或密码为空！", Toast.LENGTH_SHORT).show();

                    }
                    Log.e(Tag, "password=" + password);
                    Log.e(Tag, "phonenumber=" + phonenumber);
                    Log.e(Tag, "password=" + password);
                    String json=new Gson().toJson(new LoginInfo(phoneID,phonenumber,password,useraddress,lastdatetime,login_status));
                    Log.e(Tag, "json=" + json);

                    OkHttpUtils
                            .postString()
                            .url(loginTarget)
                            .mediaType(MediaType.parse("application/json; charset=utf-8"))
                            .content(json)
                            .build()
                            .execute(new MyCall());



                }
            });


    }
    private String intToIp(int i) {
        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
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
            Log.e(Tag, "password="+password);
            Log.e(Tag, "phonenumber="+phonenumber);

            if(!"".equals(phonenumber)&&!"".equals(password)){
                if ( "success".equals(response)) {
                    Intent it = new Intent();
                    it.setClass(getApplicationContext(),Shimingrenzheng.class);
                    editor.putBoolean("main",true);
                    editor.apply();
                    Log.e(Tag,"editor="+editor);

                    startActivity(it);
                }else{
                    Toast.makeText(Login_password.this,"登录失败", Toast.LENGTH_SHORT).show();
                }
            }

        }

    }
}
