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

    public static int notify(String ticker, String title, String text, String info, String data, String icon, String sound, int time, int interval, int id, boolean clearPreviouses) {
        Bundle bundle = new Bundle();
        bundle.putString("ticker", ticker);
        bundle.putString("title", title);
        bundle.putString("text", text);
        bundle.putString("info", info);
        bundle.putString("data", data);
        return alarm(mainContext, LocalNotificationReceiver.class, bundle, time, interval, id, clearPreviouses);
    }

    public static int invokeApp(String scheme, String packageName, String data, int time, int interval, int id, boolean clearPreviouses) {
        Bundle bundle = new Bundle();
        bundle.putString("scheme", scheme);
        bundle.putString("packageName", packageName);
        bundle.putString("data", data);

        return alarm(mainContext, InvokeAppReceiver.class, bundle, time, interval, id, clearPreviouses);
    }

    private static int alarm(Context context, Class<?> cls, Bundle bundle, int time, int interval, int id, boolean clearPreviouses) {
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

        return AlarmsManager.schedule(context, cls, bundle, (long) time);
    }

    public static void cancel(Context context, int id) {
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

        // retrieve data
        intentData = "{[";
        Object[] keis = null;

        // retrieve bundle extra data and add to json string
        Bundle bundle = mainActivity.getIntent().getExtras();
        if (bundle != null) {
            keis = bundle.keySet().toArray();
            for (int step = 0; step < keis.length; step++) {
                String key = (String) keis[step];
                Object value = bundle.get(key);
                intentData += "{\"" + key + "\":\"" + value.toString() + "\"}";

                if (step < keis.length - 1)
                    intentData += ", ";
            }
        }

        // retrieve scheme query params and add to json string
        Uri data = mainActivity.getIntent().getData();
        if (data != null) {
            if (keis != null)
                intentData += ", ";

            keis = data.getQueryParameterNames().toArray();
            for (int step = 0; step < keis.length; step++) {
                String key = (String) keis[step];
                Object value = data.getQueryParameter(key);
                intentData += "{\"" + key + "\":\"" + value.toString() + "\"}";

                if (step < keis.length - 1)
                    intentData += ", ";
            }
        }

        intentData += "]}";
    }

    public static String getParams() {
        return intentData;
    }
}