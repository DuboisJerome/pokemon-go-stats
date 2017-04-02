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
import com.pokemongostats.controller.utils.TagUtils;
import com.pokemongostats.view.PkmnGoStatsApplication;
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
        // par defaut maximizé
        icon.setVisibility(View.GONE);

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

        iconParams.gravity = Gravity.TOP | Gravity.END;
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
                                // <200ms it was a simple click
                                onClickIcon();
                                time_start = 0;
                                time_end = 0;
                            } else {
                                // >200ms && in remove view => close app
                                if (iconParams.y >= screenHeight * 0.8) {

                                    FragmentActivity currentActivity = ((PkmnGoStatsApplication) getApplicationContext())
                                            .getCurrentActivity();
                                    if (currentActivity != null) {
                                        sendBroadcast(new Intent("EXIT"));
                                    } else {
                                        Log.e(TagUtils.CRIT, "No current activity");
                                    }
                                    // stop the service
                                    stopSelf();
                                    // remove the icon
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
            Log.e(TagUtils.CRIT, "Error in OverlayService", e);
        }
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
        if (icon != null) {
            icon.setVisibility(View.VISIBLE);
        }
    }

    public void maximize() {
        PkmnGoStatsApplication app = ((PkmnGoStatsApplication) getApplicationContext());
        restartActivity(app.getCurrentActivity());
        if (icon != null) {
            icon.setVisibility(View.GONE);
        }
    }

    private void restartActivity(final Activity a){
        if(a != null){
            Log.i(TagUtils.DEBUG, "Restart activity : " +a.getClass().getName());
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


        private final Paint paint = new Paint();
        private final Paint paintBg = new Paint();
        @Override
        public void onDraw(Canvas canvas) {
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.WHITE);
            paint.setStrokeWidth(7);

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