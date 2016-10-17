package com.pokemongostats.view.commons;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;

/**
 * 
 * @author Zapagon
 *
 */
public class ImageHelper {
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		final Paint paint = new Paint();

		final int bSize = 30;
		final RectF rectF = new RectF(0, 0, 2 * w, h);
		final float roundPx = w;
		Canvas canvas;
		Bitmap src = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		Bitmap src2 = bitmap.copy(Bitmap.Config.ARGB_8888, true);

		canvas = new Canvas(src);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(0xffffffff);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, -bSize * 3, 0, paint);

		canvas = new Canvas(src2);
		paint.setColor(0xffffffff);
		canvas.drawARGB(255, 255, 255, 255);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_ATOP));
		canvas.drawBitmap(src, 0, 0, paint);

		src2 = addWhiteBorder(src2, bSize, roundPx);

		bitmap.recycle();
		return src2;
	}

	private static Bitmap addWhiteBorder(Bitmap bmp, int bSize, float roundPx) {
		int w = bmp.getWidth();
		int h = bmp.getHeight();

		Bitmap bmpWithBorder = Bitmap.createBitmap(w + bSize * 2, h + bSize * 2,
				bmp.getConfig());

		int offset = bSize * 7;
		final Paint paint = new Paint();

		final RectF rectF = new RectF(offset, 0, 2 * w, h + (bSize * 2));
		Canvas canvas = new Canvas(bmpWithBorder);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setAntiAlias(true);
		paint.setColor(0xffffffff);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bmp, offset + bSize, bSize, paint);
		return bmpWithBorder;
	}
}