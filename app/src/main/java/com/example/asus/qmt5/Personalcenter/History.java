package com.example.asus.qmt5.Personalcenter;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.example.asus.qmt5.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;


/**
 * Created by 唇角上扬~勾勒一抹笑 on 2018/5/15.
 */

public class History extends AppCompatActivity {
    String orderTarget="http://192.168.1.101:8080/xiangmu/servlet/OrderServlet";

    String Tag="History";
    private ListView listview;
    HistoryAdapter adapter;
    List<Order> orderList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historylist);
        listview=(ListView)findViewById(R.id.historylist);

        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        String phonenumber = sharedPreferences.getString("phonenumber", "");
                OkHttpUtils
                        .post()
                        .url(orderTarget)
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
        {
            Log.e(Tag,"response="+response);
            response=response.substring(9);
            response=response.substring(0,response.length()-1);

            getListOrderByArray(response);

        }
        public List<Order> getListOrderByArray(String response){
            orderList=new ArrayList<Order>();
           try{
               JSONArray jsonArray=new JSONArray(response);
                Log.e("ceshi",jsonArray.length()+"长度");
                for(int i=0;i<jsonArray.length();i++){
                   JSONObject jsonObject=(JSONObject)jsonArray.get(i);
                   String house_ID=jsonObject.optString("house_ID");
                   Log.e(Tag,"house_ID="+house_ID);
                   String begin_time=jsonObject.optString("begin_time");
                    Log.e(Tag,"begin_time="+begin_time);
                    String rent_money=jsonObject.optString("rent_money");
                   Log.e(Tag,"rent_money="+rent_money);
                   orderList.add(new Order(house_ID, begin_time,rent_money));
                    adapter=new HistoryAdapter(History.this,orderList);
                    registerForContextMenu(listview);
                    listview.setAdapter(adapter);
               }
                Log.e(Tag,"orderList1="+orderList);
               return orderList;

            }catch(JSONException e){
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Log.e(Tag,"orderList2="+orderList);
           return orderList;

        }
    }

    }



