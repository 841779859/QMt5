package com.example.asus.qmt5.LoginandRegister;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.qmt5.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

import static com.example.asus.qmt5.Data.data.url;


/**
 * Created by 唇角上扬~勾勒一抹笑 on 2018/5/6.
 */

public class Register extends AppCompatActivity {
    private ImageView back;//返回
    private TextView login;//登录
    private EditText input_phonenumber;//手机号
    private EditText input_password;//密码
    private EditText input_repassword;//确认密码
    private TextView useitem;//实用条款
    private Button register;//立即注册
    String phonenumber;
    String password;
    String repassword;
    String Tag="Register";

    private String registerTarget=url+"/xiangmu/servlet/RegisterServlet";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        back = (ImageView) findViewById(R.id.iv_return);
        login = (TextView) findViewById(R.id.tv_enter);
        input_phonenumber = (EditText) findViewById(R.id.register_phone_num);
        input_password = (EditText) findViewById(R.id.register_password);
        input_repassword = (EditText) findViewById(R.id.register_repassword);
        useitem = (TextView) findViewById(R.id.tv_termOfService);
        register = (Button) findViewById(R.id.bn_immediateRegistration);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Register.this,Login_password.class);
                startActivity(intent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Register.this,Login_password.class);
                startActivity(intent);
            }
        });

        useitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Register.this,Item.class);
                startActivity(intent);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phonenumber=input_phonenumber.getText().toString();
                password=input_password.getText().toString();
                repassword=input_repassword.getText().toString();
                // 更新写入本地缓存
// 1.实例化SharedPreferences对象（第一步）
                SharedPreferences mySharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE);
// 2. 实例化SharedPreferences.Editor对象（第二步）
                SharedPreferences.Editor editor = mySharedPreferences.edit();
// 3. 用putString的方法保存数据
                editor.putString("phonenumber", phonenumber);
                editor.putString("password", password);
// 4. 提交当前数据
                editor.apply();
                 OkHttpUtils
                        .post()
                        .url(registerTarget)
                        .addParams("phonenumber", phonenumber)
                        .addParams("password", password)
                        .build()
                        .execute(new MyCall());

            }
        });
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
            Log.e(Tag, "onResponse：complete" + response);
            if (!"".equals(phonenumber)&&!"".equals(password)&&!"".equals(repassword)){//确认密码和密码不为空
                if(!password.equals(repassword)){//两次输入密码的值不同
                    Toast.makeText(getApplicationContext(),"两次输入密码的值不同", Toast.LENGTH_SHORT).show();
                    //清空密码和确认密码
                    input_password.setText("");
                    input_repassword.setText("");
                }else{//输入密码的值相同
                    String flag;
                    Length length=new Length();
                    flag= length.flagLength(phonenumber,password);
                    //String flag=flagLength(sphonenumber,spassword);//定义标志返回值为true或false
                    Log.e(Tag,"flag="+flag);
                    if(flag.equals("true")){//长度符合判断条件
                        Log.e(Tag,"flagres="+response);
                        if (response != null) {
                            Log.e(Tag,"response"+response);

                            if ("success".equals(response)) {
                                Toast.makeText(Register.this, "注册成功！请登录", Toast.LENGTH_SHORT)
                                        .show();
                                Log.e(Tag,"response"+111111);
                                Intent it = new Intent();
                                it.setClass(getApplicationContext(), Login_password.class);
                                startActivity(it);
                                finish();
                            } else if (!"exist".equals(response)) {
                                Toast.makeText(Register.this, "该用户已存在", Toast.LENGTH_SHORT).show();
                            } else if ("fail".equals(response)) {
                                Toast.makeText(Register.this, "注册失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else if (flag.equals("false")) {//长度不符合判断条件
                        Toast.makeText(Register.this, "请确认您的手机号和密码是否符合输入规则", Toast.LENGTH_SHORT).show();
                    }

                }

            } else {
                Toast.makeText(getApplicationContext(), "手机号或密码为空", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
