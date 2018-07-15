package com.example.asus.qmt5.Personalcenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.asus.qmt5.R;
import java.util.List;

/**
 * Created by 唇角上扬~勾勒一抹笑 on 2018/5/16.
 */

public class HistoryAdapter  extends BaseAdapter {
   private List<Order> orders;
   private LayoutInflater inflater;
   private TextView house_ID,begin_time,rent_money;
   private Order order;
   public  HistoryAdapter(Context context, List<Order> order){
       inflater= LayoutInflater.from(context);
       orders=order;
   }
   public int getCount(){
       return orders.size();
   }
   public Order getItem(int position){//得到list集合的大小
       if(orders==null||orders.size()==0)
           return null;
       return orders.get(position);
   }
   public long getItemId(int position){
       return position;
   }
   public View getView(int position, View convertView, ViewGroup parent){
       if(convertView==null)
           convertView = inflater.inflate(R.layout.history, null);//得到父布局
           order=getItem(position);//得到Order实例
           house_ID=(TextView)convertView.findViewById(R.id.history_house_ID);
           house_ID.setText("房间号:"+order.getHouse_ID().toString()+"  | ");
           rent_money=(TextView)convertView.findViewById(R.id.history_rent_money);
           rent_money.setText(" 花费:"+order.getRent_money().toString()+"元");
           begin_time=(TextView)convertView.findViewById(R.id.history_begin_time);
           begin_time.setText(order.getBegin_time().toString());
           return convertView;
   }
}
