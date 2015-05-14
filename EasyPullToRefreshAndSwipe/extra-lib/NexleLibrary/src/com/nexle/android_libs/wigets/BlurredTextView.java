/*******************************************************************************
 * Copyright (c) 2014 blinkbox Entertainment Limited. All rights reserved.
 *******************************************************************************/
package com.nexle.android_libs.wigets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

public class BlurredTextView extends TextView {
	private long mFrameCount = 0;

	private Bitmap mBackground = null;
	private Bitmap mOverlay = null;
	private Canvas mOverlayCanvas = null;
	private RenderScript mRs = null;
	private Allocation mOverlayAlloc = null;
	private ScriptIntrinsicBlur mBlur = null;

	private float mRadius = 25;

	public BlurredTextView(Context context) {
		this(context, null, -1);
	}

	public BlurredTextView(Context context, AttributeSet attrs) {
		super(context, attrs, -1);
	}

	public BlurredTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (mRs != null) {
			mFrameCount++;
			mOverlayCanvas.drawBitmap(mBackground, -getLeft() - getTranslationX(), -getTop() - getTranslationY(), null);
			mOverlayAlloc.copyFrom(mOverlay);
			mBlur.setInput(mOverlayAlloc);
			mBlur.forEach(mOverlayAlloc);
			mOverlayAlloc.copyTo(mOverlay);
			canvas.drawBitmap(mOverlay, 0, 0, null);
		}
		super.onDraw(canvas);
	}

	public long getFrameCount() {
		return mFrameCount;
	}

	public void setFrameCount(long frameCount) {
		this.mFrameCount = frameCount;
	}

	public void initBlur(ImageView bgd, float radius) {
		if (bgd != null && bgd.getDrawable() != null) {
			Drawable drawable = bgd.getDrawable();
			if (drawable != null && drawable instanceof BitmapDrawable) {
				mBackground = ((BitmapDrawable) drawable).getBitmap();
				if (mBackground.isRecycled()) {
					mBackground = null;
				}
			}
		}
		mRs = RenderScript.create(getContext());
		mRadius = radius;
		resize();
	}

	private void resize() {
		if (mRs != null) {
			mOverlay = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
			mOverlayCanvas = new Canvas(mOverlay);
			if (mOverlayAlloc != null) {
				mOverlayAlloc.destroy();
			}
			mOverlayAlloc = Allocation.createFromBitmap(mRs, mOverlay);
			if (mBlur != null) {
				mBlur.destroy();
			}
			mBlur = ScriptIntrinsicBlur.create(mRs, mOverlayAlloc.getElement());
			mBlur.setRadius(mRadius);
		}
	}

	public void cleanupBlur() {
		mBackground = null;
		mOverlay = null;
		mOverlayCanvas = null;
		mRs.destroy();
		mRs = null;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if (w != oldw || h != oldh) {
			resize();
		}
	}
}