package com.example.asus.qmt5.LoginandRegister.bean;

public class LoginInfo {
public int userID;
public String phoneID;
public String phonenumber;
public String password;
public String useraddress;
public String lastdatetime;
public int login_status;
public LoginInfo(){//构造函数
	super();
}
public LoginInfo(String phonenumber,String password){//构造函数
	super();
	
	this.phonenumber=phonenumber;
	this.password=password;
	
}
public LoginInfo(String phoneID,String phonenumber,String useraddress,String lastdatetime,int login_status){//构造函数
	super();
	this.phoneID=phoneID;
	this.phonenumber=phonenumber;
	this.useraddress=useraddress;
	this.lastdatetime=lastdatetime;
	this.login_status=login_status;
}

public LoginInfo(String phoneID,String phonenumber,String password,String useraddress,String lastdatetime,int login_status){//构造函数
	super();
	this.phoneID=phoneID;
	this.phonenumber=phonenumber;
	this.password=password;
	this.useraddress=useraddress;
	this.lastdatetime=lastdatetime;
	this.login_status=login_status;
}
//get and set
public int getUserID() {
	return userID;
}
public void setUserID(int userID) {
	this.userID = userID;
}
public String getPhoneID() {
	return phoneID;
}
public void setPhoneID(String phoneID) {
	this.phoneID = phoneID;
}
public String getPhonenumber() {
	return phonenumber;
}
public void setPhonenumber(String phonenumber) {
	this.phonenumber = phonenumber;
}
public String getPassword() {
	return password;
}
public void setPassword(String password) {
	this.password = password;
}
public String getUseraddress() {
	return useraddress;
}
public void setUseraddress(String useraddress) {
	this.useraddress = useraddress;
}
public String getLastdatetime() {
	return lastdatetime;
}
public void setLastdatetime(String lastdatetime) {
	this.lastdatetime = lastdatetime;
}
public int getLogin_status() {
	return login_status;
}
public void setLogin_status(int login_status) {
	this.login_status = login_status;
}

}
