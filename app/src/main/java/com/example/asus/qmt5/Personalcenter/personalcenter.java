package com.example.asus.qmt5.Personalcenter;
import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import android.graphics.Bitmap;

import android.provider.MediaStore;
import android.util.Log;


import de.hdodenhof.circleimageview.CircleImageView;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.pm.PackageManager;

import android.os.Build;
import android.provider.DocumentsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;

import com.bumptech.glide.Glide;
import com.example.asus.qmt5.Data.data;
import com.example.asus.qmt5.Map.map;
import com.example.asus.qmt5.Personalcenter.activity.MainActivity;
import com.example.asus.qmt5.R;
import com.example.asus.qmt5.SearchHome.searchhome;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.asus.qmt5.Data.data.url;


/**
 * Created by CC on 2018/1/6.
 */

public class personalcenter extends AppCompatActivity {

    public static personalcenter per = null;
    public static TextView email;
    public static TextView phone;
    public static final int TAKE_PHOTO = 1;//拍照
    public static final int CHOOSE_PHOTO = 2;//选择相册
    public static final int PICTURE_CUT = 3;//剪切图片
    private Uri imageUri;//相机拍照图片保存地址
    private Uri outputUri;//裁剪万照片保存地址
    private String imagePath;//打开相册选择照片的路径
    private boolean isClickCamera;//是否是拍照裁剪
    public Button change;
    public static CircleImageView iamge;
    public static Bitmap bitmap;
    public String phonenumber;
    private MyHandler handler1;
    class MyHandler extends Handler {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    TextView sex=(TextView) findViewById(R.id.sex);
                    sex.setText(msg.obj.toString());
                    break;
                case 2:
                    TextView birth=(TextView) findViewById(R.id.bir);
                    birth.setText(msg.obj.toString());
                    break;
                default:
                    break;

            }
        }
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        SharedPreferences pref=getSharedPreferences("userinfo",MODE_PRIVATE);
        phonenumber=pref.getString("phonenumber","");
        handler1 = new MyHandler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String path = url + "/personal_2.jsp?phonenum="+phonenumber;
                    //2:把网址封装为一个URL对象
                    URL url = new URL(path);
                    //3:获取客户端和服务器的连接对象，此时还没有建立连接
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    //4:初始化连接对象
                    conn.setRequestMethod("GET");
                    //设置连接超时
                    conn.setConnectTimeout(8000);
                    //设置读取超时
                    conn.setReadTimeout(8000);
                    //5:发生请求，与服务器建立连接
                    conn.connect();
                    //如果响应码为200，说明请求成功
                    if (conn.getResponseCode() == 200) {
                        //获取服务器响应头中的流
                        InputStreamReader is = new InputStreamReader(conn.getInputStream());
                        BufferedReader buffer=new BufferedReader(is);
                        String retData ="";
                        String responseData = "";
                        while ((retData = buffer.readLine()) !="") {
                            responseData += retData;
                        }
                        is.close();
                        Message msg = new Message();
                        msg.obj= responseData;
                        msg.what=1;
                        handler1.sendMessage(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String path = url + "/personal_3.jsp?phonenum="+phonenumber;
                    //2:把网址封装为一个URL对象
                    URL url = new URL(path);
                    //3:获取客户端和服务器的连接对象，此时还没有建立连接
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    //4:初始化连接对象
                    conn.setRequestMethod("GET");
                    //设置连接超时
                    conn.setConnectTimeout(8000);
                    //设置读取超时
                    conn.setReadTimeout(8000);
                    //5:发生请求，与服务器建立连接
                    conn.connect();
                    //如果响应码为200，说明请求成功
                    if (conn.getResponseCode() == 200) {
                        //获取服务器响应头中的流
                        InputStreamReader is = new InputStreamReader(conn.getInputStream());
                        BufferedReader buffer=new BufferedReader(is);
                        String retData ="";
                        String responseData = "";
                        while ((retData = buffer.readLine()) !="") {
                            responseData += retData;
                        }
                        responseData=buffer.readLine();
                        is.close();
                        Message msg = new Message();
                        msg.obj= responseData;
                        msg.what=2;
                        handler1.sendMessage(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();




        iamge = (CircleImageView) findViewById(R.id.head);
        change=(Button) findViewById(R.id.changeh);
        phone=(TextView) findViewById(R.id.phn);
        phone.setText(phonenumber);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(personalcenter.this, MainActivity.class);  //进入主界面
                startActivity(intent);  //开始跳转
            }
        });
    }
    public void refresimage(){
        Glide.with(personalcenter.this)
                .load(data.imageurl)
                .into(iamge);
    }
/*    public void show() {
        //加载弹窗视图布局
        View bottom = getLayoutInflater().inflate(R.layout.dialogbottom_layout, null);
        //创建弹窗对象
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(bottom);//设置弹窗布局
        //设置弹窗的状态栏半透明
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //显示弹窗
        dialog.show();
        //设置弹窗的里布局的按钮点击事件
        LinearLayout txt1 = (LinearLayout) bottom.findViewById(R.id.txt1);
        LinearLayout txt2 = (LinearLayout) bottom.findViewById(R.id.txt2);
        LinearLayout txt3 = (LinearLayout) bottom.findViewById(R.id.txt3);
        txt1.setOnClickListener(new MineOnClick(dialog));
        txt2.setOnClickListener(new MineOnClick(dialog));
        txt3.setOnClickListener(new MineOnClick(dialog));
    }*//*


    *//**
     * 点击事件
     *//*
    class MineOnClick implements View.OnClickListener{
        private BottomSheetDialog dialog;
        public MineOnClick(BottomSheetDialog dialog) {
            this.dialog = dialog;
        }
        @Override
        public void onClick(View v) {

            switch (v.getId()){
                case R.id.txt1:
                    if (ContextCompat.checkSelfPermission(personalcenter.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(personalcenter.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    } else {
                        selectFromAlbum();//打开相册
                    }
                    Toast.makeText(personalcenter.this, "1", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.txt2:
                    openCamera();//打开相机
                    Toast.makeText(personalcenter.this, "2", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.txt3:
                    Toast.makeText(personalcenter.this, "3", Toast.LENGTH_SHORT).show();
                    break;
            }
            dialog.dismiss();//关闭弹窗
        }
    }
    @TargetApi(19)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO://拍照
                if (resultCode == RESULT_OK) {
                    cropPhoto(imageUri);//裁剪图片
                }
                break;
            case CHOOSE_PHOTO://打开相册
                // 判断手机系统版本号
                if (Build.VERSION.SDK_INT >= 19) {
                    // 4.4及以上系统使用这个方法处理图片
                    handleImageOnKitKat(data);
                } else {
                    // 4.4以下系统使用这个方法处理图片
                    handleImageBeforeKitKat(data);
                }
                break;
            case PICTURE_CUT://裁剪完成
                isClickCamera = true;
                bitmap = null;
                try {
                    if (isClickCamera) {
                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(outputUri));
                    } else {
                        bitmap = BitmapFactory.decodeFile(imagePath);
                    }
                    iamge.setImageBitmap(bitmap);
                    map.imagetx.setImageBitmap(bitmap);
                    String  touxiang=null;
                    if (DocumentsContract.isDocumentUri(this, outputUri)) {
                        // 如果是document类型的Uri，则通过document id处理
                        String docId = DocumentsContract.getDocumentId(outputUri);
                        if ("com.android.providers.media.documents".equals(outputUri.getAuthority())) {
                            String id = docId.split(":")[1]; // 解析出数字格式的id
                            String selection = MediaStore.Images.Media._ID + "=" + id;
                            touxiang = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
                        } else if ("com.android.providers.downloads.documents".equals(outputUri.getAuthority())) {
                            Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                            touxiang = getImagePath(contentUri, null);
                        }
                    } else if ("content".equalsIgnoreCase(outputUri.getScheme())) {
                        // 如果是content类型的Uri，则使用普通方式处理
                        touxiang = getImagePath(outputUri, null);
                    } else if ("file".equalsIgnoreCase(outputUri.getScheme())) {
                        // 如果是file类型的Uri，直接获取图片路径即可
                        touxiang = outputUri.getPath();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void openCamera() {
        // 创建File对象，用于存储拍照后的图片
        File outputImage = new File(getExternalCacheDir(), phonenumber+".png");
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT < 24) {
            imageUri = Uri.fromFile(outputImage);
        } else {
            //Android 7.0系统开始 使用本地真实的Uri路径不安全,使用FileProvider封装共享Uri
            //参数二:fileprovider绝对路径 com.example.cc.qimotest1：项目包名
            imageUri = FileProvider.getUriForFile(personalcenter.this, "com.example.cc.qimotest1.fileprovider", outputImage);
        }
        // 启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);

    }


    private void selectFromAlbum() {
        if (ContextCompat.checkSelfPermission(personalcenter.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(personalcenter.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            openAlbum();
        }
    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image*//*");
        startActivityForResult(intent, CHOOSE_PHOTO); // 打开相册
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }


    *//**
     * 裁剪图片
     *//*
    private void cropPhoto(Uri uri) {
        // 创建File对象，用于存储裁剪后的图片，避免更改原图
        File file = new File(getExternalCacheDir(), phonenumber+"crop.png");
        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        outputUri = Uri.fromFile(file);
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(uri, "image*//*");
        //裁剪图片的宽高比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("crop", "true");//可裁剪
        // 裁剪后输出图片的尺寸大小
        //intent.putExtra("outputX", 400);
        //intent.putExtra("outputY", 200);
        intent.putExtra("scale", true);//支持缩放
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());//输出图片格式
        intent.putExtra("noFaceDetection", true);//取消人脸识别
        startActivityForResult(intent, PICTURE_CUT);
    }

    // 4.4及以上系统使用这个方法处理图片 相册图片返回的不再是真实的Uri,而是分装过的Uri
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        imagePath = null;
        Uri uri = data.getData();
        Log.d("TAG", "handleImageOnKitKat: uri is " + uri);
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        cropPhoto(uri);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        imagePath = getImagePath(uri, null);
        cropPhoto(uri);
    }*/

}

