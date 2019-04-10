package com.gerantech.extension.alarmmanager.recievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.gerantech.extension.alarmmanager.AlarmsExtension;
import com.gerantech.extension.alarmmanager.SimpleNotification;

/**
 * @author Mansour Djawadi
 */

public class LocalNotificationReceiver extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onReceive(Context context, Intent intent) {
        Bundle b = intent.getExtras();
        SimpleNotification.notify(context, b.getInt("id"), b.getString("title"), b.getString("text"), b.getString("data"));
    }
}