package com.pokemongostats.controller;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

import androidx.annotation.NonNull;

public class ServiceNotification {

	public static final String SILENT_CHANNEL = "POGOHELPER_SILENT";

	public static void createNotificationChannel(@NonNull Context ctx) {
		CharSequence name = "Nom channel test";//;ctx.getString(R.string.channel_name);
		String description = "Desc channel test";//ctx.getString(R.string.channel_description);
		NotificationChannel channel = new NotificationChannel(SILENT_CHANNEL, name, NotificationManager.IMPORTANCE_LOW);
		channel.setDescription(description);
		// Register the channel with the system; you can't change the importance
		// or other notification behaviors after this
		NotificationManager notificationManager = ctx.getSystemService(NotificationManager.class);
		notificationManager.createNotificationChannel(channel);
	}
}