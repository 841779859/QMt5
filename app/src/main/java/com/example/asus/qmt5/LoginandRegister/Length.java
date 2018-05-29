package com.example.asus.qmt5.LoginandRegister;

/**
 * Created by 唇角上扬~勾勒一抹笑 on 2018/4/9.
 */

public class Length {//判断手机号，密码，确认密码长度的类
    public String flagLength(String sphonenumber, String spassword){//判断手机号和密码的长度
        String flag="false";
        flag=flagPhonenumberLength(sphonenumber);
        if(flag.equals("true")){
            flag=flagPwdLength(spassword);
        }
        return flag;
    }
    public String flagPwdLength(String spassword){//判断密码的长度
        String flag="false";
        int pwdLength=spassword.length();
        if((pwdLength>=6)&&(pwdLength<=16)){
            flag="true";
        }
        return flag;
    }


    public String flagPhonenumberLength(String sphonenumber){//判断手机号的长度
        String flag="false";
        int phonenumberLength=sphonenumber.length();
        if(phonenumberLength==11){
            flag="true";
        }
        return flag;
    }
}
