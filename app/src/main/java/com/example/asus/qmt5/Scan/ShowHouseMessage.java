package com.example.asus.qmt5.Scan;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.qmt5.BasicView.MainActivity;
import com.example.asus.qmt5.R;
import com.example.asus.qmt5.Scan.bean.HouseInfo;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

import static com.example.asus.qmt5.Data.data.url;

/**
 * Created by 唇角上扬~勾勒一抹笑 on 2018/6/4.
 */

public class ShowHouseMessage extends AppCompatActivity {
    private TextView housename,houseprice,houselocate,housestate,houseintroduce;
    private Button order,live;
    String house_name,house_addr,house_price,house_state,house_intorduce,houseID;
    String Tag="ShowHouseMessage";
    String ShowHouseMessageurl=url+"/xiangmu/servlet/ShowHouseMessageServlet";
    List<HouseInfo>house;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showhousemwssage);
        housename=(TextView)findViewById(R.id.house_name);
        houselocate=(TextView)findViewById(R.id.house_locate);
        houseprice=(TextView)findViewById(R.id.house_price);
        housestate=(TextView)findViewById(R.id.house_show_state);
        houseintroduce=(TextView)findViewById(R.id.house_show_introduce);
        order=(Button)findViewById(R.id.order);
        live=(Button)findViewById(R.id.live_immediate);
        Intent intent=getIntent();
         houseID=intent.getStringExtra("codedata");
        Log.e(Tag, "houseID="+houseID);

        OkHttpUtils
                .post()
                .url(ShowHouseMessageurl)
                .addParams("house_ID",houseID)
                .build()
                .execute(new MyCall());
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent=new Intent(ShowHouseMessage.this,MainActivity.class);
                startActivity(intent);
            }
        });
        live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("1".equals(house_state)){
                    Toast.makeText(ShowHouseMessage.this,"该房间正在被占用",Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(ShowHouseMessage.this, Kaisuoma.class);
                    intent.putExtra("codedata",houseID);
                    startActivity(intent);
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


        public void onResponse(String  response, int id)
        {
            Log.e(Tag,"response="+response);
            response=response.substring(9);
            response=response.substring(0,response.length()-1);

            getListOrderByArray(response);

        }
        public List<HouseInfo> getListOrderByArray(String response){
            house=new ArrayList<HouseInfo>();
            try{
                JSONArray jsonArray=new JSONArray(response);
                Log.e("ceshi",jsonArray.length()+"长度");
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject=(JSONObject)jsonArray.get(i);
                     house_name=jsonObject.optString("house_name");
                    housename.setText(house_name);
                    Log.e(Tag,"house_name="+house_name);
                    house_addr=jsonObject.optString("house_addr");
                    houselocate.setText(house_addr);
                    Log.e(Tag,"house_addr="+house_addr);
                     house_price=jsonObject.optString("house_price");
                    Log.e(Tag,"house_price="+house_price);
                    houseprice.setText(house_price+"元/小时");
                     house_state=jsonObject.optString("house_state");
                    Log.e(Tag,"house_state="+house_state);
                    if("1".equals(house_state)){
                        housestate.setText("该房间已经被占用！");

                    }else if("0".equals(house_state)){
                        housestate.setText("该房间目前无人使用，你可以选择立刻入住！");

                    }

                     house_intorduce=jsonObject.optString("house_introduce");
                    houseintroduce.setText(house_intorduce);
                    Log.e(Tag,"house_intorduce="+house_intorduce);
                    house.add(new HouseInfo(house_name,house_addr,house_price,house_state,house_intorduce));

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
    }
}
