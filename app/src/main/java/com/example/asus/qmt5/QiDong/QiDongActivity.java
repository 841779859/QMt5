package com.example.asus.qmt5.QiDong;


import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.asus.qmt5.LoginandRegister.Login_password;
import com.example.asus.qmt5.R;

public class QiDongActivity extends AppCompatActivity {
    private Button nextBtn;
    int i=5;
    boolean flag=true;
    boolean isclick=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qi_dong);
        nextBtn=(Button)findViewById(R.id.next_btn);
       final Handler handler=new Handler(){
           public  void handleMessage(Message msg){

               if(msg.what==-9){
                  nextBtn.setText("跳过"+i);
               }


           }

       };

        new Thread(new Runnable(){public void run(){
            for(;i>0;i--){
                handler.sendEmptyMessage(-9);
            if(i<=0){
                flag=false;
                break;
            }

               try{
                    Thread.sleep(1000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
            handler.sendEmptyMessage(-8);

        }
        }).start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isclick==false) {
                    Intent intent = new Intent(QiDongActivity.this, Login_password.class);
                    startActivity(intent);
                    QiDongActivity.this.finish();
                }
            }
        },1000*5);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isclick=true;
                Intent intent=new Intent(QiDongActivity.this,Login_password.class);
                startActivity(intent);
                QiDongActivity.this.finish();
            }
        });


    }
}
