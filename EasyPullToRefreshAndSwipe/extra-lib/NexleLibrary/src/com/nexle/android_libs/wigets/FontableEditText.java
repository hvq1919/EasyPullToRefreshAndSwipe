package com.nexle.android_libs.wigets;

import com.nexle.android_libs.R;
import com.nexle.android_libs.utils.UiUtil;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

public class FontableEditText extends EditText implements OnFocusChangeListener {
	private String hint;

	public FontableEditText(Context context) {
		super(context);
		if (isInEditMode())
			return;
		init();
	}

	public FontableEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (isInEditMode())
			return;
		initAttributes(context, attrs);
		UiUtil.setCustomFont(this, context, attrs, R.styleable.FontableTextView, R.styleable.FontableTextView_font);
		init();
	}

	public FontableEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		if (isInEditMode())
			return;
		initAttributes(context, attrs);
		// UiUtil.setCustomFont(this, context, attrs,
		// R.styleable.FontableTextView, R.styleable.FontableTextView_font);
		init();
	}

	private void init() {
		try {
			hint = getHint().toString();
			if (hint != null)
				super.setOnFocusChangeListener(this);
		} catch (Exception e) {

		}
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus)
			this.setHint(null);
		else
			this.setHint(hint); 
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
}