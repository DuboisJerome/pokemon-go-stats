package com.pokemongostats.controller.services;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.pokemongostats.R;
import com.pokemongostats.controller.utils.TagUtils;
import com.pokemongostats.view.PkmnGoStatsApplication;
import com.pokemongostats.view.utils.ImageUtils;

public class OverlayService extends Service {

	private final IBinder mBinder = new OverlayServiceBinder();
	private ImageView icon;

	@Override
	public IBinder onBind(Intent intent) {
		return this.mBinder;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);

		Drawable drawable = ContextCompat.getDrawable(this,
				R.drawable.ic_app);
		assert drawable != null;
		Bitmap b = ((BitmapDrawable) drawable).getBitmap();
		b = ImageUtils.getRoundedCornerBitmap(b);

		this.icon = new ImageView(this);
		this.icon.setImageBitmap(b);
		// par defaut maximizé
		this.icon.setVisibility(View.GONE);

		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int screenHeight = size.y;
		int screenWidth = size.x;

		RemoveView removeView = new RemoveView(this, screenWidth,
				screenHeight);

		WindowManager.LayoutParams iconParams = new WindowManager.LayoutParams(
				getResources().getInteger(R.integer.overlay_size),
				getResources().getInteger(R.integer.overlay_size),
				WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);

		iconParams.gravity = Gravity.TOP | Gravity.END;
		iconParams.x = 0;
		iconParams.y = 180;

		// FIXME overlay wm.addView(icon, iconParams);

//        try {
//            icon.setOnTouchListener(new View.OnTouchListener() {
//                private int initialX;
//                private int initialY;
//
//                private float initialTouchY;
//
//                private long time_start = 0;
//                private long time_end = 0;
//
//                private boolean isRemoveViewVisible = false;
//
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//
//                    switch (event.getAction()) {
//                        case MotionEvent.ACTION_DOWN:
//                            time_start = System.currentTimeMillis();
//                            initialX = iconParams.x;
//                            initialY = iconParams.y;
//                            initialTouchY = event.getRawY();
//
//                            return true;
//                        case MotionEvent.ACTION_UP:
//                            time_end = System.currentTimeMillis();
//                            if ((time_end - time_start) < 200) {
//                                // <200ms it was a simple click
//                                onClickIcon();
//                                time_start = 0;
//                                time_end = 0;
//                            } else {
//                                // >200ms && in remove view => close app
//                                if (iconParams.y >= screenHeight * 0.8) {
//
//                                    FragmentActivity currentActivity = ((PkmnGoStatsApplication) getApplicationContext())
//                                            .getCurrentActivity();
//                                    if (currentActivity != null) {
//                                        sendBroadcast(new Intent(TagUtils.EXIT));
//                                    } else {
//                                        Log.e(TagUtils.CRIT, "No current activity");
//                                    }
//                                    // stop the service
//                                    stopSelf();
//                                    // remove the icon
//                                    // FIXME wm.removeView(icon);
//                                }
//                            }
//                            if (isRemoveViewVisible) {
//                                // FIXME wm.removeView(removeView);
//                                isRemoveViewVisible = false;
//                            }
//
//                            return true;
//                        case MotionEvent.ACTION_MOVE:
//                            iconParams.x = initialX;
//                            iconParams.y = initialY
//                                    + (int) (event.getRawY() - initialTouchY);
//
//                            if (!isRemoveViewVisible) {
//                                isRemoveViewVisible = true;
//                                final WindowManager.LayoutParams removeViewParams = new WindowManager.LayoutParams(
//                                        screenWidth, screenHeight,
//                                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
//                                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//                                        PixelFormat.TRANSLUCENT);
//                                // FIXME wm.addView(removeView, removeViewParams);
//                            }
//
//                            wm.updateViewLayout(icon, iconParams);
//                            return true;
//                    }
//                    Log.d("ICON", "TOUCH");
//                    return false;
//                }
//            });
//
//        } catch (Exception e) {
//            Log.e(TagUtils.CRIT, "Error in OverlayService", e);
//        }
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return Service.START_NOT_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (this.icon != null) {
			// FIXME wm.removeView(icon);
		}
	}

	/**
	 * On clic icon
	 */
	private void onClickIcon() {
		maximize();
	}

	public void minimize() {
		PkmnGoStatsApplication app = ((PkmnGoStatsApplication) getApplicationContext());
		FragmentActivity currentActivity = app.getCurrentActivity();
		if (currentActivity != null) {
			currentActivity.moveTaskToBack(true);
		} else {
			Log.e(TagUtils.DEBUG, "No current activity in application");
		}
		if (this.icon != null) {
			this.icon.setVisibility(View.VISIBLE);
		}
	}

	public void maximize() {
		PkmnGoStatsApplication app = ((PkmnGoStatsApplication) getApplicationContext());
		restartActivity(app.getCurrentActivity());
		if (this.icon != null) {
			this.icon.setVisibility(View.GONE);
		}
	}

	private void restartActivity(Activity a) {
		if (a != null) {
			Log.i(TagUtils.DEBUG, "Restart activity : " + a.getClass().getName());
			Intent intent = a.getIntent();
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		}
	}

	public class OverlayServiceBinder extends Binder {
		public OverlayService getService() {
			return OverlayService.this;
		}
	}

	private static class RemoveView extends View {
		private final float width;
		private final float crossCenterX;
		private final float crossCenterY;
		private final RectF rect;
		private final int crossRadius;

		public RemoveView(Context context, int w, int h) {
			super(context);
			float limitY = h * 0.8f;
			this.width = getResources().getInteger(R.integer.overlay_size);
			float limitX = w - this.width;
			float right = limitX + (2f * this.width);
			float bottom = h - getResources().getInteger(R.integer.overlay_size);
			this.rect = new RectF(limitX, limitY, right, bottom);
			this.crossRadius = getResources().getInteger(R.integer.overlay_size) / 4;
			this.crossCenterX = (limitX + (limitX + (1.25f * this.width))) / 2;
			this.crossCenterY = (limitY + bottom) / 2;
		}


		private final Paint paint = new Paint();
		private final Paint paintBg = new Paint();

		@Override
		public void onDraw(Canvas canvas) {
			this.paint.setStyle(Paint.Style.STROKE);
			this.paint.setColor(Color.WHITE);
			this.paint.setStrokeWidth(7);

			this.paintBg.setColor(Color.BLACK);
			this.paintBg.setAlpha(80);
			// border
			canvas.drawRoundRect(this.rect, this.width, this.width, this.paint);
			canvas.drawRoundRect(this.rect, this.width, this.width, this.paintBg);

			// cross
			// top left to bottom right
			canvas.drawLine(this.crossCenterX - this.crossRadius,
					this.crossCenterY - this.crossRadius, this.crossCenterX + this.crossRadius,
					this.crossCenterY + this.crossRadius, this.paint);
			// top right to bottom left
			canvas.drawLine(this.crossCenterX - this.crossRadius,
					this.crossCenterY + this.crossRadius, this.crossCenterX + this.crossRadius,
					this.crossCenterY - this.crossRadius, this.paint);
		}
	}
}