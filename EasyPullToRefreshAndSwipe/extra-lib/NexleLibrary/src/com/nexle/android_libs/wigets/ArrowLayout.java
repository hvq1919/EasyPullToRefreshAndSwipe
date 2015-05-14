package com.nexle.android_libs.wigets;

import com.nexle.android_libs.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class ArrowLayout extends ViewGroup {
	private static final int ARROW_LEFT = 0;
	private static final int ARROW_RIGHT = 1;
	private static final int ARROW_BOTH = 2;
	private static final int DEFAULT_COLOUR = 0xFF17181a;
	private static int PADDING = 0;

	private Path path;
	private Paint paint;
	private int arrowSide = ARROW_RIGHT;
	private int colour = DEFAULT_COLOUR;

	public ArrowLayout(Context context) {
		super(context);
		init(context, null);
	}

	public ArrowLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public ArrowLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.ArrowLayout, 0, 0);
		try {
			arrowSide = a.getInteger(R.styleable.ArrowLayout_arrowSide,
					ARROW_RIGHT);
			colour = a.getColor(R.styleable.ArrowLayout_arrowColor,
					DEFAULT_COLOUR);
		} finally {
			a.recycle();
		}

		paint = new Paint();
		paint.setColor(colour);
		paint.setStyle(Style.FILL);
		setWillNotDraw(false);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		PADDING = getMeasuredHeight() / 4;

		path = new Path();
		switch (arrowSide) {
		case ARROW_RIGHT:
			path.lineTo(0, h);
			path.lineTo(w - PADDING, h);
			path.lineTo(w - PADDING, h / 4f * 3f);
			path.lineTo(w - PADDING + h / 4f, h / 2f);
			path.lineTo(w - PADDING, h / 4f);
			path.lineTo(w - PADDING, 0);
			break;

		case ARROW_LEFT:
			path.moveTo(PADDING - h / 4f, h / 2f);
			path.lineTo(PADDING, h / 4f * 3f);
			path.lineTo(PADDING, h);
			path.lineTo(w, h);
			path.lineTo(w, 0);
			path.lineTo(PADDING, 0);
			path.lineTo(PADDING, h / 4f);
			break;

		case ARROW_BOTH:
			path.moveTo(PADDING - h / 2f, h / 2f);
			path.lineTo(PADDING, h);
			path.lineTo(w - PADDING, h);
			path.lineTo(w - PADDING + h / 2f, h / 2f);
			path.lineTo(w - PADDING, 0);
			path.lineTo(PADDING, 0);
			break;
		}
		path.close();

		super.onSizeChanged(w, h, oldw, oldh);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int wspec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(),
				MeasureSpec.EXACTLY);
		int hspec = MeasureSpec.makeMeasureSpec(getMeasuredHeight(),
				MeasureSpec.EXACTLY);

		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			ViewGroup v = (ViewGroup) getChildAt(i);
			if (v.getVisibility() == GONE)
				continue;
			v.measure(wspec - PADDING, hspec);
			// v.requestLayout();
			//
			// if (v instanceof ViewGroup) {
			// // Force layout for all child views.
			// final int count1 = ((ViewGroup) v).getChildCount();
			// for (int i1 = 0; i1 < count1; i1++) {
			// View child = ((ViewGroup) v).getChildAt(i1);
			// LayoutParams lParams = (LayoutParams) child.getLayoutParams();
			// child.measure(MeasureSpec.makeMeasureSpec(MeasureSpec.EXACTLY,
			// lParams.width - PADDING),
			// MeasureSpec.makeMeasureSpec(MeasureSpec.EXACTLY,
			// lParams.height));
			// }
			// }
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int width = r - l;
		int height = b - t;
		final int count = getChildCount();

		for (int i = 0; i < count; i++) {
			final View v = getChildAt(i);
			if (v.getVisibility() == GONE)
				continue;
			if (arrowSide == ARROW_LEFT)
				v.layout(PADDING, 0, width, height);
			else if (arrowSide == ARROW_RIGHT)
				v.layout(0, 0, width - PADDING, height);

			v.requestLayout();
			if (v instanceof ViewGroup) {
				// Force layout for all child views.
				final int count1 = ((ViewGroup) v).getChildCount();
				for (int i1 = 0; i1 < count1; i1++) {
					View child = ((ViewGroup) v).getChildAt(i1);
					LayoutParams lp = child.getLayoutParams();
					child.requestLayout();
					// child.layout(PADDING, 0, child.getMeasuredWidth(),
					// child.getMeasuredHeight());
					// ((ViewGroup) v).getChildAt(i1).forceLayout();
				}
			}
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawPath(path, paint);
	}
}
