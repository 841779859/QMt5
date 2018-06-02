package com.example.asus.qmt5.Scan;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.asus.qmt5.R;

import com.example.asus.qmt5.Scan.zxing.app.CaptureActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

import static com.example.asus.qmt5.Data.data.url;

public class ScanActivity extends AppCompatActivity implements PermissionManager.PermissionsResultListener {
//https://blog.csdn.net/IThtt/article/details/77084448参考博客
private String houseTarget=url+"/xiangmu/servlet/ScanServlet";
String Tag="ScanActivity";
///////////////////6.2
String codeData;

/////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clickscan);
        PermissionManager.requestPermission(this, "请求摄像头权限", 1, this, Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA);
    }

    private static final int REQUEST_QRCODE = 0x01;

    public void onQRCodeClick(View view) {
        //启动二维码扫描的页面功能
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivityForResult(intent, REQUEST_QRCODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_QRCODE) {
            switch (resultCode) {
                case CaptureActivity.RESULT_CODE_DECODE:
                case Activity.RESULT_OK:
                     codeData = data.getStringExtra(CaptureActivity.EXTRA_DATA);
                    codeData=codeData.substring(0,13);
                    codeData=codeData.substring(5);
                    Log.d(Tag,"codedata="+codeData);
                    OkHttpUtils
                            .post()
                            .url(houseTarget)
                            .addParams("code",codeData)
                            .build()
                            .execute(new MyCall());

                   // Intent intent=new Intent(ScanActivity.this,Kaisuoma.class);
                   // startActivity(intent);
                    break;
                default:
                    break;
            }
        }
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

            if(response!=null) {
                if ( "exist".equals(response)) {
                    Intent it = new Intent();
                    it.setClass(getApplicationContext(),Kaisuoma.class);
                    ////////////////6.2

                    it.putExtra("codedata",codeData);
                    Log.e(Tag, "codeData"+codeData);
                    //////////////
                    startActivity(it);//界面跳转
                }else if("1".equals(response)){
                    Toast.makeText(ScanActivity.this,"该房间正在被使用！", Toast.LENGTH_SHORT).show();
                }else if("2".equals(response)){

                    Toast.makeText(ScanActivity.this,"该房间正在维修！", Toast.LENGTH_SHORT).show();
                }else if("3".equals(response)){
                    Toast.makeText(ScanActivity.this,"该房间已经不再被我们使用！", Toast.LENGTH_SHORT).show();
                }else if("notexist".equals(response)){
                    Toast.makeText(ScanActivity.this,"请扫描ofohouse的二维码！", Toast.LENGTH_SHORT).show();
                }

            }


        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.onRequestResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onPermissionGranted(int requestCode) {
        Toast.makeText(this, "申请权限成功", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onPermissionDenied(int requestCode) {
        Toast.makeText(this, "申请权限失败", Toast.LENGTH_SHORT).show();

    }
}
