package com.pokemongostats.controller;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.pokemongostats.R;

public class ServiceNotification {

	public static final String BACKTOAPP_CHANNEL = "BACKTOAPP_CHANNEL_POGOHELPER";
	public static final String SILENT_CHANNEL = "SILENT_CHANNEL_POGOHELPER";

	public static void creerChannelNotifBackToApp(@NonNull Context ctx) {
		CharSequence name = ctx.getString(R.string.backtoapp_channel_name);
		String description = ctx.getString(R.string.backtoapp_channel_description);
		creerChannelNotifSilencieux(ctx, BACKTOAPP_CHANNEL, name, description);
	}

	public static void creerChannelNotifSilencieux(@NonNull Context ctx) {
		creerChannelNotifSilencieux(ctx, SILENT_CHANNEL, "Silent", "");
	}

	private static void creerChannelNotifSilencieux(@NonNull Context ctx, String channelTag, CharSequence channelName, String channelDesc) {
		NotificationChannel channel = new NotificationChannel(channelTag, channelName, NotificationManager.IMPORTANCE_LOW);
		channel.setDescription(channelDesc);

		// Register the channel with the system; you can't change the importance or other notification behaviors after this
		NotificationManager notificationManager = ctx.getSystemService(NotificationManager.class);
		notificationManager.createNotificationChannel(channel);
	}

	public static void creerNotifBackToApp(Activity activity) {

		PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, activity.getIntent(), PendingIntent.FLAG_UPDATE_CURRENT);

		String title = activity.getString(R.string.backtoapp);
		Notification notification = new NotificationCompat.Builder(activity, ServiceNotification.SILENT_CHANNEL)
				.setContentTitle(title)
				.setSmallIcon(R.drawable.ic_app)
				.setVisibility(NotificationCompat.VISIBILITY_SECRET)
				.setCategory(NotificationCompat.CATEGORY_REMINDER)
				.setContentIntent(pendingIntent)
				.setPriority(NotificationCompat.PRIORITY_LOW)
				.setOnlyAlertOnce(true)
				.setAutoCancel(true)
				.build();

		NotificationManagerCompat notificationManager = NotificationManagerCompat.from(activity);
		notificationManager.notify(0, notification);
	}

}