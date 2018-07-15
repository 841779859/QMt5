package com.example.asus.qmt5.Personalcenter;

/**
 * Created by 唇角上扬~勾勒一抹笑 on 2018/5/16.
 */

public class Order {
    private String order_ID;
    private String userID;
    private String house_ID;
    private String order_money;
    private String begin_time;
    private String end_time;
    private int order_state;
    private String rent_time;
    private String rent_money;
    public Order(){
        super();
    }
    public Order(String house_ID, String begin_time, String rent_money){
        super();
        this.house_ID=house_ID;
        this.begin_time=begin_time;
        this.rent_money=rent_money;
    }
    public Order(String userID, String house_ID, String order_money , String begin_time, int order_state){
        super();
        this.userID=userID;
        this.house_ID=house_ID;
        this.order_money=order_money;
        this.begin_time=begin_time;
        this.order_state=order_state;
    }
    public String getOrder_ID() {
        return order_ID;
    }
    public void setOrder_ID(String order_ID) {
        this.order_ID = order_ID;
    }
    public String getUserID() {
        return userID;
    }
    public void setUserID(String userID) {
        this.userID = userID;
    }
    public String getHouse_ID() {
        return house_ID;
    }
    public void setHouse_ID(String house_ID) {
        this.house_ID = house_ID;
    }
    public String getOrder_money() {
        return order_money;
    }
    public void setOrder_money(String order_money) {
        this.order_money = order_money;
    }
    public String getBegin_time() {
        return begin_time;
    }
    public void setBegin_time(String begin_time) {
        this.begin_time = begin_time;
    }
    public String getEnd_time() {
        return end_time;
    }
    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }
    public int getOrder_state() {
        return order_state;
    }
    public void setOrder_state(int order_state) {
        this.order_state = order_state;
    }
    public String getRent_time() {
        return rent_time;
    }
    public void setRent_time(String rent_time) {
        this.rent_time = rent_time;
    }
    public String getRent_money() {
        return rent_money;
    }
    public void setRent_money(String rent_money) {
        this.rent_money = rent_money;
    }
}
