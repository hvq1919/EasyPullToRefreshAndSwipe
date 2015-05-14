package com.nexle.android_libs.wigets;

import com.nexle.android_libs.R;
import com.nexle.android_libs.utils.UiUtil;

import android.content.Context;
import android.util.AttributeSet;

public class FontableFlatButton extends FlatButton {
	public FontableFlatButton(Context context) {
		super(context);
	}

	public FontableFlatButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		UiUtil.setCustomFont(this, context, attrs,
				R.styleable.FontableButton,
				R.styleable.FontableButton_font);
	}

	public FontableFlatButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		UiUtil.setCustomFont(this, context, attrs,
				R.styleable.FontableButton,
				R.styleable.FontableButton_font);
	}
}