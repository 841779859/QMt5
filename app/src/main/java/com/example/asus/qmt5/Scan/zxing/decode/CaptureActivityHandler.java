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

package com.example.asus.qmt5.Scan.zxing.decode;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


import com.example.asus.qmt5.R;
import com.example.asus.qmt5.Scan.zxing.app.CaptureActivity;
import com.example.asus.qmt5.Scan.zxing.camera.CameraManager;
import com.example.asus.qmt5.Scan.zxing.view.ViewfinderResultPointCallback;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.Vector;


/**
 * This class handles all the messaging which comprises the state machine for
 * activity_qrcode_capture_layout.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
/**
 * This class handles all the messaging which comprises the state machine for capture.
 * 二维码扫描操作和扫描界面进行消息通知的处理类
 */

/**
 *    CaptureActivityHandler类继承自Handler类，用于线程间异步消息通信，在该类中启动
 *  了二维码识别编码线程DecodeThread、相机获取焦点及相机预览回调的一个循环过程，直至返回
 *  二维码识别的结果。当某一次识别失败后，则会启动相机预览，并将从相机预览中获取的扫描框
 *  中的图片数据传递至识别线程。 
 *  */
public final class CaptureActivityHandler extends Handler {

    private static final String TAG = CaptureActivityHandler.class.getSimpleName();

    private final CaptureActivity activity;
    /**二维码识别线程*/
    private final DecodeThread decodeThread;
    /**二维码扫描及识别状态*/
    private State state;

    private enum State {
        PREVIEW, SUCCESS, DONE
    }

    public CaptureActivityHandler(CaptureActivity activity, Vector<BarcodeFormat> decodeFormats, String characterSet) {
        this.activity = activity;
        decodeThread = new DecodeThread(activity, decodeFormats, characterSet, new ViewfinderResultPointCallback(
                activity.getViewfinderView()));
        decodeThread.start();
        state = State.SUCCESS;

        // Start ourselves capturing previews and decoding.
        CameraManager.get().startPreview();
        restartPreviewAndDecode();
    }

    @Override
    public void handleMessage(Message message) {
        if (message.what == R.id.auto_focus) {
            // Log.d(TAG, "Got auto-focus message");
            // When one auto focus pass finishes, start another. This is the
            // closest thing to
            // continuous AF. It does seem to hunt a bit, but I'm not sure what
            // else to do.
            if (state == State.PREVIEW) {
                CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
            }
        } else if (message.what == R.id.restart_preview) {
            Log.d(TAG, "Got restart preview message");
            restartPreviewAndDecode();
        } else if (message.what == R.id.decode_succeeded) {
            Log.d(TAG, "Got decode succeeded message");
            state = State.SUCCESS;
            Bundle bundle = message.getData();
            Bitmap barcode = bundle == null ? null : (Bitmap) bundle.getParcelable(DecodeThread.BARCODE_BITMAP);//二维码图片
            activity.handleDecode((Result) message.obj, barcode);//返回结果
            Result result = (Result) message.obj;
            Intent mIntent = new Intent();
            mIntent.putExtra("SCAN_RESULT", result.getText());
            activity.setResult(Activity.RESULT_OK, mIntent);
            activity.finish();
        } else if (message.what == R.id.decode_failed) {
            // We're decoding as fast as possible, so when one decode fails,
            // start another.
            state = State.PREVIEW;
            CameraManager.get().requestPreviewFrame(decodeThread.getHandler(), R.id.decode);
        } else if (message.what == R.id.return_scan_result) {
            Log.d(TAG, "Got return scan result message");
            activity.setResult(Activity.RESULT_OK, (Intent) message.obj);
            activity.finish();
        } else if (message.what == R.id.launch_product_query) {
            Log.d(TAG, "Got product query message");
            String url = (String) message.obj;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            activity.startActivity(intent);
        }
    }

    public void quitSynchronously() {
        state = State.DONE;
        CameraManager.get().stopPreview();
        Message quit = Message.obtain(decodeThread.getHandler(), R.id.quit);
        quit.sendToTarget();
        try {
            decodeThread.join();
        } catch (InterruptedException e) {
            // continue
        }

        // Be absolutely sure we don't send any queued up messages
        removeMessages(R.id.decode_succeeded);
        removeMessages(R.id.decode_failed);
    }

    private void restartPreviewAndDecode() {
        if (state == State.SUCCESS) {
            state = State.PREVIEW;
            CameraManager.get().requestPreviewFrame(decodeThread.getHandler(), R.id.decode);
            CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
            activity.drawViewfinder();
        }
    }

}
