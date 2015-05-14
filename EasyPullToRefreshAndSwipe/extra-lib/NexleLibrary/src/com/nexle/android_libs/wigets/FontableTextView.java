package com.nexle.android_libs.wigets;

import com.nexle.android_libs.R;
import com.nexle.android_libs.utils.UiUtil;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

public class FontableTextView extends TextView {
	public FontableTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (isInEditMode())
			return;

		initAttributes(context, attrs);
	}

	public FontableTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		if (isInEditMode())
			return;

		initAttributes(context, attrs);
	}

	/*
	 * initAttributes
	 * 
	 * @param Context context
	 * 
	 * @param AttributeSet attrs
	 */
	private void initAttributes(Context context, AttributeSet attrs) {
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FontableTextView);
		String customFont = a.getString(R.styleable.FontableTextView_font);
		if (customFont != null) {
			Typeface tp = UiUtil.getFont(context, customFont);
			if (tp != null)
				this.setTypeface(tp);
		}
		float fontSizePt = a.getFloat(R.styleable.FontableTextView_fontSizeUnitPt, 0.0f);
		if (fontSizePt != 0)
			this.setTextSize(TypedValue.COMPLEX_UNIT_PX, UiUtil.ptToPx(context, fontSizePt));
		a.recycle();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		System.out.println("==> .onSizeChanged child: " + getText().toString() + " - w: " + w + ", h: " + h);
		super.onSizeChanged(w, h, oldw, oldh);
	}

}