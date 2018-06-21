package com.example.asus.qmt5.House;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.asus.qmt5.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.example.asus.qmt5.Data.data.url;

//import com.example.asus.qmt5.R;


public class Hours extends AppCompatActivity {

    private ViewPager view_pager;
    private LinearLayout ll_dotGroup;
    private TextView newsTitle;
    private int imgResIds[] = new int[]{R.drawable.wu2, R.drawable.wu3,
            R.drawable.wu4, R.drawable.wu5};
    //存储5张图片
    private String textview[] = new String[]{"热", "烈"
            , "欢", "迎"};
    //存储5个目录
    private int curIndex = 0;
    //用来记录当前滚动的位置
    PicsAdapter picsAdapter;
    final int LLN = 1995;

    private Chronometer jishi;
    private Button tui_fang;
    String Tag = "Hours";
    String Target = url+ "/orderServlet1";
    String house_ID;
    String process_time;
    String order_time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hours);


        Button baoxiu=(Button)findViewById(R.id.baoxiu);
        baoxiu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Hours.this,Help.class);
                startActivity(intent);
            }
        });

        setViewPager();
        final Chronometer ch = (Chronometer) findViewById(R.id.jishi);
        ch.setBase(SystemClock.elapsedRealtime());
        int hour = (int) ((SystemClock.elapsedRealtime() - ch.getBase()) / 1000 / 60);
        ch.setFormat("0"+String.valueOf(hour)+":%s");
        ch.start();
        ch.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
            }

        });






        jishi = (Chronometer) findViewById(R.id.jishi);
        tui_fang = (Button) findViewById(R.id.tui_fang);
        tui_fang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ch.stop();
                 Intent  intent = getIntent();
                house_ID =intent.getStringExtra("house_ID");
                house_ID="00000001";
                Log.e(Tag, house_ID);
                 process_time = jishi.getText().toString();
                Log.e(Tag, process_time);
                Date date=new Date(System.currentTimeMillis());//date对象获取系统时间
                SimpleDateFormat format=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
                 order_time=format.format(date);
                Log.e(Tag, order_time);




                String s = process_time;
                int index1 = s.indexOf(":");
                int index2 = s.indexOf(":", index1 + 1);
                int hh = Integer.parseInt(s.substring(0, index1));
                int mi = Integer.parseInt(s.substring(index1 + 1, index2));
                int ss = Integer.parseInt(s.substring(index2 + 1));
                Log.e(Tag, process_time= String.valueOf((hh * 60 * 60 + mi * 60 + ss)/60));

                Intent intent1 = new Intent(Hours.this, TuiFang.class);
                //  startActivityForResult(intent, LLN);
                intent1.putExtra("jishiqi1",process_time);
                intent1.putExtra("jishiqi2",house_ID);
                intent1.putExtra("jishiqi3",order_time);
                intent1.putExtra("house_ID",house_ID);
                startActivity(intent1);

               OkHttpUtils
                        .post()
                        .url(Target)
                        .addParams("house_ID",house_ID)
                        .addParams("process_time",process_time)
                        .addParams("order_time",order_time)
                        .build()
                        .execute(new MyCall());//回调方法

            }
        });
    }

    class MyCall extends StringCallback {

        @Override
        public void onError(okhttp3.Call call, Exception e, int id) {
            Log.e(Tag, " e=" + e.getMessage());
            e.printStackTrace();//打印
        }

        @Override
        public void onResponse(String response, int id) {//服务器返回的结果
            if ("success".equals(response)) {
                Toast.makeText(getApplicationContext(), "插入成功！", Toast.LENGTH_SHORT).show();
            } else if ("error".equals(response)) {
                Toast.makeText(getApplicationContext(), "插入失败！", Toast.LENGTH_SHORT).show();
            }


           /* if ("success".equals(response)) {
                Toast.makeText(Hours.this, "1111", Toast.LENGTH_SHORT)
                        .show();
                Log.e(Tag,"response"+111111);
                finish();
            } else if (!"exist".equals(response)) {
                Toast.makeText(Hours.this, "成功", Toast.LENGTH_SHORT).show();
            } else if ("fail".equals(response)) {
                Toast.makeText(Hours.this, "失败", Toast.LENGTH_SHORT).show();
            }*/
        }
    }
    /*
     * 将时分秒转为秒数
     * */
    public long formatTurnSecond(String process_time) {
        String s = process_time;

        int index1 = s.indexOf(":");
        int index2 = s.indexOf(":", index1 + 1);
        int hh = Integer.parseInt(s.substring(0, index1));
        int mi = Integer.parseInt(s.substring(index1 + 1, index2));
        int ss = Integer.parseInt(s.substring(index2 + 1));
        Log.e(Tag, "formatTurnSecond: 时间== " + hh * 60 * 60 + mi * 60 + ss);
        return hh * 60 * 60 + mi * 60 + ss;


    }





    private void setViewPager() {

        newsTitle=(TextView)findViewById(R.id.NewsTitle);
        view_pager = (ViewPager) findViewById(R.id.view_pager);
        ll_dotGroup = (LinearLayout) findViewById(R.id.dotgroup);

        picsAdapter = new PicsAdapter(); // 创建适配器
        picsAdapter.setData(imgResIds);
        view_pager.setAdapter(picsAdapter); // 设置适配器

        view_pager.setOnPageChangeListener(new MyPageChangeListener()); //设置页面切换监听器

        initPoints(imgResIds.length); // 初始化图片小圆点
        startAutoScroll(); // 开启自动播放
    }


    // 初始化图片轮播的小圆点和目录
    private void initPoints(int count) {
        for (int i = 0; i < count; i++) {

            ImageView iv = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    20, 20);
            params.setMargins(0, 0, 20, 0);
            iv.setLayoutParams(params);

            iv.setImageResource(R.drawable.dot_normal);

            ll_dotGroup.addView(iv);

        }
        ((ImageView) ll_dotGroup.getChildAt(curIndex))
                .setImageResource(R.drawable.dot_focused);

        newsTitle.setText(textview[curIndex]);
    }

    // 自动播放
    private void startAutoScroll() {
        ScheduledExecutorService scheduledExecutorService = Executors
                .newSingleThreadScheduledExecutor();
        // 每隔4秒钟切换一张图片
        scheduledExecutorService.scheduleWithFixedDelay(new ViewPagerTask(), 5,
                4, TimeUnit.SECONDS);
    }

    // 切换图片任务
    private class ViewPagerTask implements Runnable {
        @Override
        public void run() {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int count = picsAdapter.getCount();
                    view_pager.setCurrentItem((curIndex + 1) % count);
                }
            });
        }
    }

    // 定义ViewPager控件页面切换监听器
    class MyPageChangeListener implements ViewPager.OnPageChangeListener {


        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {
        }


        public void onPageSelected(int position) {
            ImageView imageView1 = (ImageView) ll_dotGroup.getChildAt(position);
            ImageView imageView2 = (ImageView) ll_dotGroup.getChildAt(curIndex);
            if (imageView1 != null) {
                imageView1.setImageResource(R.drawable.dot_focused);
            }
            if (imageView2 != null) {
                imageView2.setImageResource(R.drawable.dot_normal);
            }
            curIndex = position;
            newsTitle.setText(textview[curIndex]);

        }


        boolean b = false;


        public void onPageScrollStateChanged(int state) {
            //这段代码可不加，主要功能是实现切换到末尾后返回到第一张
            switch (state) {
                case 1:// 手势滑动
                    b = false;
                    break;
                case 2:// 界面切换中
                    b = true;
                    break;
                case 0:// 滑动结束，即切换完毕或者加载完毕
                    // 当前为最后一张，此时从右向左滑，则切换到第一张
                    if (view_pager.getCurrentItem() == view_pager.getAdapter()
                            .getCount() - 1 && !b) {
                        view_pager.setCurrentItem(0);
                    }
                    // 当前为第一张，此时从左向右滑，则切换到最后一张
                    else if (view_pager.getCurrentItem() == 0 && !b) {
                        view_pager.setCurrentItem(view_pager.getAdapter()
                                .getCount() - 1);
                    }
                    break;

                default:
                    break;
            }
        }
    }

    // 定义ViewPager控件适配器
    class PicsAdapter extends PagerAdapter {

        private List<ImageView> views = new ArrayList<ImageView>();

        @Override
        public int getCount() {
            if (views == null) {
                return 0;
            }
            return views.size();
        }

        public void setData(int[] imgResIds) {
            for (int i = 0; i < imgResIds.length; i++) {
                ImageView iv = new ImageView(Hours.this);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                iv.setLayoutParams(params);
                iv.setScaleType(ImageView.ScaleType.FIT_XY);
                //设置ImageView的属性
                iv.setImageResource(imgResIds[i]);
                views.add(iv);
            }
        }

        public Object getItem(int position) {
            if (position < getCount())
                return views.get(position);
            return null;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {

            if (position < views.size())
                ((ViewPager) container).removeView(views.get(position));
        }

        @Override
        public int getItemPosition(Object object) {
            return views.indexOf(object);
        }

        @Override
        public Object instantiateItem(View container, int position) {
            if (position < views.size()) {
                final ImageView imageView = views.get(position);
                ((ViewPager) container).addView(imageView);
                return views.get(position);
            }
            return null;
        }

    }

}
