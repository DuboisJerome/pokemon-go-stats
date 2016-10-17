package com.pokemongostats.view;

import com.pokemongostats.R;
import com.pokemongostats.view.activities.PokedexActivity;
import com.pokemongostats.view.commons.ImageHelper;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

public class OverlayService extends Service {

	private WindowManager wm;
	private ImageView icon;

	@Override
	public IBinder onBind(Intent intent) {
		// Not used
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		wm = (WindowManager) getSystemService(WINDOW_SERVICE);

		Drawable drawable = getResources().getDrawable(R.drawable.icon_app);
		Bitmap b = ((BitmapDrawable) drawable).getBitmap();
		b = ImageHelper.getRoundedCornerBitmap(b);

		icon = new ImageView(this);
		icon.setImageBitmap(b);

		final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
				getResources().getInteger(R.integer.overlay_size),
				getResources().getInteger(R.integer.overlay_size),
				WindowManager.LayoutParams.TYPE_PHONE,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);

		params.gravity = Gravity.TOP | Gravity.RIGHT;
		params.x = 0;
		params.y = 100;

		wm.addView(icon, params);

		try {
			icon.setOnTouchListener(new View.OnTouchListener() {
				private int initialX;
				private int initialY;

				private float initialTouchY;

				private long time_start = 0;
				private long time_end = 0;

				@Override
				public boolean onTouch(View v, MotionEvent event) {

					switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN :
							time_start = System.currentTimeMillis();
							initialX = params.x;
							initialY = params.y;
							initialTouchY = event.getRawY();
							return true;
						case MotionEvent.ACTION_UP :
							time_end = System.currentTimeMillis();
							if ((time_end - time_start) < 300) {
								onClickIcon();
							}
							return true;
						case MotionEvent.ACTION_MOVE :
							params.x = initialX;
							params.y = initialY
								+ (int) (event.getRawY() - initialTouchY);
							wm.updateViewLayout(icon, params);
							return true;
					}
					Log.d("ICON", "TOUCH");
					return false;
				}
			});

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (icon != null) {
			wm.removeView(icon);
		}
	}

	/**
	 * On clic icon
	 */
	private void onClickIcon() {
		FragmentActivity currentActivity = ((PkmnGoHelperApplication) getApplicationContext()
				.getApplicationContext()).getCurrentActivity();
		if (currentActivity != null) {
			currentActivity.moveTaskToBack(true);
		} else {

			final Intent intent = new Intent(getApplicationContext(),
					PokedexActivity.class);
			intent.setAction(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			startActivity(intent);
		}
	}
}