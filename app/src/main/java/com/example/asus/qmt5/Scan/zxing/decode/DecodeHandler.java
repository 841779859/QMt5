/*
 * Copyright (C) 2010 ZXing authors
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

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;


import com.example.asus.qmt5.R;
import com.example.asus.qmt5.Scan.zxing.app.CaptureActivity;
import com.example.asus.qmt5.Scan.zxing.camera.CameraManager;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.util.Hashtable;
/**
 * 扫描过程处理类
 */

/**
 * DecodeHandler类也继承于Handler，并是在DecodeThread线程类中创建的，
 * 则这个二维码识别过程应该是运行于子线程的，该类主要用于处理二维码识别
 * 并与CaptureActivityHandler类协作，来维护整个二维码的扫描及识别过
 * 程能够不断地进行，直至二维码识别成功。decode方法则是二维码识别的方法，
 * 从该方法中可以看出，获取识别结果后，将通过CaptureActivityHandler
 * 类发送Message,将扫描结果反馈给CaptureActivityHandler的handleMessage
 * 消息处理方法中。
 * */
final class DecodeHandler extends Handler
{

	private static final String TAG = DecodeHandler.class.getSimpleName();

	private final CaptureActivity activity;
	/**二维码数据格式封装的类*/
	private final MultiFormatReader multiFormatReader;

	DecodeHandler(CaptureActivity activity, Hashtable<DecodeHintType, Object> hints)
	{
		multiFormatReader = new MultiFormatReader();
		multiFormatReader.setHints(hints);
		this.activity = activity;
	}

	@Override
	public void handleMessage(Message message)
	{
		if (message.what == R.id.decode)
		{
			// Log.d(TAG, "Got decode message");
			decode((byte[]) message.obj, message.arg1, message.arg2);
		}
		else if (message.what == R.id.quit)
		{
			Looper.myLooper().quit();
		}
	}

	/**
	 * Decode the data within the viewfinder rectangle, and time how long it took. For efficiency,
	 * reuse the same reader objects from one decode to the next.
	 *
	 * @param data   The YUV preview frame.
	 * @param width  The width of the preview frame.
	 * @param height The height of the preview frame.
	 */
	private void decode(byte[] data, int width, int height)
	{
		long start = System.currentTimeMillis();
		Result rawResult = null;
		/***********************�޸�Ϊ������ʼ******************************/
		byte[] rotatedData = new byte[data.length];
		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
				rotatedData[x * height + height - y - 1] = data[x + y * width];
		}
		int tmp = width; // Here we are swapping, that's the difference to #11
		width = height;
		height = tmp;
		data = rotatedData;
		/***********************�޸�Ϊ��������******************************/
		PlanarYUVLuminanceSource source = CameraManager.get().buildLuminanceSource(data, width, height);
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
		try
		{
			rawResult = multiFormatReader.decodeWithState(bitmap);
		}
		catch (ReaderException re)
		{
			// continue
		}
		finally
		{
			multiFormatReader.reset();
		}

		if (rawResult != null)
		{
			long end = System.currentTimeMillis();
			Log.d(TAG, "Found barcode (" + (end - start) + " ms):\n" + rawResult.toString());
			Message message = Message.obtain(activity.getHandler(), R.id.decode_succeeded, rawResult);
			Bundle bundle = new Bundle();
			bundle.putParcelable(DecodeThread.BARCODE_BITMAP, source.renderCroppedGreyscaleBitmap());
			message.setData(bundle);
			// Log.d(TAG, "Sending decode succeeded message...");
			message.sendToTarget();
		}
		else
		{
			Message message = Message.obtain(activity.getHandler(), R.id.decode_failed);
			message.sendToTarget();
		}
	}

}
