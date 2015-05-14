package com.nexle.android_libs.wigets;

import com.nexle.android_libs.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Button;

public class FlatButton extends Button {
    private StateListDrawable mNormalDrawable;
    private CharSequence mNormalText;
    private float cornerRadius;

    public FlatButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public FlatButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FlatButton(Context context) {
        super(context);
        init(context, null);
    }

    private void init(Context context, AttributeSet attrs) {
        mNormalDrawable = new StateListDrawable();
        if (attrs != null) {
            initAttributes(context, attrs);
        }
        mNormalText = getText().toString();
        setBackgroundCompat(mNormalDrawable);
    }

    private void initAttributes(Context context, AttributeSet attributeSet) {
		TypedArray attr = getTypedArray(context, attributeSet, R.styleable.FlatButton);
		if (attr == null)
			return;

		try {
			cornerRadius = attr.getDimension(R.styleable.FlatButton_cornerRadius, 0);
			int colorNormal = attr.getColor(R.styleable.FlatButton_colorNormal, 0);
			int colorPressed = attr.getColor(R.styleable.FlatButton_colorPressed, 0);
			if (colorPressed == 0) {
				int r = Color.red(colorNormal);
				int g = Color.green(colorNormal);
				int b = Color.blue(colorNormal);
				colorPressed = Color.argb(0xAF, r, g, b);
			}
			mNormalDrawable.addState(new int[] { android.R.attr.state_pressed }, createStateDrawable(colorPressed));
			mNormalDrawable.addState(new int[] {}, createStateDrawable(colorNormal));
		} finally {
			attr.recycle();
		}
    }

    private Drawable createStateDrawable(int color) {
        GradientDrawable drawablePressed = new GradientDrawable();
        drawablePressed.setCornerRadius(getCornerRadius());
        drawablePressed.setColor(color);

        return drawablePressed;
    }

    protected Drawable getDrawable(int id) {
        return getResources().getDrawable(id);
    }

    protected float getDimension(int id) {
        return getResources().getDimension(id);
    }

    protected int getColor(int id) {
        return getResources().getColor(id);
    }

    protected TypedArray getTypedArray(Context context, AttributeSet attributeSet, int[] attr) {
        return context.obtainStyledAttributes(attributeSet, attr, 0, 0);
    }

    public float getCornerRadius() {
        return cornerRadius;
    }

    public StateListDrawable getNormalDrawable() {
        return mNormalDrawable;
    }

    public CharSequence getNormalText() {
        return mNormalText;
    }

    /**
     * Set the View's background. Masks the API changes made in Jelly Bean.
     *
     * @param drawable
     */
    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public void setBackgroundCompat(Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(drawable);
        } else {
            setBackgroundDrawable(drawable);
        }
    }
}
