package com.gerantech.extension.alarmmanager;

import java.util.Map;
import java.util.Random;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import java.util.Map;
import java.util.Random;

public class AlarmsManager {

    public static int schedule(Context context, Class<?> cls,  Bundle bundle, long t
        Intent intent = new Intent(context, cls);

        int id = getRandomID(context);
        bundle.putInt("id", id);
        intent.putExtras(bundle);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(AlarmsExtension.mainContext, id, intent, 0);
        AlarmManager alarmManager = (AlarmManager) AlarmsExtension.mainContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
        //Log.i(AlarmsExtension.LOG_TAG, cls.getSimpleName()+" "+ time+" "+ id);
        return id;
    }

    public static int setRepeating(Class<?> cls, Bundle bundle, long time, long interval) {
        Intent intent = new Intent(AlarmsExtension.mainContext, cls);

        int id = getRandomID();
        bundle.putInt("id", id);
        intent.putExtras(bundle);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(AlarmsExtension.mainContext, id, intent, 0);
        AlarmManager alarmManager = (AlarmManager) AlarmsExtension.mainContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, interval, pendingIntent); // Millisec * Second * Minute
        Log.i(AlarmsExtension.LOG_TAG, "setRepeating " + cls.getSimpleName() + " " + time + " " + interval + " " + id);
        return id;
    }

    public static void cancel(Context context, Class<?> cls, int id) {
        Intent intent = new Intent(context, cls);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        NotificationManager notificationManager = ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE));
        SharedPreferences sharedPreferences = context.getSharedPreferences("alarms", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        assert alarmManager != null;
        assert notificationManager != null;

        if (id == -1) {
            @SuppressWarnings("unchecked")
            Map<String, Boolean> messagesMap = (Map<String, Boolean>) sharedPreferences.getAll();
            for (Map.Entry<String, Boolean> entry : messagesMap.entrySet()) {
                //Log.w(AndroidExtension.LOG_TAG, entry.getKey()+" -> "+entry.getValue());
                id = Integer.parseInt(entry.getKey());
                pendingIntent = PendingIntent.getBroadcast(context, id, intent, 0);
                alarmManager.cancel(pendingIntent);
                notificationManager.cancel(Integer.parseInt(entry.getKey()));
            }
            editor.clear();
        } else {
            alarmManager.cancel(pendingIntent);
            editor.remove(id + "");
            notificationManager.cancel(id);
        }
        editor.apply();
        //Toast.makeText(context, notiID+" canceled", Toast.LENGTH_LONG).show();
    }

    public static void cancelAll(Context context, Class<?> cls) {
        cancel(context, cls, -1);
    }

    private static int getRandomID(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("alarms", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int ret = getRandomInt(sharedPreferences, editor);
        editor.putBoolean(ret + "", true);
        editor.apply();
        return ret;
    }

    private static int getRandomInt(SharedPreferences sharedPreferences, SharedPreferences.Editor editor) {
        Random r = new Random();
        int ret = r.nextInt(100);
        if (sharedPreferences.contains("" + ret)) {
            if (sharedPreferences.getBoolean("" + ret, false))
                editor.remove("" + ret);
            else
                ret = getRandomInt(sharedPreferences, editor);
        }
        return ret;
    }
}