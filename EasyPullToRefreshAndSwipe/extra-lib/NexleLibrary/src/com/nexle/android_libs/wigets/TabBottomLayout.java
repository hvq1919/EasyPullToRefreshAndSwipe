package com.nexle.android_libs.wigets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;

public class TabBottomLayout extends HorizontalScrollView {

	public TabBottomLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initAttributes(context, attrs, defStyle);
	}

	public TabBottomLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initAttributes(context, attrs, 0);
	}

	public TabBottomLayout(Context context) {
		super(context);
		initAttributes(context, null, 0);
	}

	private void initAttributes(Context context, AttributeSet attrs, int defStyle) {
		setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					// Disallow ScrollView to intercept touch events.
					v.getParent().requestDisallowInterceptTouchEvent(true);
					break;

				case MotionEvent.ACTION_UP:
					// Allow ScrollView to intercept touch events.
					v.getParent().requestDisallowInterceptTouchEvent(false);
					break;
				}

				// Handle HorizontalScrollView touch events.
				v.onTouchEvent(event);
				return true;
			}
		});

	}

//	@Override
//	public boolean onTouchEvent(MotionEvent ev) {
//		System.out.println("==> onTouchEvent, with action : " + ev.getAction());
//
//		switch (ev.getAction()) {
//		case MotionEvent.ACTION_MOVE:
//			return true;
//		}
//		return super.onTouchEvent(ev);
//	}
}