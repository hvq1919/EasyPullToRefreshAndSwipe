package com.nexle.android_libs.wigets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class RoundedCornersImageView extends ImageView {
	private final Paint restorePaint = new Paint();
	private final Paint maskXferPaint = new Paint();
	private final Paint canvasPaint = new Paint();

	private final Rect bounds = new Rect();
	private final RectF boundsf = new RectF();

	public RoundedCornersImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public RoundedCornersImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public RoundedCornersImageView(Context context) {
		super(context);
		init();
	}

	private void init() {
		canvasPaint.setAntiAlias(true);
		canvasPaint.setColor(Color.argb(255, 255, 255, 255));
		restorePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
		maskXferPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY));

		setLayerType(View.LAYER_TYPE_HARDWARE, restorePaint);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.getClipBounds(bounds);
		boundsf.set(bounds);

		super.onDraw(canvas);

		canvas.saveLayer(boundsf, maskXferPaint, Canvas.ALL_SAVE_FLAG);
		canvas.drawARGB(0, 0, 0, 0);
		canvas.drawRoundRect(boundsf, 75, 75, canvasPaint);

		canvas.restore();
	}
}