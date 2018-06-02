package com.example.asus.qmt5.ProduceScancode;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.qmt5.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

import static com.example.asus.qmt5.Data.data.url;
import static com.example.asus.qmt5.ProduceScancode.zxingutils.addLogo;


public class ProduceActivity extends AppCompatActivity {
    private EditText tv_qrCode_content;//用来生成二维码图片内包含的内容
    private TextView tv_click;//按钮
    private ImageView iv_qr_code;//显示二维码的ImageView
    private String houseTarget=url+"/xiangmu/servlet/QRCodeServlet";
    String Tag="ProduceActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.produce);
        tv_qrCode_content = (EditText) findViewById(R.id.tv_qrCode_content);
        tv_click = (TextView) findViewById(R.id.tv_click);
        iv_qr_code = (ImageView) findViewById(R.id.iv_qr_code);
        tv_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = tv_qrCode_content.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(ProduceActivity.this, "请先输入需要生成二维码的内容", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    OkHttpUtils
                            .post()
                            .url(houseTarget)
                            .addParams("house_ID",content)
                            .build()
                            .execute(new MyCall());
                }
            }
        });
    }
    private class MyCall extends StringCallback {
        @Override
        public void onError(Call call, Exception e, int id)
        {
            Log.e(Tag, "onError" + e.getMessage());
            e.printStackTrace();
        }
        public void onResponse(String response, int id) {
            Log.e(Tag,"response="+response);
            if("notexist".equals(response)){
                Toast.makeText(ProduceActivity.this,"该房间不存在！",Toast.LENGTH_SHORT).show();
            }else {
                Bitmap bitmap = zxingutils.createQRImage(response, iv_qr_code.getWidth(), iv_qr_code.getHeight());
                Bitmap logoBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.house_simple);
                Bitmap bitmap1 = addLogo(bitmap, logoBitmap);
                //  iv_qr_code.setImageBitmap(bitmap);
                iv_qr_code.setImageBitmap(bitmap1);
            }
        }
    }
}










