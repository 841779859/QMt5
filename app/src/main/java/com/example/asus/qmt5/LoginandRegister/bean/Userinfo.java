package com.example.asus.qmt5.LoginandRegister.bean;

/**
 * Created by 唇角上扬~勾勒一抹笑 on 2018/5/19.
 */

public class Userinfo {
    public int userID;// 用户ID
    public String phonenumber;//手机号
    public String username;//昵称
    public String password;//密码
    public String realname;//姓名
    public String sex;//性别
    public String birthday;//生日
    public String email;//邮箱
    public String IDcardnum;//身份证号
    public int permission;// 实名认证
    public String zhifubao;//支付宝
    public String touxiang;//头像
    public Userinfo(){//构造函数
        super();
    }
    public Userinfo(String phonenumber){
        super();
        this.phonenumber=phonenumber;

    }
   public Userinfo(String phonenumber,  String username, String IDcardnum, String realname, String birthday, String sex, String email, int permission){
       super();
       this.phonenumber=phonenumber;
       this.username=username;
       this.IDcardnum=IDcardnum;
       this.realname=realname;
       this.realname=realname;
       this.birthday=birthday;
       this.sex=sex;
       this.email=email;
       this.permission=permission;
   }
    public Userinfo(String phonenumber, String password){
        super();
        this.phonenumber=phonenumber;
        this.password=password;
    }

    //生成set和get函数
    public int getUserID() {
        return userID;
    }
    public void setUserID(int userID){
        this.userID=userID;
    }

    public String getPhonenumber() {
        return phonenumber;
    }
    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getRealname() {
        return realname;
    }
    public void setRealname(String realname) {
        this.realname = realname;
    }
    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getBirthday() {
        return birthday;
    }
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getIDcardnum() {
        return IDcardnum;
    }
    public void setIDcardnum(String iDcardnum) {
        IDcardnum = iDcardnum;
    }
    public int getPermission() {
        return permission;
    }
    public void setPermission(int permission) {
        this.permission = permission;
    }

    public String getZhifubao() {
        return zhifubao;
    }
    public void setZhifubao(String zhifubao) {
        this.zhifubao = zhifubao;
    }
    public String getTouxiang() {
        return touxiang;
    }
    public void setTouxiang(String touxiang) {
        this.touxiang = touxiang;
    }

}
