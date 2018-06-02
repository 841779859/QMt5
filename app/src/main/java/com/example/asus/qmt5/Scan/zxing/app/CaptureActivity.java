/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.asus.qmt5.Scan.zxing.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.asus.qmt5.R;
import com.example.asus.qmt5.Scan.Handinput;
import com.example.asus.qmt5.Scan.zxing.camera.CameraManager;
import com.example.asus.qmt5.Scan.zxing.decode.BeepManager;
import com.example.asus.qmt5.Scan.zxing.decode.CaptureActivityHandler;
import com.example.asus.qmt5.Scan.zxing.decode.DecodeFormatManager;
import com.example.asus.qmt5.Scan.zxing.decode.FinishListener;
import com.example.asus.qmt5.Scan.zxing.decode.InactivityTimer;
import com.example.asus.qmt5.Scan.zxing.decode.Intents;
import com.example.asus.qmt5.Scan.zxing.decode.RGBLuminanceSource;
import com.example.asus.qmt5.Scan.zxing.util.Util;
import com.example.asus.qmt5.Scan.zxing.view.ViewfinderView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import java.io.IOException;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.Vector;



/**
 * The barcode reader activity itself. This is loosely based on the
 * CameraPreview example included in the Android SDK.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 */
//zxing的主活动实现surfaceHolder.Callback实现初始化摄像头和绘制工作
/**二维码扫描及识别消息处理类*/

/**
 * 实现了手机摄像头Camera、预览界面等初始化的任务，另外，创建了一个InactivityTimer类，
 * 在该类中启动了一个后台线程，用于监控扫描二维码的过程最多为5*60秒，在5*30秒后，该类
 * 中创建的定时任务执行，结束该界面。
 *
 *
 * 同时，CaptureActivityHandler类则是维系整个扫
 * 描过程的处理类。
 * */
public final class CaptureActivity extends Activity implements SurfaceHolder.Callback, View.OnClickListener {

    private static final String TAG = CaptureActivity.class.getSimpleName();//定义Tag标记

    private static final long INTENT_RESULT_DURATION = 1500L;
    private static final long BULK_MODE_SCAN_DELAY_MS = 1000L;
    private static final String PRODUCT_SEARCH_URL_PREFIX = "http://www.google";
    private static final String PRODUCT_SEARCH_URL_SUFFIX = "/m/products/scan";
    private static final String ZXING_URL = "http://zxing.appspot.com/scan";
    private static final String RETURN_URL_PARAM = "ret";//返回的url参数

    private static final Set<ResultMetadataType> DISPLAYABLE_METADATA_TYPES;//可显示的元数据类型
   // public static final int RESULT_CODE_ENCODE = 200;
    public static final int RESULT_CODE_DECODE = 300;//结果代码解码

    //    public static final String EXTRA_DATA = "result";
    public static final String EXTRA_DATA = "SCAN_RESULT";

    static {
        DISPLAYABLE_METADATA_TYPES = new HashSet<ResultMetadataType>(5);
        DISPLAYABLE_METADATA_TYPES.add(ResultMetadataType.ISSUE_NUMBER);
        DISPLAYABLE_METADATA_TYPES.add(ResultMetadataType.SUGGESTED_PRICE);
        DISPLAYABLE_METADATA_TYPES.add(ResultMetadataType.ERROR_CORRECTION_LEVEL);
        DISPLAYABLE_METADATA_TYPES.add(ResultMetadataType.POSSIBLE_COUNTRY);
    }

    private ImageView btnBack;
   // private TextView btnPhoto;
    private TextView btnFlash;
   // private TextView btnEncode;
    private TextView handinput;
    private boolean isFlash;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_qrcode_back://返回按钮
                finish();
                break;
           /* case R.id.qrcode_btn_photo:
                //跳转到系统相册选择图片
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                Intent wrapperIntent = Intent.createChooser(intent, "选择二维码图片");
                startActivityForResult(wrapperIntent, REQUEST_CODE);
                break;
*/
            case R.id.qrcode_handinput:
                Intent intent=new Intent(CaptureActivity.this,Handinput.class);
                startActivity(intent);
                break;
            case R.id.qrcode_btn_flash://闪光灯 调用CameraManager类中的打开和关闭手电筒的方法
                if (isFlash) {
                    //关闭闪光灯
                    CameraManager.get().turnLightOff();
                    isFlash=false;
                } else {
                    //开启闪光灯
                    CameraManager.get().turnLightOn();
                    isFlash=true;
                }
                break;
        /*    case R.id.qrcode_btn_encode:
                // 跳转到生成二维码页面
                Bitmap b = createQRCode();
                Intent intentEncode = getIntent();
                intentEncode.putExtra(EXTRA_DATA, b);
//                intentEncode.putExtra("QR_CODE", b);
                setResult(RESULT_CODE_ENCODE, intentEncode);
                finish();
                break;
                */
            default:
                break;
        }
    }

    private enum Source {
        NATIVE_APP_INTENT, PRODUCT_SEARCH_LINK, ZXING_LINK, NONE
    }

    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;

    private Result lastResult;
    private boolean hasSurface;
    private Source source;
    private String sourceUrl;
    private String returnUrlTemplate;
    /**二维码编码格式*/
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    /**用于创建一个后台线程，并监控二维码扫描时间最多为5*60秒*/
    private InactivityTimer inactivityTimer;
    /**
     * 声音显示**/
    private BeepManager beepManager;

   // private int REQUEST_CODE = 3;

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    @Override
    public void onCreate(Bundle icicle) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(icicle);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_qrcode_capture_layout);

        Util.currentActivity = this;
        CameraManager.init(getApplication());
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);

        btnBack = ((ImageView) findViewById(R.id.iv_qrcode_back));
        //   btnPhoto = ((TextView) findViewById(R.id.qrcode_btn_photo));
        btnFlash = ((TextView) findViewById(R.id.qrcode_btn_flash));
        handinput=(TextView) findViewById(R.id.qrcode_handinput);
        // btnEncode = ((TextView) findViewById(R.id.qrcode_btn_encode));
        btnBack.setOnClickListener(this);
        //btnPhoto.setOnClickListener(this);
        btnFlash.setOnClickListener(this);
        //btnEncode.setOnClickListener(this);
        handinput.setOnClickListener(this);
        surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        handler = null;
        lastResult = null;
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this);

        // showHelpOnFirstLaunch();
    }

    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;


    @SuppressWarnings("deprecation")
    @Override
    protected void onResume() {
        super.onResume();
        resetStatusView();
        if (hasSurface) {
            // The activity was paused but not stopped, so the surface still
            // exists. Therefore
            // surfaceCreated() won't be called, so init the camera here.
            initCamera(surfaceHolder);
        } else {
            // Install the callback and wait for surfaceCreated() to init the
            // camera.
            Log.e("CaptureActivity", "onResume");
        }

        Intent intent = getIntent();
        String action = intent == null ? null : intent.getAction();
        String dataString = intent == null ? null : intent.getDataString();
        if (intent != null && action != null) {
            if (action.equals(Intents.Scan.ACTION)) {
                // Scan the formats the intent requested, and return the result
                // to the calling activity.
                source = Source.NATIVE_APP_INTENT;
                decodeFormats = DecodeFormatManager.parseDecodeFormats(intent);
            } else if (dataString != null && dataString.contains(PRODUCT_SEARCH_URL_PREFIX)
                    && dataString.contains(PRODUCT_SEARCH_URL_SUFFIX)) {
                // Scan only products and send the result to mobile Product
                // Search.
                source = Source.PRODUCT_SEARCH_LINK;
                sourceUrl = dataString;
                decodeFormats = DecodeFormatManager.PRODUCT_FORMATS;
            } else if (dataString != null && dataString.startsWith(ZXING_URL)) {
                // Scan formats requested in query string (all formats if none
                // specified).
                // If a return URL is specified, send the results there.
                // Otherwise, handle it ourselves.
                source = Source.ZXING_LINK;
                sourceUrl = dataString;
                Uri inputUri = Uri.parse(sourceUrl);
                returnUrlTemplate = inputUri.getQueryParameter(RETURN_URL_PARAM);
                decodeFormats = DecodeFormatManager.parseDecodeFormats(inputUri);
            } else {
                // Scan all formats and handle the results ourselves (launched
                // from Home).
                source = Source.NONE;
                decodeFormats = null;
            }
            characterSet = intent.getStringExtra(Intents.Scan.CHARACTER_SET);
        } else {
            source = Source.NONE;
            decodeFormats = null;
            characterSet = null;
        }
        beepManager.updatePrefs();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (source == Source.NATIVE_APP_INTENT) {
                setResult(RESULT_CANCELED);
                finish();
                return true;
            } else if ((source == Source.NONE || source == Source.ZXING_LINK) && lastResult != null) {
                resetStatusView();
                if (handler != null) {
                    handler.sendEmptyMessage(R.id.restart_preview);
                }
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_FOCUS || keyCode == KeyEvent.KEYCODE_CAMERA) {
            // Handle these events so they don't launch the Camera app
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    /**
     * A valid barcode has been found, so give an indication of success and show
     * the results.
     *
     * @param rawResult The contents of the barcode.
     * @param barcode   A greyscale bitmap of the camera data which was decoded.
     */
    /**
     * 返回二维码识别结果及二维码图片的接口
     * @param //result
     * @param barcode
     */
    public void handleDecode(Result rawResult, Bitmap barcode) {
        inactivityTimer.onActivity();
        lastResult = rawResult;
        if (barcode == null) {
            // This is from history -- no saved barcode
            handleDecodeInternally(rawResult, null);
        } else {
            beepManager.playBeepSoundAndVibrate();
            drawResultPoints(barcode, rawResult);
            switch (source) {
                case NATIVE_APP_INTENT:
                case PRODUCT_SEARCH_LINK:
                    handleDecodeExternally(rawResult, barcode);
                    break;
                case ZXING_LINK:
                    if (returnUrlTemplate == null) {
                        handleDecodeInternally(rawResult, barcode);
                    } else {
                        handleDecodeExternally(rawResult, barcode);
                    }
                    break;
                case NONE:
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                    if (prefs.getBoolean(PreferencesActivity.KEY_BULK_MODE, false)) {
                        // Wait a moment or else it will scan the same barcode
                        // continuously about 3 times
                        if (handler != null) {
                            handler.sendEmptyMessageDelayed(R.id.restart_preview, BULK_MODE_SCAN_DELAY_MS);
                        }
                        resetStatusView();
                    } else {
                        handleDecodeInternally(rawResult, barcode);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Superimpose a line for 1D or dots for 2D to highlight the key features of
     * the barcode.
     *
     * @param barcode   A bitmap of the captured image.
     * @param rawResult The decoded results which contains the points to draw.
     */
    private void drawResultPoints(Bitmap barcode, Result rawResult) {
        ResultPoint[] points = rawResult.getResultPoints();
        if (points != null && points.length > 0) {
            Canvas canvas = new Canvas(barcode);
            Paint paint = new Paint();
            paint.setColor(getResources().getColor(R.color.result_image_border));
            paint.setStrokeWidth(3.0f);
            paint.setStyle(Paint.Style.STROKE);
            Rect border = new Rect(2, 2, barcode.getWidth() - 2, barcode.getHeight() - 2);
            canvas.drawRect(border, paint);

            paint.setColor(getResources().getColor(R.color.result_points));
            if (points.length == 2) {
                paint.setStrokeWidth(4.0f);
                drawLine(canvas, paint, points[0], points[1]);
            } else if (points.length == 4 && (rawResult.getBarcodeFormat().equals(BarcodeFormat.UPC_A))
                    || (rawResult.getBarcodeFormat().equals(BarcodeFormat.EAN_13))) {
                // Hacky special case -- draw two lines, for the barcode and
                // metadata
                drawLine(canvas, paint, points[0], points[1]);
                drawLine(canvas, paint, points[2], points[3]);
            } else {
                paint.setStrokeWidth(10.0f);
                for (ResultPoint point : points) {
                    canvas.drawPoint(point.getX(), point.getY(), paint);
                }
            }
        }
    }

    private static void drawLine(Canvas canvas, Paint paint, ResultPoint a, ResultPoint b) {
        canvas.drawLine(a.getX(), a.getY(), b.getX(), b.getY(), paint);
    }

    // Put up our own UI for how to handle the decoded contents.
    @SuppressWarnings("unchecked")
    private void handleDecodeInternally(Result rawResult, Bitmap barcode) {
        viewfinderView.setVisibility(View.GONE);

        Map<ResultMetadataType, Object> metadata = (Map<ResultMetadataType, Object>) rawResult.getResultMetadata();
        if (metadata != null) {
            StringBuilder metadataText = new StringBuilder(20);
            for (Map.Entry<ResultMetadataType, Object> entry : metadata.entrySet()) {
                if (DISPLAYABLE_METADATA_TYPES.contains(entry.getKey())) {
                    metadataText.append(entry.getValue()).append('\n');
                }
            }
            if (metadataText.length() > 0) {
                metadataText.setLength(metadataText.length() - 1);
            }
        }
    }

    // Briefly show the contents of the barcode, then handle the result outside
    // Barcode Scanner.
    private void handleDecodeExternally(Result rawResult, Bitmap barcode) {
        viewfinderView.drawResultBitmap(barcode);

        // Since this message will only be shown for a second, just tell the
        // user what kind of
        // barcode was found (e.g. contact info) rather than the full contents,
        // which they won't
        // have time to read.

        if (source == Source.NATIVE_APP_INTENT) {
            // Hand back whatever action they requested - this can be changed to
            // Intents.Scan.ACTION when
            // the deprecated intent is retired.
            Intent intent = new Intent(getIntent().getAction());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            intent.putExtra(Intents.Scan.RESULT, rawResult.toString());
            intent.putExtra(Intents.Scan.RESULT_FORMAT, rawResult.getBarcodeFormat().toString());
            Message message = Message.obtain(handler, R.id.return_scan_result);
            message.obj = intent;
            handler.sendMessageDelayed(message, INTENT_RESULT_DURATION);
        } else if (source == Source.PRODUCT_SEARCH_LINK) {
            // Reformulate the URL which triggered us into a query, so that the
            // request goes to the same
            // TLD as the scan URL.
            Message message = Message.obtain(handler, R.id.launch_product_query);
            // message.obj = sourceUrl.substring(0, end) + "?q=" +
            // resultHandler.getDisplayContents().toString()
            // + "&source=zxing";
            handler.sendMessageDelayed(message, INTENT_RESULT_DURATION);
        } else if (source == Source.ZXING_LINK) {
            // Replace each occurrence of RETURN_CODE_PLACEHOLDER in the
            // returnUrlTemplate
            // with the scanned code. This allows both queries and REST-style
            // URLs to work.
            Message message = Message.obtain(handler, R.id.launch_product_query);
            // message.obj = returnUrlTemplate.replace(RETURN_CODE_PLACEHOLDER,
            // resultHandler.getDisplayContents()
            // .toString());
            handler.sendMessageDelayed(message, INTENT_RESULT_DURATION);
        }
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            /**
             * use a CameraManager to manager the camera's life cycles
             */
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            displayFrameworkBugMessageAndExit();
            return;
        } catch (RuntimeException e) {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
            Log.e(TAG, "Unexpected error initializating camera", e);
            displayFrameworkBugMessageAndExit();
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
        }
    }

    private void displayFrameworkBugMessageAndExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.app_name));
        builder.setOnCancelListener(new FinishListener(this));
        builder.show();
    }

    private void resetStatusView() {
        viewfinderView.setVisibility(View.VISIBLE);
        lastResult = null;
    }
    /**刷新扫描界面*/
    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

    private Uri uri;
//解析扫描结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //返回选择的需要扫描二维码的图片
        if (resultCode == RESULT_OK) {
            //被选择的二维码图片的uri
            uri = data.getData();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //扫描
                    Result result = scanningImage(uri);
                    if (result == null) {
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(), "图片格式有误", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    } else {
                        // 数据返回，在这里去处理扫码结果
                        String recode = (result.toString());
                        Intent data = new Intent();
                        data.putExtra(EXTRA_DATA, recode);
                        setResult(RESULT_CODE_DECODE, data);
                        finish();
                    }
                }
            }).start();
        }
    }

    protected Result scanningImage(Uri path) {
        if (path == null || "".equals(path)) {
            return null;
        }
        // DecodeHintType 和EncodeHintType
        Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
        hints.put(DecodeHintType.CHARACTER_SET, "utf-8"); // 设置二维码内容的编码
        try {
            Bitmap scanBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));

            RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap);
            BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
            QRCodeReader reader = new QRCodeReader();
            return reader.decode(bitmap1, hints);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
