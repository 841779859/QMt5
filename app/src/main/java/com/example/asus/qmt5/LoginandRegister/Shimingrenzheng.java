package com.example.asus.qmt5.LoginandRegister;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.sdk.model.IDCardParams;
import com.baidu.ocr.sdk.model.IDCardResult;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.baidu.ocr.ui.camera.CameraNativeHelper;
import com.baidu.ocr.ui.camera.CameraView;
import com.example.asus.qmt5.BasicView.MainActivity;
import com.example.asus.qmt5.LoginandRegister.bean.Userinfo;
import com.example.asus.qmt5.R;

import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.MediaType;

import static com.example.asus.qmt5.Data.data.url;

/**
 * Created by 唇角上扬~勾勒一抹笑 on 2018/5/21.
 */

public class Shimingrenzheng extends AppCompatActivity {
    private EditText nicknameE, idcardnumberE, realnameE, birthdateE, emailaddressE;
    
    private RadioGroup sexR;
    private RadioButton men,women;
    private Button choosebirth,submit,takephoto;

    String shimingrenzheng=url+"/xiangmu/servlet/ShimingServlet";
    String username, IDcardnum, realname, birthday, email, sex;
    String namei,idnumi,birthi,sexi;
    static String Tag="Shimingrenzheng";
    String phonenumber;
    Calendar calendar;
    DatePickerDialog dialog;
    private static final int REQUEST_CODE_CAMERA = 102;

    boolean flag,flag1;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shimingrenzheng);
        nicknameE=(EditText)findViewById(R.id.nickname);
        idcardnumberE=(EditText)findViewById(R.id.idcard_number);
        realnameE=(EditText)findViewById(R.id.real_name);
        birthdateE=(EditText)findViewById(R.id.birthdate);
        emailaddressE=(EditText)findViewById(R.id.email_address);
        sexR=(RadioGroup)findViewById(R.id.sex);
        men=(RadioButton)findViewById(R.id.men);
        women=(RadioButton)findViewById(R.id.women);
        choosebirth=(Button)findViewById(R.id.choose_birth);
        submit=(Button)findViewById(R.id.submit);

        takephoto=(Button)findViewById(R.id.buttonCamera) ;

        sexR.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(women.getId()==checkedId){
                    sex=women.getText().toString();
                }else if(men.getId()==checkedId){
                    sex=men.getText().toString();
                }
            }
        });

     
        takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Shimingrenzheng.this, CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                        FileUtil.getSaveFile(getApplication()).getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_NATIVE_ENABLE, true);
                // KEY_NATIVE_MANUAL设置了之后CameraActivity中不再自动初始化和释放模型
                // 请手动使用CameraNativeHelper初始化和释放模型
                // 推荐这样做，可以避免一些activity切换导致的不必要的异常
                intent.putExtra(CameraActivity.KEY_NATIVE_MANUAL, true);
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_FRONT);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
            }
        });


        // 初始化
        initAccessTokenWithAkSk();



        

        choosebirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar= Calendar.getInstance();
                dialog=new DatePickerDialog(Shimingrenzheng.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthofYear, int dayOfMonth) {
                        System.out.println("年-->"+year+"月-->"+monthofYear+"日-->"+dayOfMonth);
                        birthdateE.setText(year+"-"+(monthofYear+1)+"-"+dayOfMonth);

                    }
                },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));

                dialog.show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref=getSharedPreferences("userinfo",MODE_PRIVATE);
                phonenumber=pref.getString("phonenumber","");
                Log.e(Tag,"phone s="+phonenumber);
                username=nicknameE.getText().toString();
                IDcardnum=idcardnumberE.getText().toString();
                Panduan panduan=new Panduan();
                flag=panduan.personIdValidation(IDcardnum);
                Log.e(Tag,"phone flag="+flag);
                realname=realnameE.getText().toString();
                birthday=birthdateE.getText().toString();
                email=emailaddressE.getText().toString();
                flag1= panduan.isEmail(email);
                Log.e(Tag,"phone flag1="+flag1);
                int permission=1;
                Log.e("Shimingrenzheng", "姓名1: " + namei);
                Log.e("Shimingrenzheng", "性别1: " + sexi);
                Log.e("Shimingrenzheng", "身份证号1: " + idnumi);
                Log.e("Shimingrenzheng", "出生日期1: " + birthi);
                Log.e("Shimingrenzheng", "realname: " + realname);
                Log.e("Shimingrenzheng", "birthday: " + birthday);
                Log.e("Shimingrenzheng", "IDcardnum: " + IDcardnum);
                Log.e("Shimingrenzheng", "sex: " + sex);
               // Log.e(Tag,"tijiao s="+s.length());
                String json = new Gson().toJson(new Userinfo(phonenumber,  username, IDcardnum,realname, birthday, sex,email,permission));
                if("".equals(username)||"".equals(IDcardnum)||"".equals(realname)||"".equals(birthday)||"".equals(sex)||"".equals(email)){
                    Toast.makeText(Shimingrenzheng.this,"所有的空格都必须填写！",Toast.LENGTH_SHORT).show();
               }else if (flag==false){
                   Toast.makeText(Shimingrenzheng.this,"身份证号码格式错误！",Toast.LENGTH_SHORT).show();
               }else if (flag1==false){
                   Toast.makeText(Shimingrenzheng.this,"邮箱格式错误！",Toast.LENGTH_SHORT).show();
               }else if(!idnumi.equals(IDcardnum)||!namei.equals(realname)||!birthi.equals(birthday)||!sexi.equals(sex)){
                    Toast.makeText(Shimingrenzheng.this,"您所填的信息与身份证的内容不一致，请确认后重新填写！",Toast.LENGTH_SHORT).show();
                }else {


                    OkHttpUtils
                            .postString()
                            .url(shimingrenzheng)
                            .mediaType(MediaType.parse("application/json; charset=utf-8"))
                            .content(json)
                            .build()
                            .execute(new MyCall());
                }

            }
        });

    }
    private void initAccessTokenWithAkSk() {
        OCR.getInstance().initAccessToken(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {

                // 本地自动识别需要初始化
                        initLicense();

                        Log.d("Shimingrenzheng", "onResult: " + result.toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Shimingrenzheng.this, "初始化认证成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onError(OCRError error) {
                        error.printStackTrace();
                        Log.e("Shimingrenzheng", "onError: " + error.getMessage());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Shimingrenzheng.this, "初始化认证失败,请检查 key", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
        }, getApplicationContext());
    }

    private void initLicense() {
        CameraNativeHelper.init(this, OCR.getInstance().getLicense(),
                new CameraNativeHelper.CameraNativeInitCallback() {
                    @Override
                    public void onError(int errorCode, Throwable e) {
                        final String msg;
                        switch (errorCode) {
                            case CameraView.NATIVE_SOLOAD_FAIL:
                                msg = "加载so失败，请确保apk中存在ui部分的so";
                                break;
                            case CameraView.NATIVE_AUTH_FAIL:
                                msg = "授权本地质量控制token获取失败";
                                break;
                            case CameraView.NATIVE_INIT_FAIL:
                                msg = "本地质量控制";
                                break;
                            default:
                                msg = String.valueOf(errorCode);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Shimingrenzheng.this,
                                        "本地质量控制初始化错误，错误原因： " + msg, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String contentType = data.getStringExtra(CameraActivity.KEY_CONTENT_TYPE);
                String filePath = FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath();
                if (!TextUtils.isEmpty(contentType)) {
                    if (CameraActivity.CONTENT_TYPE_ID_CARD_FRONT.equals(contentType)) {
                        recIDCard(IDCardParams.ID_CARD_SIDE_FRONT, filePath);
                    }
                }
            }
        }

    }

    /**
     * 解析身份证图片
     *
     * @param idCardSide 身份证正反面
     * @param filePath   图片路径
     */
    private void recIDCard(String idCardSide, String filePath) {
        IDCardParams param = new IDCardParams();
        param.setImageFile(new File(filePath));
        // 设置身份证正反面
        param.setIdCardSide(idCardSide);
        // 设置方向检测
        param.setDetectDirection(true);
        // 设置图像参数压缩质量0-100, 越大图像质量越好但是请求时间越长。 不设置则默认值为20
        param.setImageQuality(40);
        OCR.getInstance().recognizeIDCard(param, new OnResultListener<IDCardResult>() {
            @Override
            public void onResult(IDCardResult result) {
                if (result != null) {


                    if (result.getName() != null) {
                        namei = result.getName().toString();
                        Log.e("Shimingrenzheng", "姓名: " + namei);
                    }
                    if (result.getGender() != null) {
                        sexi = result.getGender().toString();
                        Log.e("Shimingrenzheng", "性别: " + sexi);
                    }
                    if (result.getIdNumber() != null) {
                        idnumi = result.getIdNumber().toString();
                        Log.e("Shimingrenzheng", "身份证号: " + idnumi);
                    }
                    if(result.getBirthday()!=null){
                        birthi=result.getBirthday().toString();
                        String year=birthi.substring(0,4);
                        String month=birthi.substring(4,6);
                        if(month.substring(0,1).equals("0")){
                            month=month.substring(1,2);
                            Log.e("Shimingrenzheng", "出生日期: " +month);
                        }
                        String day=birthi.substring(6,8);
                        Log.e("Shimingrenzheng", "出生日期: " + year);
                        Log.e("Shimingrenzheng", "出生日期: " +month);
                        Log.e("Shimingrenzheng", "出生日期: "+day);

                        Log.e("Shimingrenzheng", "出生日期: " + year+"-"+month+"-"+day);
                        birthi=year+"-"+month+"-"+day;
                        Log.e("Shimingrenzheng", "出生日期: "+ birthi);


                    }

                }
            }

            @Override
            public void onError(OCRError error) {
                Toast.makeText(Shimingrenzheng.this, "识别出错,请查看log错误代码", Toast.LENGTH_SHORT).show();
                Log.d("Shimingrenzheng", "onError: " + error.getMessage());
            }
        });
    }



    @Override
    protected void onDestroy() {
        CameraNativeHelper.release();
        // 释放内存资源
        OCR.getInstance().release();
        super.onDestroy();

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
            Log.e(Tag,"服务器返回的结果："+response);
              if(response!=null){
                  if("success".equals(response)){
                      Toast.makeText(Shimingrenzheng.this,"您已实名认证成功", Toast.LENGTH_SHORT).show();
                      Intent intent=new Intent(Shimingrenzheng.this,MainActivity.class);
                      startActivity(intent);
                  }else{
                      Toast.makeText(Shimingrenzheng.this,"认证失败，请重试", Toast.LENGTH_SHORT).show();
                  }
              }


        }

    }
}
