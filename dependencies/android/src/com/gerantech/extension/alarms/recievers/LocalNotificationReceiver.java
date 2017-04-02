package com.gerantech.extension.alarms.recievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.gerantech.extension.alarms.SimpleNotification;

/**
 * @author Mansour Djawadi
 *
 */

public class LocalNotificationReceiver extends BroadcastReceiver 
{
	public void onReceive(Context context, Intent intent)
    {   
		Bundle bundle = intent.getExtras();
        int notiID = bundle.getInt("id");
        String notiTicker = bundle.getString("ticker");
        String notiTitle = bundle.getString("title");
        String notiText = bundle.getString("text");
        String notiInfo = bundle.getString("info");
        String notiData = bundle.getString("data");
        String notiIcon = bundle.getString("icon");
        String notiSound = bundle.getString("sound");
        
        SimpleNotification.notify(context, notiID, notiTicker, notiTitle, notiText, notiInfo, notiData, notiIcon, notiSound);
        
        //Log.i(AlarmsExtension.LOG_TAG, "{\"id\":\""+notiID+"\",\"ticker\":\""+notiTicker+"\",\"title\":\""+notiTitle+"\",\"text\":\""+notiText+"\",\"info\":\""+notiInfo+"\",\"data\":"+notiData+"}"+ notiIcon+ notiSound);
		/*if( extensionContext != null )
			extensionContext.dispatchStatusEventAsync("LocalNotificationReceived", "{\"id\":\""+notiID+"\",\"ticker\":\""+notiTicker+"\",\"title\":\""+notiTitle+"\",\"text\":\""+notiText+"\",\"info\":\""+notiInfo+"\",\"data\":"+notiData+"}");
*/
		//AlarmsManager.cancel(context, LocalNotificationReceiver.class, notiID);
    }
}