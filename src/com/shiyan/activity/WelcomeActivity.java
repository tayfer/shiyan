package com.shiyan.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * 
 * ClassName: WelcomeActivity <br/>
 * Function: 首页面 <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2015年4月27日 下午3:25:32 <br/>
 *
 * @author T
 * @version
 * @since JDK 1.7
 */
public class WelcomeActivity extends Activity {

    private static final int GO_HOME = 100;
    private static final int GO_GUIDE = 200;
    boolean isFirst = false;
    private Handler mHandler = new Handler() {
	@Override
	public void handleMessage(Message msg) {
	    switch (msg.what) {
	    case GO_HOME:
		goHome();
		break;
	    case GO_GUIDE:
		goGuide();
		break;
	    }
	}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	init();
    }

    private void init() {
	SharedPreferences preferences = getSharedPreferences("first_pref",
		MODE_PRIVATE);
	isFirst = preferences.getBoolean("isFirst", true);
	if (!isFirst) {
	    mHandler.sendEmptyMessageDelayed(GO_HOME, 2800);
	} else {
	    mHandler.sendEmptyMessageDelayed(GO_GUIDE, 2800);
	}
    }

    private void goHome() {
	Intent intent = new Intent(WelcomeActivity.this, ContentActivity.class);
	startActivity(intent);
	overridePendingTransition(android.R.anim.fade_in,
		android.R.anim.fade_out);
	this.finish();
    }

    private void goGuide() {
	Intent intent = new Intent(WelcomeActivity.this, GuideActivity.class);
	startActivity(intent);
	overridePendingTransition(android.R.anim.fade_in,
		android.R.anim.fade_out);
	this.finish();
    }
}
