package com.shiyan.task;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;

import com.shiyan.APPConstant;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class DownloadHandler {
	private HttpParams httpParams;

	public DownloadHandler() {
		this.httpParams = new BasicHttpParams();
		// 设置连接超时和 Socket 超时，以及 Socket 缓存大小
		HttpConnectionParams.setConnectionTimeout(httpParams, 20 * 1000);
		HttpConnectionParams.setSoTimeout(httpParams, 20 * 1000);
		HttpConnectionParams.setSocketBufferSize(httpParams, 8192);
		// 设置重定向，缺省为 true
		HttpClientParams.setRedirecting(httpParams, true);
		// 设置 user agent
		HttpProtocolParams.setUserAgent(httpParams,
				APPConstant.USER_AGENT_CHROME);
	}

	@SuppressLint("HandlerLeak")
	public void Down(final String path, final DownLoadCallback callback) {
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				byte[] result = (byte[]) msg.obj;
				callback.download(result);
			}
		};
		class MyThread implements Runnable {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpClient client = new DefaultHttpClient(httpParams);
				HttpGet httpGet = new HttpGet(path);
				try {
					HttpResponse httpResponse = client.execute(httpGet);
					Log.i("TAG", "------>"
							+ httpResponse.getStatusLine().getStatusCode());
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						byte[] result = EntityUtils.toByteArray(httpResponse
								.getEntity());
						Message message = Message.obtain();
						message.obj = result;
						message.what = 1;
						handler.sendMessage(message);
					}
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					if (client != null) {
						client.getConnectionManager().shutdown();
					}
				}
			};
		}
		new Thread(new MyThread()).start();
	}

	public interface DownLoadCallback {
		public void download(byte[] data);
	}

}