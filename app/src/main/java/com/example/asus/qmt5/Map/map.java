package com.example.asus.qmt5.Map;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.asus.qmt5.LoginandRegister.Shimingrenzheng;
import com.example.asus.qmt5.R;

import com.example.asus.qmt5.Scan.PermissionManager;
import com.example.asus.qmt5.Scan.ShowHouseMessage;
import com.example.asus.qmt5.Scan.bean.HouseInfo;
import com.example.asus.qmt5.Scan.zxing.app.CaptureActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

import static com.example.asus.qmt5.Data.data.url;


public class map extends AppCompatActivity implements PermissionManager.PermissionsResultListener {
    String Tag="ScanActivity";
    ///////////////////6.2
    String codeData,house_ID;
    List<HouseInfo> house;
    String phonenumber;
    String scanTarget=url+"/xiangmu/servlet/CheckServlet";
    /////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        PermissionManager.requestPermission(this, "请求摄像头权限", 1, this, Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA);
    }

    private static final int REQUEST_QRCODE = 0x01;

    public void onQRCodeClick(View view) {
        //启动二维码扫描的页面功能
        SharedPreferences pref=getSharedPreferences("userinfo",MODE_PRIVATE);
        phonenumber=pref.getString("phonenumber","");
        Log.e(Tag,"phone s="+phonenumber);

        OkHttpUtils.post()
                .url(scanTarget)
                .addParams("phonenumber",phonenumber)
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
        { Log.e(Tag,"onresponse="+response);
            if("1".equals(response)) {
                Intent intent = new Intent(map.this, CaptureActivity.class);
                startActivityForResult(intent, REQUEST_QRCODE);
            }else{
                Toast.makeText(map.this,"您还未进行实名认证，请先完成实名认证！",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(map.this, Shimingrenzheng.class);
                startActivity(intent);
            }

        }
    }

    /**二维码扫描结果
     * */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_QRCODE) {
            switch (resultCode) {
                case CaptureActivity.RESULT_CODE_DECODE:
                case Activity.RESULT_OK:
                    codeData = data.getStringExtra(CaptureActivity.EXTRA_DATA);//codeData表示二维码封装的内容
                    codeData=codeData.substring(9);
                    codeData=codeData.substring(0,codeData.length()-1);

                    getListOrderByArray(codeData);



                    Log.d(Tag,"house_ID="+house_ID);
                    Intent intent=new Intent(map.this,ShowHouseMessage.class);
                    intent.putExtra("codedata",house_ID);
                    startActivity(intent);

                    break;
                default:
                    break;
            }
        }
    }
    public List<HouseInfo> getListOrderByArray(String codeData){
        house=new ArrayList<HouseInfo>();
        try{
            JSONArray jsonArray=new JSONArray(codeData);
            Log.e("ceshi",jsonArray.length()+"长度");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=(JSONObject)jsonArray.get(i);
                house_ID=jsonObject.optString("house_ID");

                Log.e(Tag,"house_ID="+house_ID);

                house.add(new HouseInfo(house_ID));

            }
            Log.e(Tag,"houseList1="+house);
            return house;

        }catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.e(Tag,"houseList2="+house);
        return house;

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
