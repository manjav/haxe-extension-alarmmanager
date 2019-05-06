package com.gerantech.extension.alarmmanager;

import android.app.NotificationManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;

import com.gerantech.extension.alarmmanager.recievers.InvokeAppReceiver;
import com.gerantech.extension.alarmmanager.recievers.LocalNotificationReceiver;

import org.haxe.extension.Extension;

public class AlarmsExtension extends Extension {
    public static final String LOG_TAG = "H.N.E";
    private static String intentData;

    public static int notify(String title, String text, int time, int interval, int id, boolean clearPrevious, String data) {
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("text", text);
        bundle.putString("data", data);
        return alarm(mainContext, LocalNotificationReceiver.class, time, interval, id, clearPrevious, bundle);
    }

    public static int invokeApp(String scheme, String packageName, int time, int interval, int id, boolean clearPrevious, String data) {
        Bundle bundle = new Bundle();
        bundle.putString("scheme", scheme);
        bundle.putString("packageName", packageName);
        bundle.putString("data", data);

        return alarm(mainContext, InvokeAppReceiver.class, time, interval, id, clearPrevious, bundle);
    }

    private static int alarm(Context context, Class<?> cls, long time, long interval, int id, boolean clearPrevious, Bundle bundle) {
        // Cancel previous notifications
        if (id != -2) {
            cancel(context, id);
            AlarmsManager.cancel(context, cls, id);
            return id;
        }

        // Clear previous notifications
        if (clearPrevious) {
            cancel(context, -1);
            AlarmsManager.cancelAll(context, cls);
        }

        return AlarmsManager.schedule(context, cls, time, interval, bundle);
    }

    public static void cancel(Context context, int id) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (id == -1)
            notificationManager.cancelAll();
        else
            notificationManager.cancel(id);
        //Log.i(LOG_TAG, "Notification canceled by "+id);
    }

    public void onCreate(Bundle savedInstanceState) {
        // for launch app when screen locked
        mainActivity.getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        // Retrieve data
        intentData = "{[";
        Object[] keys = null;

        // Retrieve bundle extra data and add to json string
        Bundle bundle = mainActivity.getIntent().getExtras();
        if (bundle != null) {
            keys = bundle.keySet().toArray();
            for (int step = 0; step < keys.length; step++) {
                String key = (String) keys[step];
                Object value = bundle.get(key);
                intentData += "{\"" + key + "\":\"" + value.toString() + "\"}";

                if (step < keys.length - 1)
                    intentData += ", ";
            }
        }

        // Retrieve scheme query params and add to json string
        Uri data = mainActivity.getIntent().getData();
        if (data != null) {
            if (keys != null)
                intentData += ", ";

            keys = data.getQueryParameterNames().toArray();
            for (int step = 0; step < keys.length; step++) {
                String key = (String) keys[step];
                Object value = data.getQueryParameter(key);
                intentData += "{\"" + key + "\":\"" + value.toString() + "\"}";

                if (step < keys.length - 1)
                    intentData += ", ";
            }
        }

        intentData += "]}";
    }

    public static String getParams() {
        return intentData;
    }
}