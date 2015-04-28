package com.shiyan.activity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ActionBar;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.shiyan.R;
import com.shiyan.adapter.NavDrawerListAdapter;
import com.shiyan.fragment.BetterLifeFragment;
import com.shiyan.fragment.MemberFragment;
import com.shiyan.fragment.TopNewFragment;
import com.shiyan.fragment.TrafficFragment;
import com.shiyan.fragment.WeatherFragment;
import com.shiyan.util.DeviceInfo;
import com.shiyan.vo.NavDrawerItem;

public class ContentActivity extends FragmentActivity {
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private NavDrawerListAdapter adapter;
    private ListView mDrawerList;
    private int curItem;
    private String title = "最美十堰";
    private ArrayList<CloseSoftKeyboardListener> myListeners = new ArrayList<CloseSoftKeyboardListener>();
    private String[] navMenuTitles;
    private final String TAG = ContentActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_content);

	setActionBarStyle();// 设置actionbar

	mDrawerLayout = (DrawerLayout) findViewById(R.id.content_drawer);
	mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
		GravityCompat.START);

	mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
		R.string.drawer_open, R.string.drawer_close) {
	    public void onDrawerClosed(View view) {
		invalidateOptionsMenu();
	    }

	    public void onDrawerOpened(View drawerView) {
		invalidateOptionsMenu();
		closeSoftKeyboard();
	    }

	};// 设置抽屉开关

	mDrawerLayout.setDrawerListener(mDrawerToggle);
	initLiftView();// 初始化列表数据
	displayView(0);// 自动初始化第一页数据
    }

    private void initLiftView() {
	mDrawerList = (ListView) findViewById(R.id.left_drawer);
	mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
	adapter = new NavDrawerListAdapter(getApplicationContext(), getData());
	View headView = LayoutInflater.from(this).inflate(R.layout.head_view,
		null);
	mDrawerList.addHeaderView(headView);// 为listview添加头部view
	mDrawerList.setAdapter(adapter);

    }

    /**
     * Slide menu item click listener
     * */
    private class SlideMenuClickListener implements
	    ListView.OnItemClickListener {
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
		long id) {
	    if (position == curItem)
		return;
	    displayView(position);
	}
    }

    private void displayView(int position) {
	// update the main content by replacing fragments
	Fragment fragment = null;
	if (!DeviceInfo.isNetworkAvailable(this)) {
	    Toast.makeText(this, R.string.error_msg, Toast.LENGTH_SHORT).show();
	}
	switch (position) {
	case 0:
	    curItem = 0;
	    title = navMenuTitles[position];
	    fragment = new TopNewFragment();
	    break;
	case 1:
	    curItem = 1;
	    title = navMenuTitles[position];
	    fragment = new BetterLifeFragment();
	    break;
	case 2:
	    curItem = 2;
	    fragment = new TrafficFragment();
	    title = navMenuTitles[position];
	    break;
	case 3:
	    curItem = 3;
	    fragment = new WeatherFragment();
	    title = navMenuTitles[position];
	    break;
	case 4:
	    curItem = 4;
	    fragment = new MemberFragment();
	    title = navMenuTitles[position];
	    break;
	default:
	    break;
	}

	if (fragment != null) {
	    FragmentManager fragmentManager = getSupportFragmentManager();
	    fragmentManager.beginTransaction()
		    .replace(R.id.frame_container, fragment)
		    .commitAllowingStateLoss();
	}
	// update selected item and title, then close the drawer
	mDrawerList.setItemChecked(position, true);
	mDrawerList.setSelection(position);
	mDrawerLayout.closeDrawer(mDrawerList);
	this.getActionBar().setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	getMenuInflater().inflate(R.menu.main, menu);
	return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
	boolean isDrawerOper = mDrawerLayout.isDrawerOpen(mDrawerList);
	if (isDrawerOper) {
	    this.getActionBar().setTitle("最美十堰");
	} else {
	    this.getActionBar().setTitle(title);
	}
	return super.onPrepareOptionsMenu(menu);
    }

    private ArrayList<NavDrawerItem> getData() {
	ArrayList<NavDrawerItem> navDrawerItems = new ArrayList<NavDrawerItem>();
	TypedArray navMenuIcons = getResources().obtainTypedArray(
		R.array.nav_drawer_icons);
	navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

	navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons
		.getResourceId(0, -1)));
	navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons
		.getResourceId(1, -1)));
	navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons
		.getResourceId(2, -1)));
	navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons
		.getResourceId(3, -1)));
	navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons
		.getResourceId(4, -1)));
	navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons
		.getResourceId(5, -1)));
	// Recycle the typed array
	navMenuIcons.recycle();

	return navDrawerItems;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
	super.onPostCreate(savedInstanceState);
	mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
	super.onConfigurationChanged(newConfig);
	// Pass any configuration change to the drawer toggls
	mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void setActionBarStyle() {
	ActionBar actionBar = getActionBar();
	Drawable actionBarDrawable = this.getBaseContext().getResources()
		.getDrawable(R.drawable.action_bar_background);
	actionBar.setBackgroundDrawable(actionBarDrawable);
	// getActionBar().setIcon(R.color.transparent);
	int titleId = Resources.getSystem().getIdentifier("action_bar_title",
		"id", "android");
	TextView textView = (TextView) findViewById(titleId);
	textView.setTextColor(0xFFdfdfdf);
	textView.setTextSize(17);
	getActionBar().setDisplayHomeAsUpEnabled(true);
	getActionBar().setHomeButtonEnabled(true);
    }

    /**
     * 连续按两次返回键就退出
     */
    private int keyBackClickCount = 0;

    @Override
    protected void onResume() {
	super.onResume();
	keyBackClickCount = 0;
    }

    public void onPause() {
	super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
	// TODO Auto-generated method stub

	if (keyCode == KeyEvent.KEYCODE_BACK) {
	    switch (keyBackClickCount++) {
	    case 0:
		Toast.makeText(this,
			getResources().getString(R.string.press_again_exit),
			Toast.LENGTH_SHORT).show();
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
		    @Override
		    public void run() {
			keyBackClickCount = 0;
		    }
		}, 3000);
		break;
	    case 1:
		this.finish();
		break;
	    default:
		break;
	    }
	    return true;
	} else if (keyCode == KeyEvent.KEYCODE_MENU) {
	    // if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
	    // mDrawerLayout.closeDrawer(mDrawerList);
	    // } else {
	    // mDrawerLayout.openDrawer(mDrawerList);
	    // }
	}
	return super.onKeyDown(keyCode, event);
    }

    /*------------  侧边栏打开后 关闭所有的软键盘  ------------*/

    public interface CloseSoftKeyboardListener {
	public void onCloseListener();
    }

    public void registerMyTouchListener(CloseSoftKeyboardListener listener) {
	myListeners.add(listener);
    }

    public void unRegisterMyTouchListener(CloseSoftKeyboardListener listener) {
	myListeners.remove(listener);
    }

    public void closeSoftKeyboard() {
	for (CloseSoftKeyboardListener listener : myListeners) {
	    listener.onCloseListener();
	}
    }
}
