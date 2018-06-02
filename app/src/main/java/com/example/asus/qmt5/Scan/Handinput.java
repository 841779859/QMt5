package com.example.asus.qmt5.Scan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.asus.qmt5.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

import static com.example.asus.qmt5.Data.data.url;


/**
 * Created by 唇角上扬~勾勒一抹笑 on 2018/5/8.
 */

public class Handinput extends AppCompatActivity {
    private LinearLayout content;
    private VerificationCodeView icv;
    private Button usehouse;
    String house_ID;
    private String houseTarget=url+"/xiangmu/servlet/HouseServlet";
    String Tag="Handinput";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hand_input);
        content = (LinearLayout) findViewById(R.id.content);
        icv = (VerificationCodeView) findViewById(R.id.icv);

        usehouse = (Button) findViewById(R.id.use_house);


        usehouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (icv.getInputContent().length() < icv.mEtNumber) {//判断输入的房间编号是否满足房间编号的位数
                    Toast.makeText(Handinput.this, "请输入" + icv.mEtNumber + "位房间编号", Toast.LENGTH_SHORT).show();
                } else {
                    house_ID=icv.getInputContent();
                    OkHttpUtils
                            .post()
                            .url(houseTarget)
                            .addParams("house_ID",house_ID)
                            .build()
                            .execute(new MyCall());

                }
            }
        });

        icv.setInputCompleteListener(new VerificationCodeView.InputCompleteListener() {
            public void inputComplete() {
                Log.i("icv_input", icv.getInputContent());
            }

            public void deleteContent() {
                Log.i("icv_delete", icv.getInputContent());
            }
        });


    }
public String send(){
    house_ID=icv.getInputContent();
    Log.e(Tag, "house_ID="+house_ID);
        return house_ID;

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



            if(!"".equals(house_ID)){

                //  if(response!=null) {
                if ( "exist".equals(response)) {
                    Intent it = new Intent();
                    it.setClass(getApplicationContext(),Kaisuoma.class);
                    startActivity(it);
                }else if("1".equals(response)){
                    Toast.makeText(Handinput.this,"该房间正在被使用！", Toast.LENGTH_SHORT).show();
                }else if("2".equals(response)){
                    Toast.makeText(Handinput.this,"该房间正在维修！", Toast.LENGTH_SHORT).show();
                }else if("3".equals(response)){
                    Toast.makeText(Handinput.this,"该房间已经不再被我们使用！", Toast.LENGTH_SHORT).show();
                }else if("notexist".equals(response)){
                    Toast.makeText(Handinput.this,"请确认输入了正确的房间编号！", Toast.LENGTH_SHORT).show();
                }

            }
        }

    }
}



