package com.nexle.android_libs.utils;

import com.nexle.android_libs.blur.IBlur;

import android.graphics.Bitmap;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;

/**
 * Simple example of ScriptIntrinsicBlur Renderscript gaussion blur. In production always use this algorithm as it is
 * the fastest on Android.
 */
public class RenderScriptGaussianBlur implements IBlur {
	private RenderScript rs;

	public RenderScriptGaussianBlur(RenderScript rs) {
		this.rs = rs;
	}

	@Override
	public Bitmap blur(int radius, Bitmap bitmapOriginal) {
		final Allocation input = Allocation.createFromBitmap(rs, bitmapOriginal);
		final Allocation output = Allocation.createTyped(rs, input.getType());
		final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
		script.setRadius(radius);
		script.setInput(input);
		script.forEach(output);
		output.copyTo(bitmapOriginal);
		return bitmapOriginal;
	}
}
