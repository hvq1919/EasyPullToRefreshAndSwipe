package com.nexle.android_libs.wigets;

import com.nexle.android_libs.R;
import com.nexle.android_libs.utils.UiUtil;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.Button;

public class FontableButton extends Button {

	private int iOpacity = -1;
	private ColorStateList orditionColor;

	public FontableButton(Context context) {
		super(context);
	}

	public FontableButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (isInEditMode())
			return;
		initAttributes(context, attrs);
		iOpacity = getOpacity(context, attrs);
		// UiUtil.setCustomFont(this, context, attrs,
		// R.styleable.FontableTextView, R.styleable.FontableTextView_font);
		orditionColor = getTextColors();
		customTextColor();
	} 

	public FontableButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		if (isInEditMode())
			return;
		initAttributes(context, attrs);
		iOpacity = getOpacity(context, attrs);
		// UiUtil.setCustomFont(this, context, attrs,
		// R.styleable.FontableTextView, R.styleable.FontableTextView_font);
		orditionColor = getTextColors();
		customTextColor();
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

	private void customTextColor() {
		if (iOpacity >= 0) {
			int[][] states = new int[][] { new int[] { -android.R.attr.state_pressed, android.R.attr.state_selected }, // press:false,select:true
					new int[] { android.R.attr.state_pressed }, // press:true
					new int[] { -android.R.attr.state_pressed, -android.R.attr.state_selected } // press:false,select:false
			};
			int color = orditionColor.getDefaultColor();
			int[] colors = new int[] { Color.argb(Color.alpha(color), Color.red(color), Color.green(color), Color.blue(color)),
					Color.argb(iOpacity, Color.red(color), Color.green(color), Color.blue(color)),
					Color.argb(Color.alpha(color), Color.red(color), Color.green(color), Color.blue(color)), };
			setTextColor(new ColorStateList(states, colors));
			invalidate();
		}
	}

	/**
	 * @return
	 */
	private int getOpacity(Context context, AttributeSet attrs) {
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FontableButton);
		int result = a.getInteger(R.styleable.FontableButton_opacity_click, -1);
		a.recycle();
		return result;
	}
}