package com.example.asus.qmt5.Update;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.TextView;
import org.json.JSONObject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by lgy on 17/4/20.
 */

public class AppUpDataManager {

    private Context context;
    private String appName;
    private boolean canle = false;
    File file = null;
    String urlPath;
    AlertDialog dialog;


    private String getVersionName(Context context) throws Exception
    {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(),0);
        String version = packInfo.versionName;
        return version;
    }


    public AppUpDataManager(Context context) {
        super();
        this.context = context;
    }

    public void findVersion(String urlPathLink){
        urlPath = urlPathLink;
        new Thread(){
            public void run(){
                if(isNeedUpData(urlPath+"version.txt")){
                    sendHandlerMessage(0,null);
                }
            }
        }.start();
    }
    private   void sendHandlerMessage(int what,Object object){
        Message message = new Message();
        message.what = what;
        message.obj = object;
        handler.sendMessage(message);
    }
    /**
     * Creted by lgy on 17/4/21.
     */
    private  MyHandler handler = new MyHandler(this);
    private final class MyHandler extends Handler {
        private final WeakReference<AppUpDataManager> weakReference1;
        public MyHandler(AppUpDataManager manager){
            weakReference1 = new WeakReference<AppUpDataManager>(manager);
        }

        @Override
        public void handleMessage(Message msg) {
            final AppUpDataManager manager = weakReference1.get();
            if(manager != null){
                switch (msg.what){
                    case 0:
                        new AlertDialog.Builder(manager.context).setTitle("更新提示").
                                setMessage("发现新版本，是否需要跟新？").setPositiveButton("更新",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                downLoadFile(urlPath);
                                showAlertDialog(manager.context);
                                canle = false;
                            }
                        }).setNegativeButton("取消",null).create().show();
                        break;
                    case 1:
                        sAlertDialogMessage(msg.obj.toString());
                        break;
                    case 2:
                        dialog.dismiss();
                        installApk(manager.context);
                        break;
                }
            }
        }
    }

    public Boolean isNeedUpData(String url){
        boolean flag = false;
        OkHttpClient client = new OkHttpClient();
        File file = null;
        try {
            Request request = new Request.Builder().url(url).build();
            String response = client.newCall(request).execute().body().string();
            JSONObject jsonObject;
            jsonObject = new JSONObject(response);
            Double thisVersion = Double.valueOf(getVersionName(context));
            int version = Integer.valueOf(jsonObject.getString("version"));
            if (version > thisVersion) {
                flag = true;
                appName = jsonObject.getString("appName");
            }
        }catch (Exception e){
            Log.e("isNeedUpData",e.getMessage());
        }
        return flag;
    }

    //下载apk程序代码
    protected void downLoadFile(final String urlLink) {
        new Thread(){
            public void run(){
                OkHttpClient client = new OkHttpClient();
                // 判断SD卡是否存在，并且是否具有读写权限
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    // 获得存储卡的路径
                    String sdpath = Environment.getExternalStorageDirectory() + "/update";
                    File tmpFile = new File(sdpath);
                    if (!tmpFile.exists()) {
                        tmpFile.mkdir();
                    }
                    try {
                        URL url = new URL(urlLink+appName);
                        file = new File(sdpath + appName);
                        try {
                            HttpURLConnection conn = (HttpURLConnection) url
                                    .openConnection();
                            conn.connect();
                            // 获取文件大小
                            int length = conn.getContentLength();
                            InputStream is = conn.getInputStream();
                            FileOutputStream fos = new FileOutputStream(file);
                            byte[] buf = new byte[1024];
                            int count = 0;
                            if (is != null) {
                                int numRead = 0;
                                do{
                                    numRead = is.read(buf);
                                    count += numRead;
                                    if (numRead <= 0) {
                                        break;
                                    } else {
                                        fos.write(buf, 0, numRead);
                                    }
                                    sendHandlerMessage(1,"正在下载更新包，已下载"+count*100/length+"%");
                                }while (canle == false);
                            }
                            fos.close();
                            is.close();
                            sendHandlerMessage(2,"更新成功！");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }


                }
            }
        }.start();
    }
    /**
     * 安装APK文件
     */
    private void installApk(Context context)
    {
        // 通过Intent安装APK文件
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + file.toString()), "application/vnd.android.package-archive");
        context.startActivity(i);
    }

    TextView megView;
    private void showAlertDialog(Context context){
        megView = new TextView(context);
        megView.setTextSize(18);
        megView.setPadding(50,5,10,5);
        megView.setText("正在下载更新包，已下载0%");
        AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle("正在下载").setView(megView).setPositiveButton("取消更新",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                canle = true;
            }
        });
        dialog = builder.create();
        dialog.show();
    }
    private  void sAlertDialogMessage(String msg){
        megView.setText(msg);
    }

}