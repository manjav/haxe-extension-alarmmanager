package com.gerantech.extension.alarms;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

public class SimpleNotification {

	@SuppressLint("NewApi")
	public static void notify(Context context, int id, String ticker, String title, String text, String info, String data, String icon, String sound)
	{
        Intent myintent;
		try {

			Log.i(AlarmsExtension.LOG_TAG, id + " " + ticker + " " + title + " " + text + " " + info + " " + icon + " " + sound);
			//Log.i(AlarmsExtension.LOG_TAG, AlarmsExtension.packageName + " " + AlarmsExtension.mainActivity.getLocalClassName());
            myintent = new Intent(context, Class.forName(context.getPackageName() + ".MainActivity"));// + ".AppEntry"

			// put data for host app
			int s=0;
			for(String d : data.split(","))
				myintent.putExtra("arg"+(s++), d);
			myintent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

            //use the flag FLAG_UPDATE_CURRENT to override any notification already there
			PendingIntent contentIntent = PendingIntent.getActivity(context, 0, myintent, PendingIntent.FLAG_UPDATE_CURRENT);

			NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		    Notification.Builder builder = new Notification.Builder(context);

			PackageManager pm = context.getPackageManager();
			ApplicationInfo applicationInfo = pm.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
			Resources resources = pm.getResourcesForApplication(applicationInfo);

			builder.setContentIntent(contentIntent)
		                .setSmallIcon(applicationInfo.icon)
		                .setWhen(System.currentTimeMillis())
		                .setAutoCancel(true)
		                .setDefaults(Notification.DEFAULT_ALL)
		                .setTicker(ticker)
		                .setContentTitle(title)
		                .setContentText(text)
		                .setContentInfo(info);

			if (icon.isEmpty())
				builder.setLargeIcon(BitmapFactory.decodeResource(resources, applicationInfo.icon));
			else
				builder.setLargeIcon(BitmapFactory.decodeFile(icon));

			if(!sound.isEmpty())
				 builder.setSound(Uri.parse(sound));

			Notification notification = builder.build();
			notificationManager.notify(id, notification);
		}
		catch (Exception e)
		{
			Log.e(AlarmsExtension.LOG_TAG, e.toString());
			e.printStackTrace();
		}
	}
}