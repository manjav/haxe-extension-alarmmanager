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
import com.gerantech.extension.alarmmanager.Alarms;
```
#Schedule local notification

```Haxe
// notify every day in this time:
Alarms.scheduleLocalNotification("notification ticker", "notification title", "notification message", Date.now().getTime() + 3000, DateTools.days(1), "notification info", "comma,sepatated,args,to,retrieve,after,notification.touched");


// cancel and delete notification by id:
Alarms.cancelLocalNotifications( -1);
```

#Invoke app

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
// run app every day in this time:
Alarms.invokeApp("com.company.product", "welcome,to,haxe", Date.now().getTime() + 10000, DateTools.days(1));
```