package com.example.asus.qmt5.LoginandRegister;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings.Secure;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.qmt5.BasicView.MainActivity;
import com.example.asus.qmt5.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import okhttp3.Call;

import static com.example.asus.qmt5.Data.data.url;

/**
 * Created by 唇角上扬~勾勒一抹笑 on 2018/5/6.
 */

public class Login_phonenumber extends AppCompatActivity {
    private ImageView back;//返回
    private TextView register;//注册
    private EditText inputphonenumber;//手机号
    private String phonenumber;
    private Button getcode;
    private EditText inputcode;//短信验证码
    private TextView useitem;//使用条款
    private Button login;//登录
    private ProgressDialog dialog;//进度对话框
    String password="";
    int i=30;// 获取验证码的时间
    private String loginTarget=url+"/xiangmu/servlet/LoginphoneServlet";
    String Tag="Login_phonenumber";
    String phoneID;
    String useraddress ;
    String login_status;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_phonenumber);
        back=(ImageView)findViewById(R.id.phonenumber_return);
        register=(TextView)findViewById(R.id.password_register1);
        inputphonenumber=(EditText)findViewById(R.id.input_phonenumber);
        getcode=(Button)findViewById(R.id.get_sms_code);
        inputcode=(EditText)findViewById(R.id.input_sms_code);
        useitem=(TextView)findViewById(R.id.tv_termOfService1);
        login=(Button)findViewById(R.id.phonenumber_login);

        EventHandler eventHandler=new EventHandler(){//创建一个线程
            public void afterEvent(int event, int result,Object data){
                Message msg=new Message();
                msg.arg1=event;
                msg.arg2=result;
                msg.obj=data;
                handler.sendMessage(msg);
            }
        };

        SMSSDK.registerEventHandler(eventHandler); //注册短信回调监听
        getcode.setOnClickListener(new View.OnClickListener() {//获取验证码
            @Override
            public void onClick(View v) {
                phonenumber = inputphonenumber.getText().toString().trim();
                //发送短信，传入国家号和电话---使用SMSSDK核心类之前一定要在MyApplication中初始化，否侧不能使用
                if (TextUtils.isEmpty(phonenumber)) {
                    Toast.makeText(Login_phonenumber.this, "号码不能为空！", Toast.LENGTH_SHORT).show();
                }
                else if(!judgePhoneNums(phonenumber)){
                    Toast.makeText(Login_phonenumber.this, "号码格式不正确！", Toast.LENGTH_SHORT).show();

                }else {
                    SMSSDK.getVerificationCode("+86", phonenumber);
                    Toast.makeText(Login_phonenumber.this, "发送成功:" + phonenumber, Toast.LENGTH_SHORT).show();
                }
                ////////////////
                getcode.setClickable(false);
                getcode.setText("重新发送("+i+")");
                new Thread(new Runnable(){public void run(){
                    for(;i>0;i--){
                        handler.sendEmptyMessage(-9);
                        if(i<=0){
                            break;
                        }
                        try{
                            Thread.sleep(1000);
                        }catch(InterruptedException e){
                            e.printStackTrace();
                        }
                    }
                    handler.sendEmptyMessage(-8);
                }
                }).start();
                //////////////////
            }
        });

        //为返回设置点击事件
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login_phonenumber.this,Login_password.class);
                startActivity(intent);
            }
        });
        //为注册设置点击事件
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login_phonenumber.this,Register.class);
                startActivity(intent);
            }
        });
        //为条款设置点击事件
        useitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login_phonenumber.this,Item.class);
                startActivity(intent);
            }
        });

        //为登录设置点击事件
        login.setOnClickListener(new View.OnClickListener() {//向服务器提交验证码，在监听回调中判断是否通过验证
            @Override
            public void onClick(View v) {
                String code = inputcode.getText().toString();
                if (!TextUtils.isEmpty(code)) {
                    dialog = ProgressDialog.show(Login_phonenumber.this, null, "正在验证...", false, true);
                    //提交短信验证码
                      SMSSDK.submitVerificationCode("+86", phonenumber, code);//国家号，手机号码，验证码

                    // Toast.makeText(Login_phonenumber.this, "提交了注册信息:" + phonenumber, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Login_phonenumber.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                }
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
            String code = inputcode.getText().toString();
            Log.e(Tag, "onResponse：complete=" + response);

            Log.e(Tag, "phonenumber="+phonenumber);

            if(!"".equals(phonenumber)){

                //  if(response!=null) {
                if ( "success".equals(response)) {
                    Intent intent=new Intent(Login_phonenumber.this,MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(Login_phonenumber.this,"你还未进行注册", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    /**
     * 短信验证的回调监听
     */
    //////////////////////////
    Handler handler=new Handler(){
        public void handleMessage(Message msg){
            if(msg.what==-9){
                getcode.setText("重新发送("+i+")");
            }else if(msg.what==-8){
                getcode.setText("获取验证码");
                getcode.setClickable(true);
                i=30;
            }else{
                int event=msg.arg1;
                int result=msg.arg2;
                Object data=msg.obj;
                Log.e("event","event="+event);
                ///////////////
                if (result == SMSSDK.RESULT_COMPLETE) { //回调完成
                    //提交验证码成功,如果验证成功会在data里返回数据。data数据类型为HashMap<number,code>
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        Log.e("TAG", "提交验证码成功" + data.toString());
                        HashMap<String, Object> mData = (HashMap<String, Object>) data;
                        String country = (String) mData.get("country");//返回的国家编号
                        String phone = (String) mData.get("phone");//返回用户注册的手机号

                        Log.e("TAG", country + "====" + phone);

                        if (phone.equals(phonenumber)) {
                            runOnUiThread(new Runnable() {//更改ui的操作要放在主线程，实际可以发送hander
                                @Override
                                public void run() {
                                    //获取phoneid
                                    phoneID= Secure.getString( getApplicationContext().getContentResolver(),//getContext().getContentResolver(),
                                            Secure.ANDROID_ID);
                                    //获取登陆时间

                                    SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    Date date=new Date(System.currentTimeMillis());
                                    String lastdatetime=format.format(date);
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
                                    login_status="1";
                                    Map<String,String> map=new HashMap<String,String>();
                                    map.put("phoneID",phoneID);
                                    map.put("phonenumber", phonenumber);
                                    map.put("password", password);
                                    map.put("useraddress",useraddress);
                                    map.put("lastdatetime", lastdatetime);
                                    map.put("login_status",login_status);

                                    OkHttpUtils
                                            .post()
                                            .url(loginTarget)
                                            .params(map)
                                            .build()
                                            .execute(new MyCall());

                                    Toast.makeText(getApplicationContext(),"提交验证码成功", Toast.LENGTH_SHORT).show();

                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showDailog("验证失败");
                                    dialog.dismiss();
                                    //     Toast.makeText(MainActivity.this, "验证失败", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {//获取验证码成功
                        Log.e("TAG", "获取验证码成功");
                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {//返回支持发送验证码的国家列表

                    }
                } else {
                    ((Throwable) data).printStackTrace();
                }


            }
        }
    };


    private String intToIp(int i) {
        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }


    private void showDailog(String text) {
        new AlertDialog.Builder(this)
                .setTitle(text)
                .setPositiveButton("确定", null)
                .show();
    }

    //判断手机号是否正确（包括长度||手机号码的格式是否正确）
    private boolean judgePhoneNums(String phoneNums){
        if(isMatchLength(phoneNums,11)&&isMobileNO(phoneNums)){
            return true;
        }
        Toast.makeText(this,"手机号码输入有误！", Toast.LENGTH_SHORT).show();
        return false;
    }
    //判断手机号码的长度
    public static boolean isMatchLength(String str, int length){
        if(str.isEmpty()){
            return false;
        }else{
            return str.length()==length?true:false;
        }
    }
    //判断手机号码的格式
    public static boolean isMobileNO(String mobileNums){
                 /*   
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188  
         * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）  
         * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9  
         */
        //通过正则表达式判断手机号是否符合格式
        String telRegex="[1][358]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。    
        if (TextUtils.isEmpty(mobileNums))
            return false;
        else
            return mobileNums.matches(telRegex);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //要在activity销毁时反注册，否侧会造成内存泄漏问题
        SMSSDK.unregisterAllEventHandler();
    }


}
