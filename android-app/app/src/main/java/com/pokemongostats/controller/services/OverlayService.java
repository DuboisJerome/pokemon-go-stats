package com.pokemongostats.controller.services;

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
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.pokemongostats.R;
import com.pokemongostats.view.PkmnGoStatsApplication;
import com.pokemongostats.view.activities.PokedexActivity;
import com.pokemongostats.view.utils.ImageHelperUtils;

public class OverlayService extends Service {

    private final IBinder mBinder = new OverlayServiceBinder();
    private WindowManager wm;
    private ImageView icon;

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        wm = (WindowManager) getSystemService(WINDOW_SERVICE);

        Drawable drawable = ContextCompat.getDrawable(this,
                R.drawable.icon_app);
        Bitmap b = ((BitmapDrawable) drawable).getBitmap();
        b = ImageHelperUtils.getRoundedCornerBitmap(b);

        icon = new ImageView(this);
        icon.setImageBitmap(b);

        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final int screenHeight = size.y;
        final int screenWidth = size.x;

        final RemoveView removeView = new RemoveView(this, screenWidth,
                screenHeight);

        final WindowManager.LayoutParams iconParams = new WindowManager.LayoutParams(
                getResources().getInteger(R.integer.overlay_size),
                getResources().getInteger(R.integer.overlay_size),
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        iconParams.gravity = Gravity.TOP | Gravity.RIGHT;
        iconParams.x = 0;
        iconParams.y = 180;

        wm.addView(icon, iconParams);

        try {
            icon.setOnTouchListener(new View.OnTouchListener() {
                private int initialX;
                private int initialY;

                private float initialTouchY;

                private long time_start = 0;
                private long time_end = 0;

                private boolean isRemoveViewVisible = false;

                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            time_start = System.currentTimeMillis();
                            initialX = iconParams.x;
                            initialY = iconParams.y;
                            initialTouchY = event.getRawY();

                            return true;
                        case MotionEvent.ACTION_UP:
                            time_end = System.currentTimeMillis();
                            if ((time_end - time_start) < 200) {
                                onClickIcon();
                                time_start = 0;
                                time_end = 0;
                            } else {
                                if (iconParams.y >= screenHeight * 0.8) {

                                    FragmentActivity currentActivity = ((PkmnGoStatsApplication) getApplicationContext())
                                            .getCurrentActivity();
                                    if (currentActivity != null
                                            && !currentActivity.isFinishing()) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                            currentActivity.finishAndRemoveTask();
                                        } else {
                                            currentActivity.finish();
                                        }
                                    }
                                    stopSelf();
                                    wm.removeView(icon);
                                }
                            }
                            if (isRemoveViewVisible) {
                                wm.removeView(removeView);
                                isRemoveViewVisible = false;
                            }

                            return true;
                        case MotionEvent.ACTION_MOVE:
                            iconParams.x = initialX;
                            iconParams.y = initialY
                                    + (int) (event.getRawY() - initialTouchY);

                            if (!isRemoveViewVisible) {
                                isRemoveViewVisible = true;
                                final WindowManager.LayoutParams removeViewParams = new WindowManager.LayoutParams(
                                        screenWidth, screenHeight,
                                        WindowManager.LayoutParams.TYPE_PHONE,
                                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                                        PixelFormat.TRANSLUCENT);
                                wm.addView(removeView, removeViewParams);
                            }

                            wm.updateViewLayout(icon, iconParams);
                            return true;
                    }
                    Log.d("ICON", "TOUCH");
                    return false;
                }
            });

        } catch (Exception e) {
            // TODO: handle exception
        }
        onClickIcon();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_NOT_STICKY;
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
        PkmnGoStatsApplication app = ((PkmnGoStatsApplication) getApplicationContext());
        FragmentActivity currentActivity = app.getCurrentActivity();
        if (currentActivity != null) {
            if (app.isCurrentActivityIsVisible()) {
                // should not happen any more
                minimize();
            } else {
                maximize();
            }
        } else {
            maximize();
        }
    }

    public void minimize() {
        if (icon != null) {
            icon.setVisibility(View.VISIBLE);
        }
        PkmnGoStatsApplication app = ((PkmnGoStatsApplication) getApplicationContext());
        FragmentActivity currentActivity = app.getCurrentActivity();
        if (currentActivity != null) {
            currentActivity.moveTaskToBack(true);
        }
    }

    public void maximize() {
        if (icon != null) {
            icon.setVisibility(View.GONE);
        }
        PkmnGoStatsApplication app = ((PkmnGoStatsApplication) getApplicationContext());
        FragmentActivity currentActivity = app.getCurrentActivity();
        if (currentActivity != null) {
            startActivity(currentActivity.getIntent());
        } else {
            final Intent intent = new Intent(getApplicationContext(),
                    PokedexActivity.class);
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
        }

    }

    ;

    public class OverlayServiceBinder extends Binder {
        public OverlayService getService() {
            return OverlayService.this;
        }
    }

    private class RemoveView extends View {
        private final float limitY, limitX, width, crossCenterX, crossCenterY;
        private final RectF rect;
        private final int crossRadius;

        public RemoveView(Context context, int w, int h) {
            super(context);
            this.limitY = h * 0.8f;
            this.width = 180;
            this.limitX = w - width;
            float left = limitX;
            float right = limitX + (2f * width);
            float top = limitY;
            float bottom = h - 80;
            this.rect = new RectF(left, top, right, bottom);
            this.crossRadius = 40;
            this.crossCenterX = (left + (limitX + (1.25f * width))) / 2;
            this.crossCenterY = (top + bottom) / 2;
        }

        @Override
        public void onDraw(Canvas canvas) {
            final Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.WHITE);
            paint.setStrokeWidth(7);

            final Paint paintBg = new Paint();
            paintBg.setColor(Color.BLACK);
            paintBg.setAlpha(80);
            // border
            canvas.drawRoundRect(rect, width, width, paint);
            canvas.drawRoundRect(rect, width, width, paintBg);

            // cross
            // top left to bottom right
            canvas.drawLine(crossCenterX - crossRadius,
                    crossCenterY - crossRadius, crossCenterX + crossRadius,
                    crossCenterY + crossRadius, paint);
            // top right to bottom left
            canvas.drawLine(crossCenterX - crossRadius,
                    crossCenterY + crossRadius, crossCenterX + crossRadius,
                    crossCenterY - crossRadius, paint);
        }
    }
}