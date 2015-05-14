package com.example.easypulltorefreshandswipe.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.easypulltorefreshandswipe.R;

public class TestPullSwipeAdapter extends BaseSwipeListViewAdapter<TestPullSwipeObject> {
	private Context mContext;
	private ArrayList<TestPullSwipeObject> mDatas;
	private LayoutInflater mInflater;

	public TestPullSwipeAdapter(ArrayList<TestPullSwipeObject> mData, Context mContext) {
		super(mData);
		this.mContext = mContext;
		this.mDatas = mData;
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	static class ViewHolder {
		TextView name;
		TextView sub;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.pull_swipe_row, parent, false);
			holder = new ViewHolder();

			holder.name = (TextView) convertView.findViewById(R.id.value);
			holder.sub = (TextView) convertView.findViewById(R.id.sub);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		TestPullSwipeObject baseHouseList = mDatas.get(position);

		holder.name.setText(baseHouseList.getName());
		holder.sub.setText(baseHouseList.getSub());

		return convertView;
	}

}
