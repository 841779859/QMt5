package com.example.asus.qmt5.Map;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.bumptech.glide.Glide;
import com.example.asus.qmt5.Data.IpQH;
import com.example.asus.qmt5.Data.data;
import com.example.asus.qmt5.LoginandRegister.Login_password;
import com.example.asus.qmt5.LoginandRegister.Shimingrenzheng;
import com.example.asus.qmt5.Personalcenter.History;
import com.example.asus.qmt5.Personalcenter.personalcenter;
import com.example.asus.qmt5.R;
import com.example.asus.qmt5.Scan.ShowHouseMessage;
import com.example.asus.qmt5.Scan.zxing.app.CaptureActivity;
import com.example.asus.qmt5.SearchHome.searchhome;
import com.example.asus.qmt5.Update.AppUpDataManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

import static com.example.asus.qmt5.Data.data.url;

/**
 * Created by ASUS on 2018/6/19.
 */

public class map  extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    /*地图相关*/
    public static ImageView imagetx;
    String Tag="ScanActivity";
    private TextView level;
    private TextView name;
    private double lat;
    private double lon;
    private double lati;
    private double loni;
    public String city;
    private DrawerLayout drawerLayout;
    private MapView mMapView = null;    //定义地图控件
    private BaiduMap mBaiduMap;         //定义百度地图对象
    boolean isFirstLoc = true;         //是否首次定位
    public LocationClient mLocationClient = null;   //定位服务
    public BDLocationListener myListener = new MyLocationListener();    //创建监听器
    public Button btn_location;               //定义按钮
    public ImageView sousuo;
    public Button scan;
    public String txurl;
    public String phone;
    public String userid="1";
    public SharedPreferences sp;
    public SharedPreferences.Editor editor;
    public static map m;
    private String permissionInfo;
    private final int SDK_PERMISSION_REQUEST = 127;
    String scanTarget=url+"/servlet/CheckServlet";
    private static final int REQUEST_QRCODE = 0x01;
    String codeData,house_ID;
    List<com.example.asus.qmt5.Scan.bean.HouseInfo> house;
    private ProgressDialog progressDialog;
    private String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private final int PERMS_REQUEST_CODE = 200;
    private MyHandler handler1;
    class MyHandler extends Handler {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:

                    if (msg.arg1 == 1)
                        level.setText("已认证");
                    else
                        level.setText("未认证");
                    break;
                case 2:
                    imagetx=(ImageView) findViewById(R.id.imageView);
                    imagetx.setImageBitmap((Bitmap)msg.obj);
                    break;
                default:
                    break;

            }
        }
    }
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationClient = new LocationClient(getApplicationContext());  //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);          //注册监听函数
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        mLocationClient.start();
        setContentView(R.layout.activity_map);

        scan=(Button) findViewById(R.id.scan);
        mMapView = (MapView) findViewById(R.id.mapView);//获取地图控件引用
        mMapView.showZoomControls(false);
        mMapView.removeViewAt(1);
        mBaiduMap = mMapView.getMap();                      //获取百度地图对象
        mBaiduMap.setMyLocationEnabled(true);//开启定位图层
        initLocation();                                       //调用配置定位参数方法
        getPersimmions();
        btn_location = (Button) findViewById(R.id.location_btn);        //获取定位按钮
        btn_location.setOnClickListener(new View.OnClickListener() {      //按钮单击事件
            @Override
            public void onClick(View v) {
                isFirstLoc = true;              //用于二次定位
                mLocationClient.start();        //启动定位
            }
        });

        SharedPreferences pref=getSharedPreferences("userinfo",MODE_PRIVATE);
        phone=pref.getString("phonenumber","");
        data.ph=phone;
        Log.e(Tag,"phone s="+phone);








        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            public boolean onMarkerClick(Marker marker) {
                //从marker中获取info信息
                Bundle bundle = marker.getExtraInfo();
                String houseid=bundle.getString("m");


                ///////////////////////////////
                return true;
            }
        });

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               /* if (ContextCompat.checkSelfPermission(com.example.asus.qmt5.Map.map2.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(com.example.asus.qmt5.Map.map2.this, new String[]{Manifest.permission.CAMERA}, 1);
                }else {
                    startActivity(new Intent(com.example.asus.qmt5.Map.map2.this, CaptureActivity.class));
                }*/
                OkHttpUtils.post()
                        .url(scanTarget)
                        .addParams("phonenumber",phone)
                        .build()
                        .execute(new MyCall());

                //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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
        public void onResponse(String response, int id)
        { Log.e(Tag,"onresponse="+response);
            if("1".equals(response)) {
                Intent intent = new Intent(map.this, CaptureActivity.class);
                startActivityForResult(intent, REQUEST_QRCODE);
            }else{
                Toast.makeText(map.this,"您还未进行实名认证，请先完成实名认证！",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(map.this, Shimingrenzheng.class);
                startActivity(intent);
            }

        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_QRCODE) {
            switch (resultCode) {
                case CaptureActivity.RESULT_CODE_DECODE:
                case Activity.RESULT_OK:
                    codeData = data.getStringExtra(CaptureActivity.EXTRA_DATA);//codeData表示二维码封装的内容
                    codeData=codeData.substring(9);
                    codeData=codeData.substring(0,codeData.length()-1);

                    getListOrderByArray(codeData);



                    Log.d(Tag,"house_ID="+house_ID);
                    Intent intent=new Intent(map.this,ShowHouseMessage.class);
                    intent.putExtra("codedata",house_ID);
                    startActivity(intent);

                    break;
                default:
                    break;
            }
        }
    }
    public List<com.example.asus.qmt5.Scan.bean.HouseInfo> getListOrderByArray(String codeData){
        house=new ArrayList<com.example.asus.qmt5.Scan.bean.HouseInfo>();
        try{
            JSONArray jsonArray=new JSONArray(codeData);
            Log.e("ceshi",jsonArray.length()+"长度");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=(JSONObject)jsonArray.get(i);
                house_ID=jsonObject.optString("house_ID");

                Log.e(Tag,"house_ID="+house_ID);

                house.add(new com.example.asus.qmt5.Scan.bean.HouseInfo(house_ID));

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


    private void getPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            /***
             * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
             */
            // 定位精确位置
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
			/*
			 * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
			 */
            // 读写权限
            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
            }
            // 读取电话状态权限
            if (addPermission(permissions, Manifest.permission.READ_PHONE_STATE)) {
                permissionInfo += "Manifest.permission.READ_PHONE_STATE Deny \n";
            }
            if (addPermission(permissions, Manifest.permission.CAMERA)) {
                permissionInfo += "Manifest.permission.READ_PHONE_STATE Deny \n";
            }

            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            }
        }
    }

    @TargetApi(23)
    private boolean addPermission(ArrayList<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) { // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
            if (shouldShowRequestPermissionRationale(permission)) {
                return true;
            } else {
                permissionsList.add(permission);
                return false;
            }

        } else {
            return true;
        }
    }

    private void setMarker() {
        //定义Maker坐标点
        LatLng point = new LatLng(lat, lon);
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.man);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);
    }

    /**
     * 设置中心点
     */
    private void setUserMapCenter() {
        LatLng cenpt = new LatLng(lat, lon);
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(cenpt)
                .zoom(18)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);

        setMakerPool(cenpt);
        //Toast.makeText(this,"cenpt:"+cenpt,Toast.LENGTH_LONG).show();
/*===========================================测试添加房屋数据=====================================================*/
/*HousesDAO housesDAO=new HousesDAO(this);


Tb_houses houses=new Tb_houses(1+"","测试房"+1,"孙超超","","江西师大","28.688350;116.0351558","10","12345678",
        "暂无",0,"","","","");
        Tb_houses houses1=new Tb_houses(2+"","测试房"+2,"孙超超","","江西师大","28.689342;116.036151","10","12345678",
                "暂无",0,"","","","");
        Tb_houses houses2=new Tb_houses(3+"","测试房"+3,"孙超超","","江西师大","28.692342;116.039151","10","12345678",
                "暂无",0,"","","","");
housesDAO.add(houses);
housesDAO.add(houses1);
housesDAO.add(houses2);
*/
/*===============================================================================================================*/
    }

    private void initLocation() {
        //定位参数对象
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
//        option.setNeedDeviceDirect(true); // 返回的定位结果包含手机机头的方向
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);

        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {

            }


            public void onMapStatusChangeStart(MapStatus mapStatus, int i) {

            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {


            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                mBaiduMap.clear();//如果不设置这个的话会因为地图状态改变而多次设置重复的maker，这时你会看到你的maker颜色越来越重，最后导致程序运行慢，OOM。
                setMarker();
                int zoom = (int) mapStatus.zoom;//这个捏就是地图缩放级别的参数
                LatLng latLng = mapStatus.target;//这个捏就是中心点的坐标参数了
                lati=latLng.latitude;
                loni=latLng.longitude;
                setMakerPool(latLng);
            }
        });


    }

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());

            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());// 单位度
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());// 位置语义化信息
            List<Poi> list = location.getPoiList();// POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            lat = location.getLatitude();
            lon = location.getLongitude();
            //这个判断是为了防止每次定位都重新设置中心点和marker
            if (isFirstLoc) {
                isFirstLoc = false;
                setMarker();
                setUserMapCenter();
            }

        }
    }

    public void onBackPressed() {//返回键事件
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        imagetx=(ImageView) findViewById(R.id.imageView);
        level=(TextView) findViewById(R.id.level);
        name=(TextView) findViewById(R.id.name);
        name.setText(phone);
        handler1 = new MyHandler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String path = url + "/personal_1.jsp?phonenum="+phone;
                    //2:把网址封装为一个URL对象
                    URL url = new URL(path);
                    //3:获取客户端和服务器的连接对象，此时还没有建立连接
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    //4:初始化连接对象
                    conn.setRequestMethod("GET");
                    //设置连接超时
                    conn.setConnectTimeout(8000);
                    //设置读取超时
                    conn.setReadTimeout(8000);
                    //5:发生请求，与服务器建立连接
                    conn.connect();
                    //如果响应码为200，说明请求成功
                    if (conn.getResponseCode() == 200) {
                        //获取服务器响应头中的流
                        InputStreamReader is = new InputStreamReader(conn.getInputStream());
                        BufferedReader buffer=new BufferedReader(is);
                        String retData ="";
                        String responseData = "";
                       /* while ((retData = buffer.readLine()) !="") {
                            responseData += retData;
                        }*/
                        responseData=buffer.readLine();
                        txurl=buffer.readLine();
                        is.close();
                        Message msg = new Message();
                        msg.arg1= Integer.valueOf(responseData).intValue();
                        msg.what=1;
                        handler1.sendMessage(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String path = url  + txurl;
                    //2:把网址封装为一个URL对象
                    URL url = new URL(path);
                    //3:获取客户端和服务器的连接对象，此时还没有建立连接
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    //4:初始化连接对象
                    conn.setRequestMethod("GET");
                    //设置连接超时
                    conn.setConnectTimeout(8000);
                    //设置读取超时
                    conn.setReadTimeout(8000);
                    //5:发生请求，与服务器建立连接
                    conn.connect();
                    //如果响应码为200，说明请求成功
                    if (conn.getResponseCode() == 200) {
                        //获取服务器响应头中的流
                        InputStream is = conn.getInputStream();
                        //读取流里的数据，构建成bitmap位图
                        Bitmap bm = BitmapFactory.decodeStream(is);
                        Message msg = new Message();
                        msg.obj = bm;
                        msg.what=2;
                        handler1.sendMessage(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();



        /*if(cursor!=null&&!cursor.isAfterLast()) {
            int sta = cursor.getInt(cursor.getColumnIndex("permission"));
            if (sta == 0)
                level.setText("未认证");
            else
                level.setText("已认证");
            String st = cursor.getString(cursor.getColumnIndex("touxiang"));
            if (st != null) {
                Bitmap bitmap = BitmapFactory.decodeFile(st);
                imagetx.setImageBitmap(bitmap);
            }
        }*/
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {///右上角单击
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(map.this, searchhome.class);  //进入主界面
            startActivity(intent);  //开始跳转
            return true;
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        }
        if (id == R.id.action_setting2) {
            Intent intent = new Intent(map.this, IpQH.class);  //进入主界面
            startActivity(intent);  //开始跳转
            return true;
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override

    public boolean onNavigationItemSelected(MenuItem item) {/////////菜单栏单击
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.mr_ts) {
            Intent intent=new Intent(map.this,History.class);

            startActivity(intent);
            //住房记录
        } else if (id == R.id.mr_sp) {

            resetsp();
            Intent intent=new Intent(map.this, Login_password.class);
            startActivity(intent);
            map.this.finish();
            //退出

        }
         else if (id == R.id.mr_ds) {
            //检查更新
            AppUpDataManager manager = new AppUpDataManager(this);
            manager.findVersion(url+"/file/app/");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void resetsp() {
        OkHttpUtils.post()
                .url(url+"/servlet/OutServlet")
                .addParams("phonenumber",phone)
                .build()
                .execute(new MyCall2());
        sp= PreferenceManager.getDefaultSharedPreferences(this);
        editor=sp.edit();
        editor.putBoolean("main",false);
        editor.apply();
    }

    private class MyCall2 extends StringCallback {

        @Override

        public void onError(Call call, Exception e, int id)

        {

            Log.e(Tag, "onError"+e.getMessage());
            e.printStackTrace();


        }


        public void onResponse(String response, int id)
        { Log.e(Tag,"onresponse="+response);
            if("success".equals(response)){
                Toast.makeText(map.this,"成功退出",Toast.LENGTH_SHORT).show();
            }
        }
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        //退出时销毁定位
        mLocationClient.stop();
        mBaiduMap.setMyLocationEnabled(false);         //关闭定位图层
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
    public void refrestx(){
        Glide.with(map.this)
                .load(data.imageurl)
                .into(imagetx);

    }

    public void setMakerPool( LatLng la) {
        double lat1 = la.latitude + 0.1;
        double lat2 = la.latitude - 0.1;
        double log1 = la.longitude + 0.1;
        double log2 = la.longitude - 0.1;
        //图片自己弄
       // http://192.168.1.104:8080/xiangmu/servlet/MapServlet

        /*HousesDAO housesDAO = new HousesDAO(this);
        List<Tb_houses> housesList = housesDAO.getScrollData(0, (int) housesDAO.getCount());
        //a从数据库中获得的house列表
        //b是house实体类*/
        String path = url + "/servlet/MapServlet";
        OkHttpUtils.post().
                url(path).
                addParams("nud", "1")
                .build()
                .execute(new MyCall1());
    }


/*
    public void run() {
        try {
            String path = url + "/xiangmu/servlet/MapServlet";
            //2:把网址封装为一个URL对象
            URL url = new URL(path);
            //3:获取客户端和服务器的连接对象，此时还没有建立连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //4:初始化连接对象
            conn.setRequestMethod("POST");
            //设置连接超时
            conn.setConnectTimeout(8000);
            //设置读取超时
            conn.setReadTimeout(8000);
            //5:发生请求，与服务器建立连接
            conn.connect();
            //如果响应码为200，说明请求成功
            if (conn.getResponseCode() == 200) {
                //获取服务器响应头中的流
                InputStreamReader is = new InputStreamReader(conn.getInputStream());
                BufferedReader buffer=new BufferedReader(is);
                String retData ="";
                String responseData = "";
                while ((retData = buffer.readLine()) !="") {
                    responseData += retData;
                }
                is.close();
                Message msg = new Message();
                msg.obj=responseData;
                msg.what=1;
                handler1.sendMessage(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        }).start();
｝
*/














    /*       for (int i = 0; i < housesList.size(); i++) {
               Tb_houses m = housesList.get(i);
               LatLng latlng = new LatLng(Double.parseDouble(m.getHouse_position().substring(0, m.getHouse_position().indexOf(';'))),
                       Double.parseDouble(m.getHouse_position().substring(m.getHouse_position().indexOf(';') + 1, m.getHouse_position().length())));
               if (latlng.latitude > lat2 && latlng.latitude < lat1 && latlng.longitude > log2
                       && latlng.longitude < log1) {
   //这里往下的是为了将maker的其他数据传递到地图上，为了后边点击maker显示详细信息，这里首先得给maker的实体类序列化
                   Bundle bundle = new Bundle();
                   bundle.putString("m", m.getHouse_ID());
                   OverlayOptions oo = new MarkerOptions().position(latlng).icon(bitmap).period(6)
                           .extraInfo(bundle);
                   mBaiduMap.addOverlay(oo);


               }

           }


       }  */        /*地图相关*/
    public void gerenzhongxin(View view){
        Intent intent = new Intent(com.example.asus.qmt5.Map.map.this,personalcenter.class);
        startActivity(intent);   //开始跳转
    }


    private class MyCall1 extends StringCallback {
        @Override
        public void onError(Call call, Exception e, int id) {
            e.printStackTrace();
        }

        @Override
        public void onResponse(String response, int id) {

            response=response.substring(9);
            response=response.substring(0,response.length()-1);
            getListHouseByArray(response);
        }
    List houseList;
    public List<HouseInfo> getListHouseByArray(String response) {
        houseList = new ArrayList<HouseInfo>();
        try {
            JSONArray jsonArray = new JSONArray(response);
            Log.e("ceshi", jsonArray.length() + "长度");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                String house_ID = jsonObject.optString("house_ID");
                String house_name = jsonObject.optString("house_name");
                String house_addr = jsonObject.optString("house_addr");
                String house_price = jsonObject.optString("house_price");
                String house_position = jsonObject.optString("house_position");
                String house_state = jsonObject.optString("house_state");
                String house_intorduce = jsonObject.optString("house_introduce");
                String house_img = jsonObject.optString("house_img");
                houseList.add(new HouseInfo(house_ID, house_name, house_addr, house_position, house_price, house_state, house_intorduce, house_img));

            }

            double lat1 = lati + 0.1;
            double lat2 = lati - 0.1;
            double log1 = loni + 0.1;
            double log2 = loni - 0.1;
            BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.fangwu);
            ;
            for (int i = 0; i < houseList.size(); i++) {
                HouseInfo m = (HouseInfo) houseList.get(i);
                LatLng latlng = new LatLng(Double.parseDouble(m.getHouse_position().substring(0, m.getHouse_position().indexOf(';'))),
                        Double.parseDouble(m.getHouse_position().substring(m.getHouse_position().indexOf(';') + 1, m.getHouse_position().length())));
                if (latlng.latitude > lat2 && latlng.latitude < lat1 && latlng.longitude > log2
                        && latlng.longitude < log1) {
//这里往下的是为了将maker的其他数据传递到地图上，为了后边点击maker显示详细信息，这里首先得给maker的实体类序列化
                    Bundle bundle = new Bundle();
                    bundle.putString("m", m.getHouse_ID());
                    OverlayOptions oo = new MarkerOptions().position(latlng).icon(bitmap).period(6)
                            .extraInfo(bundle);
                    mBaiduMap.addOverlay(oo);
                }


            }

            return houseList;

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }

        return houseList;
    }

    }
}
