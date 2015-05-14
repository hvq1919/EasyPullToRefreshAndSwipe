package com.example.easypulltorefreshandswipe.adapter;

import java.util.ArrayList;

import com.fortysevendeg.swipelistview.SwipeListView;
import com.fortysevendeg.swipelistview.SwipeListViewAdapter;

public abstract class BaseSwipeListViewAdapter<E> extends SwipeListViewAdapter {
	private ArrayList<E> mData;

	public BaseSwipeListViewAdapter(ArrayList<E> mData) {
		super();
		this.mData = mData;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public E getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	public void removeItem(int pos) {
		mData.remove(pos);
		notifyDataSetChanged();
	}

	public ArrayList<E> getData() {
		return mData;
	}

	public void addAll(ArrayList<E> list) {
		mData.clear();
		if (list != null)
			mData.addAll(list);
		notifyDataSetChanged();
	}

	public void addData(ArrayList<E> list) {
		if (list != null)
			mData.addAll(list);
		notifyDataSetChanged();
	}

	public void clearData() {
		mData.clear();
		notifyDataSetChanged();
	}

	@Override
	public int getItemRestrictType(int position) {
		return SwipeListView.ALLOW_SWIPE;
	}

}
