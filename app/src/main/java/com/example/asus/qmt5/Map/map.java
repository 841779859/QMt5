package com.example.asus.qmt5.Map;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.asus.qmt5.R;

import java.util.ArrayList;
import java.util.List;

public class map extends AppCompatActivity {

    /*地图相关*/
    public static ImageView imagetx;
    private TextView level;
    private TextView name;
    private double lat;
    private double lon;
    public String city;
    public TextView cityname;
    private DrawerLayout drawerLayout;
    private MapView mMapView = null;    //定义地图控件
    private BaiduMap mBaiduMap;         //定义百度地图对象
    boolean isFirstLoc = true;         //是否首次定位
    public LocationClient mLocationClient = null;   //定位服务
    public BDLocationListener myListener = new MyLocationListener();    //创建监听器
    public Button btn_location;               //定义按钮
    private String permissionInfo;
    private final int SDK_PERMISSION_REQUEST = 127;
    public Cursor cursor;
    private RelativeLayout rl_marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationClient = new LocationClient(getApplicationContext());  //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);          //注册监听函数
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        mLocationClient.start();
        //setTitle(Map1Activity.this,com.example.cc.ditu2.R.layout.activity_map1);
        setContentView(R.layout.activity_map);
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
                .fromResource(R.drawable.snowman);
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
        //Log.v("pcw","setUserMapCenter : lat : "+ lat+" lon : " + lon);
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

        /*LatLng convertLatLng = new LatLng(lat, lon);
        mBaiduMap.setMyLocationEnabled(false);
        OverlayOptions ooA=new MarkerOptions().position(convertLatLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.startcenterpoint)).zIndex(9)
                .draggable(true);
        final Marker marker=(Marker) mBaiduMap.addOverlay(ooA);
        MapStatusUpdate u=MapStatusUpdateFactory.newLatLngZoom(convertLatLng,17.0f);
        mBaiduMap.animateMapStatus(u);*/
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
                setMakerPool(latLng);
            }
        });

        /*mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public boolean onMapPoiClick(MapPoi arg0) {
                return false;
            }
            @Override
            public void onMapClick(LatLng arg0) {
                rl_marker.setVisibility(View.GONE);
            }
        });
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }
        });*/

    }

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            /*// map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null)
                return;
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);    //设置定位数据
            if (isFirstLoc) {
                //设置定位的坐标
                LatLng ll = new LatLng(location.getLatitude(),location.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, 19);    //设置地图中心点以及缩放级别
                mBaiduMap.animateMapStatus(u);
                isFirstLoc = false;         //设置false  防止无法移动地图
            }*/

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

    public void setMakerPool(final LatLng la) {
        double lat1 = la.latitude + 0.1;
        double lat2 = la.latitude - 0.1;
        double log1 = la.longitude + 0.1;
        double log2 = la.longitude - 0.1;
        //图片自己弄
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.fangwu);

     /*   HousesDAO housesDAO = new HousesDAO(this);
        List<Tb_houses> housesList = housesDAO.getScrollData(0, (int) housesDAO.getCount());
        //a从数据库中获得的house列表
        //b是house实体类
        for (int i = 0; i < housesList.size(); i++) {
            Tb_houses m = housesList.get(i);
            LatLng latlng = new LatLng(Double.parseDouble(m.getHouse_position().substring(0, m.getHouse_position().indexOf(';'))),
                    Double.parseDouble(m.getHouse_position().substring(m.getHouse_position().indexOf(';') + 1, m.getHouse_position().length())));
            if (latlng.latitude > lat2 && latlng.latitude < lat1 && latlng.longitude > log2
                    && latlng.longitude < log1) {
//这里往下的是为了将maker的其他数据传递到地图上，为了后边点击maker显示详细信息，这里首先得给maker的实体类序列化
                Bundle bundle = new Bundle();
                bundle.putString("m", m.getHouse_ID());*/
        LatLng latlng = new LatLng(28.689342,116.036151);
        Bundle bundle = new Bundle();
        bundle.putString("m", "1");
        OverlayOptions oo = new MarkerOptions().position(latlng).icon(bitmap).period(6)
                .extraInfo(bundle);
        mBaiduMap.addOverlay(oo);
    }
}
