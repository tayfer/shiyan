/***********************************************************************
 * 版权所有(C) 上海科拉迪电子科技有限公司 2014-2023
 * Copyright 2014-2023 KELADI TECHNOLOGY CO..
 *  
 * This software is the confidential and proprietary information of
 * KELADI Corporation ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with KELADI.
 ***********************************************************************/

package com.shiyan.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shiyan.R;
import com.shiyan.adapter.TopNewAdapter;
import com.shiyan.adapter.ViewPagerAdapter;
import com.shiyan.services.InformationService;
import com.shiyan.vo.NewItem;

/*******************************************
 * @COMPANY:上海科拉迪电子科技有限公司
 * @CLASS:TopNewFragment
 * @DESCRIPTION:<p class="detail"></p>
 * @AUTHOR:<a href="mailto:tuojin@coo-k.com ">T</a>
 * @VERSION:v1.0
 * @DATE:2015年4月27日 -下午4:31:58
 *******************************************/

public class TopNewFragment extends Fragment {

	private Context context;

	private PullToRefreshListView pullToRefreshListView;

	private ViewPager viewPager;

	private List<View> mListViews;

	private ViewPagerAdapter vAdapter;

	private List<NewItem> newItems;

	private TopNewAdapter topnewadapter;

	private TextView imgtext;

	private ListView listView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_topnew, null);
		pullToRefreshListView = (PullToRefreshListView) view
				.findViewById(R.id.news_list);
		this.context = getActivity();
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initListView();
	}

	private void initListView() {
		View headerView = LayoutInflater.from(context).inflate(
				R.layout.head_view, null);
		viewPager = (ViewPager) headerView.findViewById(R.id.news_list_header);
		imgtext = (TextView) headerView.findViewById(R.id.img_info);
		mListViews = new ArrayList<View>();
		vAdapter = new ViewPagerAdapter(mListViews);
		viewPager.setAdapter(vAdapter);
		new LoadNewsPicTask().execute(1);
		// 当滑动时,切换图片说明文字
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				imgtext.setText(mListViews.get(arg0).getTag().toString());
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
		pullToRefreshListView.setMode(Mode.BOTH);
		listView = pullToRefreshListView.getRefreshableView();
		listView.addHeaderView(headerView);

		newItems = new ArrayList<NewItem>();
		topnewadapter = new TopNewAdapter(context, newItems);
		pullToRefreshListView.setAdapter(topnewadapter);
		new LoadNewsFooterAsyncTask().execute(1);
		pullToRefreshListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// 点击头部、底部栏无效
						if (position == 0)
							return;
						// 跳转到新闻详情
						Toast.makeText(context,
								"跳转到id为 " + position + " 的信息详情", 2000);
					}
				});
		pullToRefreshListView
				.setOnRefreshListener(new OnRefreshListener<ListView>() {

					@Override
					public void onRefresh(
							PullToRefreshBase<ListView> refreshView) {
						new LoadNewsFooterAsyncTask().execute(1);
					}

				});
	}

	private class LoadNewsFooterAsyncTask extends
			AsyncTask<Object, Integer, List<NewItem>> {

		@Override
		protected List<NewItem> doInBackground(Object... params) {
			return new InformationService().getTopNews();
		}

		@Override
		protected void onPostExecute(List<NewItem> result) {
			super.onPostExecute(result);
			topnewadapter.notifyDataSetChanged();
			topnewadapter.addNewItems(result);
			pullToRefreshListView.onRefreshComplete();
		}
	}

	private class LoadNewsPicTask extends
			AsyncTask<Object, Integer, List<NewItem>> {

		@Override
		protected List<NewItem> doInBackground(Object... params) {
			return new InformationService().getTopNewPics();
		}

		@Override
		protected void onPostExecute(List<NewItem> result) {
			super.onPostExecute(result);
			for (int i = 0; i < result.size(); i++) {
				ImageView imageView = new ImageView(context);
				String imgurl = result.get(i).getImageUrl();
				ImageLoader.getInstance().displayImage(imgurl, imageView);
				imageView.setScaleType(ScaleType.FIT_XY);
				imageView.setTag(result.get(i).getText());
				mListViews.add(imageView);
			}
			vAdapter = new ViewPagerAdapter(mListViews);
			viewPager.setAdapter(vAdapter);
		}
	}

}
