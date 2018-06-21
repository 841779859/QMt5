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
       final Handler handler=new Handler(){//创建handler对象
           public  void handleMessage(Message msg){//重写方法处理界面跳过的数字显示

               if(msg.what==-9){
                  nextBtn.setText("跳过"+i);
               }


           }

       };

        new Thread(new Runnable(){public void run(){//创建线程子线程
            for(;i>0;i--){
                handler.sendEmptyMessage(-9);//发送空信息
            if(i<=0){
                flag=false;
                break;
            }

               try{
                    Thread.sleep(1000);//线程休眠1s
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
            handler.sendEmptyMessage(-8);

        }
        }).start();//开启线程
        new Handler().postDelayed(new Runnable() {//延迟发送Runnable对象
            @Override
            public void run() {
                if(isclick==false) {
                    Intent intent = new Intent(QiDongActivity.this, Login_password.class);
                    startActivity(intent);
                    QiDongActivity.this.finish();//关闭活动
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
