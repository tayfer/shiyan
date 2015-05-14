package com.shiyan.app;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.shiyan.APPConstant;
import com.shiyan.task.DownloadHandler;
import com.shiyan.task.DownloadHandler.DownLoadCallback;

public class ShiYanAPP extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		initImageLoader(getApplicationContext());
		initSourceHtml();
	}

	public void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				// .writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);

	}

	public void initSourceHtml() {
		new DownloadHandler().Down(APPConstant.TARGETURL,
				new DownLoadCallback() {

					@Override
					public void download(byte[] data) {
						FileOutputStream fileOutputStream = null;
						InputStream is = null;
						try {
							if (data != null) {
								is = new ByteArrayInputStream(data);
								File file = new File(APPConstant.HTMLSOURCEPAHT);
								if (!file.exists()) {
									file.getParentFile().mkdirs();
									file.createNewFile();
								}
								fileOutputStream = new FileOutputStream(file);
								byte[] buf = new byte[1024];
								int ch = -1;
								while ((ch = is.read(buf)) != -1) {
									fileOutputStream.write(buf, 0, ch);
								}
							}
						} catch (Exception e) {
							Log.e("ShiYanAPP", e.getMessage());
						}
					}
				});
	}
}
