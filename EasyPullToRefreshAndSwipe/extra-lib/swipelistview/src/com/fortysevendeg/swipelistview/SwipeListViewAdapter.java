package com.fortysevendeg.swipelistview;

import android.widget.BaseAdapter;
import android.widget.ListAdapter;

public abstract class SwipeListViewAdapter extends BaseAdapter implements ListAdapter {
	public abstract int getItemRestrictType(int position);
}
