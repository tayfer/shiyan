package com.shiyan.adapter;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shiyan.R;
import com.shiyan.vo.NewItem;

public class TopNewAdapter extends BaseAdapter {

	private List<NewItem> newitems;
	private Context context;

	public TopNewAdapter(Context ctx) {
		this.context = ctx;
	}

	public TopNewAdapter(Context ctx, List<NewItem> nis) {
		this.context = ctx;
		this.newitems = nis;
	}

	@Override
	public int getCount() {
		return newitems.size();
	}

	@Override
	public Object getItem(int position) {
		return newitems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		NewItem newItem = newitems.get(position);
		View view = LayoutInflater.from(context).inflate(
				R.layout.fragment_topnew_list, null);
		TextView textview = (TextView) view.findViewById(R.id.topnews_text);
		textview.setText(newItem.getText());
		view.setTag(position);
		return view;
	}

	public void addNewItems(List<NewItem> newItems2) {
		newitems.addAll(newItems2);
	}
}
