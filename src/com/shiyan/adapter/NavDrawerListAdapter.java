package com.shiyan.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shiyan.R;
import com.shiyan.vo.NavDrawerItem;

/**
 * 
 * ClassName: NavDrawerListAdapter <br/>
 * Function:adapter <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2015年4月27日 下午4:00:52 <br/>
 *
 * @author T
 * @version
 * @since JDK 1.7
 */
public class NavDrawerListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<NavDrawerItem> navDrawerItems;

    public NavDrawerListAdapter(Context context,
	    ArrayList<NavDrawerItem> navDrawerItems) {
	this.context = context;
	this.navDrawerItems = navDrawerItems;
    }

    @Override
    public int getCount() {
	return navDrawerItems.size();
    }

    @Override
    public Object getItem(int position) {
	return navDrawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
	return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	if (convertView == null) {
	    LayoutInflater mInflater = (LayoutInflater) context
		    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    convertView = mInflater.inflate(R.layout.behind_list_show, null);
	}

	if ((position == 0 || position == 3)) {
	    convertView.setVisibility(View.GONE);
	} else {
	    convertView.setVisibility(View.VISIBLE);
	}

	ImageView imgIcon = (ImageView) convertView
		.findViewById(R.id.imageview_behind_icon);
	TextView txtTitle = (TextView) convertView
		.findViewById(R.id.textview_behind_title);

	imgIcon.setImageResource(navDrawerItems.get(position).getIcon());
	txtTitle.setText(navDrawerItems.get(position).getTitle());

	return convertView;
    }

}
