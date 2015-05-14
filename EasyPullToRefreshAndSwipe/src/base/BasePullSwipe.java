package base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.fortysevendeg.swipelistview.SwipeListViewAdapter;
import com.handmark.pulltorefresh.library.PullAndLoadSwipeListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.nexle.android_libs.utils.DeviceUtils;

public abstract class BasePullSwipe<E extends SwipeListViewAdapter> extends Fragment implements
		OnRefreshListener<SwipeListView> {

	/** The pull and load swipe listview */
	protected PullAndLoadSwipeListView lViewViewedMe;
	private E mViewedMeAdapter;
	private SwipeListView lViewViewedMeAble;

	private int currentPageNumber = 1;
	/* The total size from server */
	private int totalSize = 0;

	// //////////////////////////////////
	// ----------- Abstract Method -----

	/** The view will be displayed when no data return */
	protected abstract View setEmptyView();

	/** To get data from server , You put statements call api in here */
	protected abstract void getData(int pageNumber);

	/**
	 * Init the pull and load swipe listview (lViewViewedMe) in here
	 */
	protected abstract PullAndLoadSwipeListView setViewPullSwipe();

	/** The adapter of listview */
	protected abstract E setAdapter();

	/** Handle action when click on back view on each item of listview */
	protected abstract void clickOnBackView(E adapter, int position, View view);

	/** Handle action when click on front view on each item of listview */
	protected abstract void clickOnFrontView(E adapter, int position, View view);

	public void setAdapter(E adapter) {
		mViewedMeAdapter = adapter;
		lViewViewedMeAble.setAdapter(mViewedMeAdapter);
	}

	/* set OffsetLeft when swiping */
	protected abstract int getRemoveWidth();

	/* set OffsetLeft when swiping */
	protected abstract int getTotalPadding();

	/**
	 * To set for the listview can swipe or not
	 * 
	 * true if Listview can swipe and no for otherwise
	 * */
	protected boolean noSwipe() {
		return false;
	}

	protected int swipeMode() {
		return SwipeListView.SWIPE_MODE_RIGHT;
	}

	/**
	 * To complete PullToRefresh
	 */
	protected void RefreshComplete() {
		if (lViewViewedMe.isRefreshing())
			lViewViewedMe.onRefreshComplete();
	}

	@Override
	public void onRefresh(PullToRefreshBase<SwipeListView> refreshView) {
		currentPageNumber = 1;
		getData(currentPageNumber);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initObject();
	}

	private void initObject() {
		lViewViewedMe = setViewPullSwipe();
		lViewViewedMeAble = lViewViewedMe.getRefreshableView();

		if (setEmptyView() != null) {
			((ViewGroup) lViewViewedMeAble.getParent()).addView(setEmptyView());
			lViewViewedMeAble.setEmptyView(setEmptyView());
		}

		/* init adapter in the first time focus */
		// if (mViewedMeAdapter == null) {
		mViewedMeAdapter = setAdapter();
		// new AdapterLists(mActivity, R.layout.item_member, new
		// ArrayList<MemberInfo>(), textRemove, isInbox);
		getData(currentPageNumber = 1);
		// }

		lViewViewedMe.setOnRefreshListener(this);
		lViewViewedMeAble.setSwipeListViewListener(swipeListListener);
		lViewViewedMeAble.setAdapter(mViewedMeAdapter);
		lViewViewedMeAble.setmBIsRestrictedByOffset(true);
		lViewViewedMeAble.setSwipeOpenOnLongPress(false);
		lViewViewedMeAble.setSwipeCloseAllItemsWhenMoveList(true);

		lViewViewedMeAble.setSwipeActionLeft(SwipeListView.SWIPE_ACTION_REVEAL);
		lViewViewedMeAble.setSwipeActionRight(SwipeListView.SWIPE_ACTION_REVEAL);
		lViewViewedMeAble.setSwipeMode(swipeMode());

		if (swipeMode() == SwipeListView.SWIPE_MODE_RIGHT) {

			if (noSwipe())
				lViewViewedMeAble.setOffsetRight(DeviceUtils.getScreenSize(getActivity()).x - getTotalPadding());
			else
				lViewViewedMeAble.setOffsetRight(DeviceUtils.getScreenSize(getActivity()).x - getRemoveWidth()
						- getTotalPadding());

			lViewViewedMeAble.setOffsetLeft(DeviceUtils.getScreenSize(getActivity()).x - getTotalPadding());

		} else {
			if (noSwipe())
				lViewViewedMeAble.setOffsetLeft(DeviceUtils.getScreenSize(getActivity()).x - getTotalPadding());
			else
				lViewViewedMeAble.setOffsetLeft(DeviceUtils.getScreenSize(getActivity()).x - getRemoveWidth()
						- getTotalPadding());

			lViewViewedMeAble.setOffsetRight(DeviceUtils.getScreenSize(getActivity()).x - getTotalPadding());

		}

		lViewViewedMeAble.setOnScrollListener(onScrollListener);

	}

	private OnScrollListener onScrollListener = new OnScrollListener() {
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			int count = lViewViewedMeAble.getCount();
			if (scrollState == SCROLL_STATE_IDLE) {
				if (/*
					 * totalSize > mViewedMeAdapter.getCount() && (use when
					 * server return total size)
					 */lViewViewedMeAble.getLastVisiblePosition() >= count - 1) {
					Log.d("TAG", "Load More");
					currentPageNumber += 1;
					getData(currentPageNumber);
				}
			}
			// else clickedFrontView = false;
			Log.d("TAG", "onScrollStateChanged: scrollState" + scrollState);
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			/* has header in list */
			try {
				lViewViewedMeAble.closeOpenedItems();
			} catch (Exception e) {
			}
			// lViewViewedMeAble.setM_nScrollState(SwipeListView.SCROLL_STATE_NORMAL);

			// Logger.showLogD("onScroll");
		}
	};

	private BaseSwipeListViewListener swipeListListener = new BaseSwipeListViewListener() {

		@Override
		public void onOpened(int position, boolean toRight) {
			// clickedFrontView = false;

			lViewViewedMeAble.setM_nScrollState(SwipeListView.SCROLL_STATE_NORMAL);
			if ((swipeMode() == SwipeListView.SWIPE_MODE_RIGHT && toRight)
					|| (swipeMode() == SwipeListView.SWIPE_MODE_LEFT && !toRight)) {
				try {
					lViewViewedMeAble.closeOpenedItems(position);
				} catch (Exception e) {
				}
			}
		}

		@Override
		public void onClosed(View view, int position, boolean toRight) {
			// if(clickedFrontView){
			// clickedFrontView = false;
			// clickOnFrontView(mViewedMeAdapter.getData(), position);
			// }

			// lViewViewedMeAble
			// .setM_nScrollState(SwipeListView.SCROLL_STATE_NORMAL);
		}

		@Override
		public void onStartOpen(int position, int action, boolean right) {
			// clickedFrontView = false;

			// lViewViewedMeAble
			// .setM_nScrollState(SwipeListView.SCROLL_STATE_NORMAL);

		}

		@Override
		public void onClickFrontView(View view, int position) {
			// lViewViewedMeAble
			// .setM_nScrollState(SwipeListView.SCROLL_STATE_NORMAL);

			// clickedFrontView = false;
			clickOnFrontView(mViewedMeAdapter, position, view);

		}

		@Override
		public void onClickBackView(int position, View view) {
			super.onClickBackView(position, view);

			// TODO ca
			if (position > 0)
				clickOnBackView(mViewedMeAdapter, position, view);
		}
	};

	/**
	 * Shouldn't hide keyboard in pullswipe
	 */
	protected boolean shouldHideKB() {
		return false;
	};
}
