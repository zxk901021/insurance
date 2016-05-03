package com.example.market.utils;


import com.example.market.R;
import com.example.market.activity.InsuranceDetailActivity;
import com.example.market.activity.LoginActivity;
import com.example.market.activity.MainActivity;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class NotificationUtil {

	@SuppressLint("NewApi") public static void notificationMethod(Context context){
		NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		PendingIntent pending = PendingIntent.getActivity(context, 0, new Intent(context, InsuranceDetailActivity.class), 0);
		Notification newNotification = new Notification.Builder(context).setSmallIcon(R.drawable.ic_launcher)
				.setTicker("AAAAAA")
				.setContentText("交通工具意外险")
				.setContentTitle("无忧行天下")
				.setContentIntent(pending)
				.setNumber(1)
				.build();
		newNotification.flags |= Notification.FLAG_AUTO_CANCEL;
		manager.notify(1, newNotification);
//		Notification notification = new Notification();
//		notification.icon = R.drawable.ic_launcher;
//		notification.tickerText = "Hello world ! Welcome to the earth!";
//		notification.when = System.currentTimeMillis();
//		notification.setLatestEventInfo(context, "Notification Title!", "Notification Content!", pending);
//		notification.number = 1;
//		notification.flags |= Notification.FLAG_AUTO_CANCEL;
//		manager.notify(1, notification);
	}
	
}
