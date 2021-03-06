package com.nexle.android_libs.utils;

import com.nexle.android_libs.blur.EBlurAlgorithm;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlend;

//import at.favre.app.blurbenchmark.blur.EBlurAlgorithm;
//import at.favre.app.blurbenchmark.blur.algorithms.BoxBlur;
//import at.favre.app.blurbenchmark.blur.algorithms.GaussianFastBlur;
//import at.favre.app.blurbenchmark.blur.algorithms.RenderScriptBox5x5Blur;
//import at.favre.app.blurbenchmark.blur.algorithms.RenderScriptGaussian5x5Blur;
//import at.favre.app.blurbenchmark.blur.algorithms.RenderScriptGaussianBlur;
//import at.favre.app.blurbenchmark.blur.algorithms.RenderScriptStackBlur;
//import at.favre.app.blurbenchmark.blur.algorithms.StackBlur;

/**
 * Created by PatrickF on 07.04.2014.
 */
public class BlurUtil {

	public static Bitmap blur(RenderScript rs, Context ctx, Bitmap bitmap, int radius, EBlurAlgorithm algorithm) {
		switch (algorithm) {
		case RS_GAUSS_FAST:
			return new RenderScriptGaussianBlur(rs).blur(radius, bitmap);
//		case RS_BOX_5x5:
//			return new RenderScriptBox5x5Blur(rs).blur(radius, bitmap);
//		case RS_GAUSS_5x5:
//			return new RenderScriptGaussian5x5Blur(rs).blur(radius, bitmap);
//		case RS_STACKBLUR:
//			return new RenderScriptStackBlur(rs, ctx).blur(radius, bitmap);
//		case STACKBLUR:
//			return new StackBlur().blur(radius, bitmap);
//		case GAUSS_FAST:
//			return new GaussianFastBlur().blur(radius, bitmap);
//		case BOX_BLUR:
//			return new BoxBlur().blur(radius, bitmap);
		default:
			return bitmap;
		}
	}

	public static Bitmap blendRenderScript(RenderScript rs, Bitmap bitmap1, Bitmap bitmap2) {
		if (Build.VERSION.SDK_INT >= 17) {
			final Allocation input1 = Allocation.createFromBitmap(rs, bitmap1, Allocation.MipmapControl.MIPMAP_NONE,
					Allocation.USAGE_SCRIPT);
			final Allocation input2 = Allocation.createFromBitmap(rs, bitmap2, Allocation.MipmapControl.MIPMAP_NONE,
					Allocation.USAGE_SCRIPT);
			final ScriptIntrinsicBlend blendScript = ScriptIntrinsicBlend.create(rs, Element.U8_4(rs));
			blendScript.forEachAdd(input1, input2);
			input2.copyTo(bitmap1);
			return bitmap1;
		} else {
			throw new IllegalStateException("Renderscript needs sdk >= 17");
		}
	}
}