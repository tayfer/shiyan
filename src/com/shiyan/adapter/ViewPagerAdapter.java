package com.shiyan.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class ViewPagerAdapter extends PagerAdapter {
	private List<View> mListViews;

	public ViewPagerAdapter(List<View> mListViews) {
		this.mListViews = mListViews;
	}

	@Override
	public void destroyItem(View v, int position, Object object) {
		((ViewPager) v).removeView(mListViews.get(position));
	}

	@Override
	public Object instantiateItem(View v, int position) {
		View view = mListViews.get(position);
		((ViewPager) v).addView(view);
		return view;
	}

	@Override
	public int getCount() {
		return mListViews.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	public void addView(List<View> views) {
		mListViews.addAll(views);
	}
}