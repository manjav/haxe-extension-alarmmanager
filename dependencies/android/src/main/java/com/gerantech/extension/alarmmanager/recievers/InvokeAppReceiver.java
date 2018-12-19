/**
 * 
 */
package com.gerantech.extension.alarmmanager.recievers;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.gerantech.extension.alarmmanager.AlarmsExtension;

/**
 * @author Mansour Djawadi
 *
 */

public class InvokeAppReceiver extends BroadcastReceiver 
{

    @TargetApi(11)
    public void onReceive(Context context, Intent intent)
    {
		Bundle bundle = intent.getExtras();
        String packageName = bundle.getString("packageName");
        String scheme = bundle.getString("scheme");
        String data = bundle.getString("data");
        //Log.i(AlarmsExtension.LOG_TAG, "packageName "+ packageName + "  scheme: " + scheme + "  data: " + data);
        if(packageName.length() > 2)
        {
    		try
            {
                Intent i = context.getPackageManager().getLaunchIntentForPackage(packageName);
                if (i == null)
                {
                	Log.e(AlarmsExtension.LOG_TAG, "PackageManager.NameNotFoundException");
                	return;
                }
                i.setAction(Intent.ACTION_MAIN);

                // put data for host app

                int s=0;
                for(String d : data.split(","))
                    i.putExtra("arg"+(s++), d);

    			context.startActivity(i);
            }
            catch (Exception e)
            {
    			Log.e(AlarmsExtension.LOG_TAG, e.getMessage());
    			e.printStackTrace();
            }
    	}
        else if( scheme.length() > 2)
        {
			try
			{
				Intent i = Intent.parseUri(scheme, Intent.URI_INTENT_SCHEME);
                i.addCategory(Intent.CATEGORY_BROWSABLE);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
                i.setComponent(null);
                i.putExtra("data", bundle.getString("data"));
                context.startActivity(i);
            }
			catch (Exception e)
			{
                Log.e(AlarmsExtension.LOG_TAG, e.getMessage());
				e.printStackTrace();
			}
        }
    }
}