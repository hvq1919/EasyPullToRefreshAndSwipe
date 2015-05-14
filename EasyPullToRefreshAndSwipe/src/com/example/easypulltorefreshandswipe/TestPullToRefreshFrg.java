package com.example.easypulltorefreshandswipe;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import base.BasePullSwipe;

import com.example.easypulltorefreshandswipe.TestPullToRefreshFrg.GenData.OnSuccessData;
import com.example.easypulltorefreshandswipe.adapter.TestPullSwipeAdapter;
import com.example.easypulltorefreshandswipe.adapter.TestPullSwipeObject;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.handmark.pulltorefresh.library.PullAndLoadSwipeListView;

public class TestPullToRefreshFrg extends BasePullSwipe<TestPullSwipeAdapter> {

	private TestPullSwipeAdapter mPullSwipeAdapter;

	private View mRootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.frg_test_pull_to_refresh, container, false);

		init();
		return mRootView;
	}

	private void init() {
		mPullSwipeAdapter = new TestPullSwipeAdapter(new ArrayList<TestPullSwipeObject>(), getActivity());

	}

//	@Override
//	protected boolean noSwipe() {
//		//return true;
//		return false;
//	}
	
	@Override
	protected int swipeMode() {
		return SwipeListView.SWIPE_MODE_RIGHT;
		//return SwipeListView.SWIPE_MODE_LEFT;
	}
	
	
	@Override
	protected View setEmptyView() {
		/* You must set visibility = gone for this view*/
		View emptyView = getActivity().getLayoutInflater().inflate(R.layout.empty_listview, null);

		return emptyView;
	}

	@Override
	protected void getData(int pageNumber) {
		new GenData(getActivity(),new OnSuccessData() {
			@Override
			public void OnDone(ArrayList<TestPullSwipeObject> swipeObjects) {
				mPullSwipeAdapter.addData(swipeObjects);
				RefreshComplete();
			}
		}).execute();

	}

	@Override
	protected PullAndLoadSwipeListView setViewPullSwipe() {
		return (PullAndLoadSwipeListView) mRootView.findViewById(R.id.lstView);
	}

	@Override
	protected TestPullSwipeAdapter setAdapter() {
		return mPullSwipeAdapter;
	}

	@Override
	protected int getTotalPadding() {
		int totalPadding = 2 * (int) getResources().getDimension(R.dimen.container_padding);
		return totalPadding;
	}

	@Override
	protected int getRemoveWidth() {
		int valueInPixels = (int) getResources().getDimension(R.dimen.width_back_view);
		return valueInPixels;
	}

	@Override
	protected void clickOnBackView(TestPullSwipeAdapter adapter, int position, View view) {
		Toast.makeText(getActivity(), "Clicked on BackView", Toast.LENGTH_SHORT).show();
		adapter.removeItem(position-1);
	}

	@Override
	protected void clickOnFrontView(TestPullSwipeAdapter adapter, int position, View view) {
		Toast.makeText(getActivity(),((TestPullSwipeObject)adapter.getItem(position-1)).getName(), Toast.LENGTH_SHORT).show();
	}

	// ///////// TODO TEST data //////////////////

	public static class GenData extends AsyncTask<Void, Void, Void> {
		public interface OnSuccessData {
			public void OnDone(ArrayList<TestPullSwipeObject> swipeObjects);
		}

		
		
		public OnSuccessData mOnSuccessData;

		private ArrayList<TestPullSwipeObject> mObjects;

		private ProgressDialog pDialog;

		public GenData(Context mContext,OnSuccessData mOnSuccessData) {
			super();
			this.mOnSuccessData = mOnSuccessData;
			mObjects = new ArrayList<TestPullSwipeObject>();
			
			pDialog = new ProgressDialog(mContext);
			pDialog.setMessage("Loading...");
			pDialog.setIndeterminate(false);
			pDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {

			for (int i = 0; i < 10; i++) {
				TestPullSwipeObject object = new TestPullSwipeObject();
				object.setId(i);
				object.setName("Name " + i);
				object.setSub("Sub " + i);
				mObjects.add(object);

			}

			try {
				new Thread().sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			
			pDialog.dismiss();
			
			if (mOnSuccessData != null)
				mOnSuccessData.OnDone(mObjects);
		}

	}

}
