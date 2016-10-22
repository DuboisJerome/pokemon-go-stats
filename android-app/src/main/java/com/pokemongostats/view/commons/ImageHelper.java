package com.pokemongostats.view.commons;

import java.io.IOException;
import java.io.InputStream;

import com.pokemongostats.model.bean.PokemonDescription;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * 
 * @author Zapagon
 *
 */
public class ImageHelper {

	private static final String ASSETS_FILE_SPRITE_PATH = "pokemons_sprites/";

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
		return src2;
	}

	private static Bitmap addWhiteBorder(Bitmap bmp, int bSize, float roundPx) {
		int w = bmp.getWidth();
		int h = bmp.getHeight();

		Bitmap bmpWithBorder = Bitmap.createBitmap(w + bSize * 2, h + bSize * 2, bmp.getConfig());

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

	public static Drawable resize(Drawable image, Context context) {
		Bitmap bitmapResized = Bitmap.createScaledBitmap(((BitmapDrawable) image).getBitmap(), 50, 50, false);
		return new BitmapDrawable(context.getResources(), bitmapResized);
	}

	public static Drawable getDrawableFromAssets(final Context context, final String fileName) {
		AssetManager am = context.getAssets();
		try {
			// get input stream
			InputStream ims = am.open(ASSETS_FILE_SPRITE_PATH + fileName);
			// return drawable
			return Drawable.createFromStream(ims, null);
		} catch (IOException ex) {
			return null;
		}
	}

	public static Drawable getPkmnDrawable(final Context c, final PokemonDescription p) {
		String fileName = p.getPokedexNum() + ".png";
		return getDrawableFromAssets(c, fileName);
	}
}