package com.example.asus.qmt5.LoginandRegister;

import android.Manifest;
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

/**
 * Created by 唇角上扬~勾勒一抹笑 on 2018/5/21.
 */

public class Shimingrenzheng extends AppCompatActivity {
    private EditText nicknameE, idcardnumberE, realnameE, birthdateE, emailaddressE;
    
    private RadioGroup sexR;
    private RadioButton men,women;
    private Button choosebirth,submit,takephoto,pickphoto;
    private ImageView idimage;
    String shimingrenzheng="http://192.168.1.105:8080/xiangmu/servlet/ShimingServlet";
    String username;
    String IDcardnum;
    String realname;
    String birthday;
    String email;
    String sex;
    static String Tag="Shimingrenzheng";
    String phonenumber;
    Calendar calendar;
    private static final String SD_PATH = "/sdcard/dskqxt/pic/";
    private static final String IN_PATH = "/all/live/pic/";
    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg");
    private Uri imageUri;
    private Uri cropImageUri;
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 0x03;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;
    //截取的宽高
    private static final int OUTPUT_X = 640;
    private static final int OUTPUT_Y = 480;
    String s;
    static String path;
    private DatePickerDialog dialog;
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
        idimage=(ImageView)findViewById(R.id.idcard_pic);
        takephoto=(Button)findViewById(R.id.buttonCamera) ;
        pickphoto=(Button)findViewById(R.id.buttonLocal) ;

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
                autoObtainCameraPermission();
                Log.e(Tag,"take s="+s);
                Log.e(Tag,"take path="+path);


            }
        });
        pickphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoObtainStoragePermission();
                Log.e(Tag,"pick s="+s);
                Log.e(Tag,"pick path="+path);
            }
        });
        

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
                Log.e(Tag,"phone s="+phonenumber);
                Log.e(Tag,"tijiao s="+s);
               // Log.e(Tag,"tijiao s="+s.length());
                String json = new Gson().toJson(new Userinfo(phonenumber,  username, IDcardnum,s,realname, birthday, sex,email,permission));
               if("".equals(username)||"".equals(IDcardnum)||"".equals(realname)||"".equals(birthday)||"".equals(sex)||"".equals(email)||s==null){
                    Toast.makeText(Shimingrenzheng.this,"所有的空格都必须填写！",Toast.LENGTH_SHORT).show();
               }else if (flag==false){
                   Toast.makeText(Shimingrenzheng.this,"身份证号码格式错误！",Toast.LENGTH_SHORT).show();
               }else if (flag1==false){
                   Toast.makeText(Shimingrenzheng.this,"邮箱格式错误！",Toast.LENGTH_SHORT).show();
               } else {
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
    /**
     * 自动获取相机权限
     */
    private void autoObtainCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                Toast.makeText(this, "xxxxxxxxxxxxxxxx", Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSIONS_REQUEST_CODE);
        } else {//有权限直接调用系统相机拍照
            if (hasSdcard()) {
                imageUri = Uri.fromFile(fileUri);
                //通过FileProvider创建一个content类型的Uri
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    imageUri = FileProvider.getUriForFile(Shimingrenzheng.this, "app.king", fileUri);
                }
                PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "您没有SD卡", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 自动获取sdk权限
     */
    private void autoObtainStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSIONS_REQUEST_CODE);
        } else {
            PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                //拍照完成回调
                case CODE_CAMERA_REQUEST:
                    cropImageUri = Uri.fromFile(fileCropUri);
                    PhotoUtils.cropImageUri(this, imageUri, cropImageUri, 1, 1, OUTPUT_X, OUTPUT_Y, CODE_RESULT_REQUEST);
//                    String path = PhotoUtils.getPath(this, cropImageUri);

                    break;
                //访问相册完成回调
                case CODE_GALLERY_REQUEST:
                    if (hasSdcard()) {
                        cropImageUri = Uri.fromFile(fileCropUri);
                        Uri newUri = Uri.parse(PhotoUtils.getPath(this, data.getData()));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            newUri = FileProvider.getUriForFile(this, "app.king", new File(newUri.getPath()));
                        }
                        PhotoUtils.cropImageUri(this, newUri, cropImageUri, 1, 1, OUTPUT_X, OUTPUT_Y, CODE_RESULT_REQUEST);
                    } else {
                        Toast.makeText(this, "ssss", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case CODE_RESULT_REQUEST:
                    Bitmap bitmap = PhotoUtils.getBitmapFromUri(cropImageUri, this);
                    if (bitmap != null) {
                        //设置头像的地方
                        idimage.setImageBitmap(bitmap);
                        String state = Environment.getExternalStorageState();
                        //如果状态不是mounted，无法读写
                        if (!state.equals(Environment.MEDIA_MOUNTED)) {
                            return;
                        }
                        s = saveBitmap(this, bitmap);
                        Log.e(Tag,"s="+s);
//                        try {
//                            File file = new File(SD_PATH + fileName + ".jpg");
//                            FileOutputStream out = new FileOutputStream(file);
//                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
//                            out.flush();
//                            out.close();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        Log.e("TAG",SD_PATH + fileName + ".jpg");

                    }
                    break;
                default:
            }
        }
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 这个方法主要实现于吧bitmap图片缓存到本地
     * @param context 本类对象
     * @param mBitmap 这是你所得到的图片
     * @return 返回的是本地的缓存路径
     */

    public static String saveBitmap(Context context, Bitmap mBitmap) {
        String savePath;
        File filePic;
        String s = get();
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            savePath = SD_PATH;
        } else {
            savePath = context.getApplicationContext().getFilesDir()
                    .getAbsolutePath()
                    + IN_PATH;
        }
        try {
            filePic = new File(savePath + s + ".jpg");
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
       // String path;
        path=filePic.getAbsolutePath();
        Log.e(Tag,"path="+path);
        return path;
                //filePic.getAbsolutePath();
    }

    /**
     * 返回的是根据时间生成的字符串
     * @return
     */
    private static String get() {
        Calendar now = new GregorianCalendar();
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        String fileName = simpleDate.format(now.getTime());
        return fileName;
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
