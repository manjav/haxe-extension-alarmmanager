package com.gerantech.extension.alarmmanager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import org.json.JSONObject;

public class SimpleNotification {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void notify(Context context, int id, String title, String text, String data) {
        try {
//            Log.i(AlarmsExtension.LOG_TAG, id + " " + ticker + " " + title + " " + text + " " + info + " " + icon + " " + sound);
            Intent myIntent = new Intent(context, Class.forName(context.getPackageName() + ".MainActivity"));// + ".AppEntry"

            // put icon for host app
            String ticker = null;
            String icon = null;
            String sound = null;
            String contentInfo = null;
            String channel = id + "-channel";
            String lightColor = "#FF00FF00";
            String vibrationPattern = "100,350,500";
            if (data != null) {
                myIntent.putExtra("data", data);

                // Create extra options
                JSONObject jsonObject = new JSONObject(data);
                ticker = jsonObject.has("ticker") ? jsonObject.getString("ticker") : null;
                icon = jsonObject.has("icon") ? jsonObject.getString("icon") : null;
                sound = jsonObject.has("sound") ? jsonObject.getString("sound") : null;
                channel = jsonObject.has("channel") ? jsonObject.getString("channel") : id + "-channel";
                contentInfo = jsonObject.has("contentInfo") ? jsonObject.getString("contentInfo") : null;
                lightColor = jsonObject.has("lightColor") ? jsonObject.getString("lightColor") : "#FF00FFFF";
                vibrationPattern = jsonObject.has("vibrationPattern") ? jsonObject.getString("vibrationPattern") : "100,350,500";
            }

            myIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

            //use the flag FLAG_UPDATE_CURRENT to override any notification already there
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationChannel notificationChannel = new NotificationChannel(channel, channel, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setShowBadge(true);
            notificationManager.createNotificationChannel(notificationChannel);

            PackageManager pm = context.getPackageManager();
            ApplicationInfo applicationInfo = pm.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Resources resources = pm.getResourcesForApplication(applicationInfo);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, notificationChannel.getId())
                    .setSmallIcon(applicationInfo.icon)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(text))
                    .setContentIntent(contentIntent)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            if (contentInfo != null && !contentInfo.isEmpty())
                builder.setContentInfo(contentInfo);

            if (ticker != null && !ticker.isEmpty())
                builder.setTicker(ticker);

            if (icon != null && !icon.isEmpty())
                builder.setLargeIcon(BitmapFactory.decodeFile(icon));
            else
                builder.setLargeIcon(BitmapFactory.decodeResource(resources, applicationInfo.icon));

            if (sound != null && !sound.isEmpty())
                builder.setSound(Uri.parse(sound));

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.parseColor(lightColor));

            String[] vibrates = vibrationPattern.split(",");
            if( vibrates.length > 0){
                notificationChannel.enableVibration(true);
                long[] vibratesPattern = new long[vibrates.length];
                for (int i = 0; i <vibrates.length ; i++)
                    vibratesPattern[i] = Long.parseLong(vibrates[i]);
                notificationChannel.setVibrationPattern(vibratesPattern);
            }
            
            NotificationManagerCompat.from(context).notify(id, builder.build());
        } catch (Exception e) {
            Log.e(AlarmsExtension.LOG_TAG, e.toString());
            e.printStackTrace();
        }
    }
}