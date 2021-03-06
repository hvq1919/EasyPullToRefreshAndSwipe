/**
 * 
 */
package com.nexle.android_libs.wigets;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;

/**
 * @author DUONGNX
 * 
 */
public class CustomTypefaceSpan extends TypefaceSpan {
	private Typeface newType;

	public CustomTypefaceSpan(String family, Typeface type) {
		super(family);
		this.newType = type;
	}

	@Override
	public void updateDrawState(TextPaint ds) {
		super.updateDrawState(ds);
		applyCustomTypeFace(ds, newType);
	}

	@Override
	public void updateMeasureState(TextPaint paint) {
		super.updateMeasureState(paint);
		applyCustomTypeFace(paint, newType);
	}

	private void applyCustomTypeFace(Paint paint, Typeface tf) {
		int oldStyle;
		Typeface old = paint.getTypeface();
		if (old == null) {
			oldStyle = 0;
		} else {
			oldStyle = old.getStyle();
		}

		int fake = oldStyle & ~tf.getStyle();
		if ((fake & Typeface.BOLD) != 0) {
			paint.setFakeBoldText(true);
		}

		if ((fake & Typeface.ITALIC) != 0) {
			paint.setTextSkewX(-0.25f);
		}

		paint.setTypeface(tf);
	}
}
