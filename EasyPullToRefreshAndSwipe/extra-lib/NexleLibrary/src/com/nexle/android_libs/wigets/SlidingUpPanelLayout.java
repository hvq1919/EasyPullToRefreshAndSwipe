package com.nexle.android_libs.wigets;

import com.nexle.android_libs.R;
import com.nexle.android_libs.utils.DeviceUtils;
import com.nexle.android_libs.utils.UiUtil;
import com.nexle.android_libs.utils.springsystem.Spring;
import com.nexle.android_libs.utils.springsystem.SpringListener;
import com.nexle.android_libs.utils.springsystem.SpringSystem;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * The SlidingUpPanelLayout provides a reusable view for live-editing all
 * registered springs within an Application. Each registered Spring can be
 * accessed by its id and its tension and friction properties can be edited
 * while the user tests the effected UI live.
 */
public class SlidingUpPanelLayout extends FrameLayout {
	public interface PanelSlideListener {
		public void onPanelOpened(int height);

		public void onPanelClosed(int height);
	}

	private final int mHandleId;
	private final int mContentId;
	private final float mRatio;

	private View mHandle;
	private View mContent;

	private final Spring mRevealerSpring;
	private float mStashPx = 0;
	private float mRevealPx = 0;
	private int mHeight = 0;
	private PanelSlideListener mListener;

	public SlidingUpPanelLayout(Context context) {
		this(context, null);
	}

	public SlidingUpPanelLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public SlidingUpPanelLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SlidingUpPanelLayout, 0, 0);
		mHandleId = a.getResourceId(R.styleable.SlidingUpPanelLayout_handle, 0);
		if (mHandleId == 0)
			throw new IllegalArgumentException("The handle attribute is required and must refer " + "to a valid child.");

		mContentId = a.getResourceId(R.styleable.SlidingUpPanelLayout_content, 0);
		if (mContentId == 0)
			throw new IllegalArgumentException("The content attribute is required and must refer to a valid child.");

		mRatio = a.getFloat(R.styleable.SlidingUpPanelLayout_expandHeight, 0);
		if (mRatio < 0 || mRatio > 1)
			throw new IllegalArgumentException("The expandHeight attribute must be between 0 and 1.0");
		a.recycle();

		SpringSystem springSystem = SpringSystem.create();
		mRevealerSpring = springSystem.createSpring();
		SpringListener revealerSpringListener = new RevealerSpringListener();
		mRevealerSpring.setCurrentValue(1).setEndValue(1).addListener(revealerSpringListener);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		mHandle = findViewById(mHandleId);
		if (mHandle == null)
			throw new IllegalArgumentException("The handle attribute is must refer to an existing child.");

		mContent = findViewById(mContentId);
		if (mContent == null)
			throw new IllegalArgumentException("The content attribute must refer to an existing child.");

		ViewGroup.LayoutParams lp = mContent.getLayoutParams();
		if (mRatio > 0) {
			int getHeightContent = DeviceUtils.getScreenSize(getContext()).y;
			lp.height = (int) (getHeightContent * mRatio);
			mContent.requestLayout();
		}
		mStashPx = lp.height;
		setTranslationY(mStashPx);

		measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		mHeight = getMeasuredHeight();

		// mHandle.setOnTouchListener(new OnNubTouchListener());
	}

	/**
	 * remove the configurator from its parent and clean up springs and
	 * listeners
	 */
	public void destroy() {
		ViewGroup parent = (ViewGroup) getParent();
		if (parent != null) {
			parent.removeView(this);
		}
		mRevealerSpring.destroy();
	}

	public void togglePosition() {
		double currentValue = mRevealerSpring.getEndValue();
		mRevealerSpring.setEndValue(currentValue == 1 ? 0 : 1);
	}

	private class RevealerSpringListener implements SpringListener {
		@Override
		public void onSpringUpdate(Spring spring) {
			float val = (float) spring.getCurrentValue();

			float minTranslate = mRevealPx;
			float maxTranslate = mStashPx;
			float range = maxTranslate - minTranslate;
			float yTranslate = (val * range) + minTranslate;
			SlidingUpPanelLayout.this.setTranslationY(yTranslate);
		}

		@Override
		public void onSpringAtRest(Spring spring) {
		}

		@Override
		public void onSpringActivate(Spring spring) {
		}

		@Override
		public void onSpringEndStateChange(Spring spring) {
			if (mListener != null) {
				if (spring.getCurrentValue() == 0)
					mListener.onPanelClosed(getHeightWithCollapse());
				else
					mListener.onPanelOpened(getHeightWithExpand());
			}
		}
	}

	public int getHeightWithCollapse() {
		return mHeight - (int) mStashPx;
	}

	public int getHeightWithExpand() {
		return mHeight;
	}

	/**
	 * Check if the sliding panel in this layout is currently visible.
	 * 
	 * @return true if the sliding panel is visible.
	 */
	public boolean isPanelHidden() {
		return mRevealerSpring.getEndValue() == 1;
	}

	/**
	 * Shows the panel from the hidden state
	 */
	public void showPanel() {
		mRevealerSpring.setEndValue(0);
	}

	/**
	 * Hides the sliding panel entirely.
	 */
	public void hidePanel() {
		mRevealerSpring.setEndValue(1);
	}

	public void setPanelSlideListener(PanelSlideListener listener) {
		mListener = listener;
	}
}
