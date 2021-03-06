extension-nativefunctions
=============

Simple OpenFL extension for Android to schedule local notification and schedule launch app.


Install via 
`haxelib git extension-alarmmanager https://github.com/manjav/haxe-extension-alarmmanager`

Add to `project.xml`:

```xml
<haxelib name="extension-alarmmanager" if="android" />
```

And import into your project (haxe) with:
  
```Haxe
import com.gerantech.extension.alarmmanager.AlarmManager;
```
# Schedule Local Notification

```Haxe
// notify in 3 seconds later time every day :
AlarmManager.scheduleLocalNotification("notification ticker", "notification title", "notification message", Date.now().getTime() + 3000, DateTools.days(1), "notification info", "comma,sepatated,args,to,retrieve,after,notification.touched");


// cancel and delete notification by id:
AlarmManager.cancelLocalNotifications( -1);
```

# Invoke App

Note: In order to use invoke app, you must replace manifest:<br/>
Step 1: Add manifest template to `project.xml`
```XML
<template path="templates/AndroidManifest.xml" rename="app/src/main/AndroidManifest.xml" />
```

Step 1: Create templates folder in root of project and create manifest inside the template folder

```XML
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android" 
		  package="::APP_PACKAGE::" 
		  android:versionCode="::APP_BUILD_NUMBER::" 
		  android:versionName="::APP_VERSION::" 
		  android:installLocation="::ANDROID_INSTALL_LOCATION::">
	
	<uses-feature android:glEsVersion="0x00020000" android:required="true" />
	<uses-feature android:name="android.hardware.touchscreen" android:required="false" />
	
	::if (ANDROID_PERMISSIONS != null)::::foreach ANDROID_PERMISSIONS::<uses-permission android:name="::__current__::" />
	::end::::end::
	
	<uses-sdk android:minSdkVersion="::ANDROID_MINIMUM_SDK_VERSION::" android:targetSdkVersion="::ANDROID_TARGET_SDK_VERSION::"/>
	
	<application 
		android:label="::APP_TITLE::" 
		::if (HAS_ICON):: android:icon="@drawable/icon"::end:: 
		android:allowBackup="true" android:theme="@android:style/Theme.NoTitleBar.Fullscreen" android:hardwareAccelerated="true">
		
		<activity android:name="MainActivity" android:launchMode="singleTask" 
				android:label="::APP_TITLE::" 
				::if (WIN_ORIENTATION=="portrait"):: android:screenOrientation="sensorPortrait"::end::::if (WIN_ORIENTATION=="landscape"):: android:screenOrientation="sensorLandscape"::end::
				android:configChanges="keyboardHidden|orientation|screenSize|screenLayout"
				android:showOnLockScreen="true">
			
			<intent-filter>
				<action android:name="android.intent.action.MAIN" />
				<category android:name="android.intent.category.LAUNCHER" />
				<category android:name="tv.ouya.intent.category.GAME" />
			</intent-filter>
			
		</activity>
		
	</application>
	
</manifest>
```

```Haxe
// run app ten secondes later every day:
AlarmManager.invokeApp("com.company.product", "welcome,to,haxe", Date.now().getTime() + 10000, DateTools.days(1));
```

If you want retrieve data ("welcome,to,haxe in launched app you can use `getParams` method in `new` or `main` function in target app<br/>
Dont forget if you want run target app when screen locked, `showOnLockScreen` attribute in template manifest must be true 
```Haxe
public function new()
{
	super();
	trace(AlarmManager.getParams());//JsonString  {[{"arg0":"welcome"}, {"arg1":"to"}, {"arg2":"haxe"}]}
	//var data:Dynamic = JsonParser.parse(AlarmManager.getParams());
}
```

# Invoke App via Scheme

Add filter and data to MainActivity in manifest template
```XML
<intent-filter>
	<action android:name="android.intent.action.VIEW"/>
	<category android:name="android.intent.category.BROWSABLE"/>
	<category android:name="android.intent.category.DEFAULT"/>
	<data android:scheme="testapp"/>
</intent-filter>
```

Now your manifest ready
```XML
<activity android:name="MainActivity" android:launchMode="singleTask" 
		android:label="::APP_TITLE::" 
		::if (WIN_ORIENTATION=="portrait"):: android:screenOrientation="sensorPortrait"::end::::if (WIN_ORIENTATION=="landscape"):: android:screenOrientation="sensorLandscape"::end::
		android:configChanges="keyboardHidden|orientation|screenSize|screenLayout"
		android:showOnLockScreen="true">
	
	<intent-filter>
		<action android:name="android.intent.action.MAIN" />
		<category android:name="android.intent.category.LAUNCHER" />
		<category android:name="tv.ouya.intent.category.GAME" />
	</intent-filter>
	<intent-filter>
		<action android:name="android.intent.action.VIEW"/>
		<category android:name="android.intent.category.BROWSABLE"/>
		<category android:name="android.intent.category.DEFAULT"/>
		<data android:scheme="testapp"/>
	</intent-filter>
</activity>

```

```Haxe
// run app ten secondes later one time:
AlarmManager.invokeAppScheme("testapp://testoo?a=1&b=2", "welcome,to,haxe", Date.now().getTime() + 10000, 0);
```
As local notification you can cancel invokes by:
```Haxe
// cancel invoke by id:
AlarmManager.cancelInvokeApp();
```
