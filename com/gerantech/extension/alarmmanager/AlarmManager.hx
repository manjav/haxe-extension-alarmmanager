package com.gerantech.extension.alarmmanager;

/**
 * ...
 * @author Mansour Djawadi
 */
#if (android && openfl)
	import lime.system.JNI;
#end

class AlarmManager {
	#if (android && openfl)
	static var pn = "com.gerantech.extension.alarmmanager.AlarmsExtension";
	static var notify_jni = JNI.createStaticMethod(pn, "notify", "(Ljava/lang/String;Ljava/lang/String;IIIZLjava/lang/String;)I");
	static var invoke_jni = JNI.createStaticMethod(pn, "invokeApp", "(Ljava/lang/String;Ljava/lang/String;IIIZLjava/lang/String;)I");
	static var params_jni = JNI.createStaticMethod(pn, "getParams", "()Ljava/lang/String;");
	#end

	/**
	 * 
	 * @param	title : notification title
	 * @param	text : notification body
	 * @param	time : notification time in miliseconds
	 * @param	interval : loop time in miliseconds[zero value for disable interval] 
	 * @param	clearPrevious : cancel and delete all previous notification
	 * @param	data : camma separated data (you can retrieve in getparams method
	 * @return id of notification for canceling
	 */
	public static function scheduleLocalNotification(title:String, text:String, time:Float, interval:Float = 0, clearPrevious:Bool = false, data:String = ""):Int {
		#if (android && openfl)
		return notify_jni(title, text, Math.round(time), Math.round(interval), -2, clearPrevious, data);
		#end
		return -1;
	}
	
	/**
	 * @param	notificationID : Cancel previous notification by id<br>if id equals -1 all notifications have been canceled.
	 */
	public static function cancelLocalNotifications(notificationId:Int = -1):Void {
		#if (android && openfl)
		notify_jni("", "", 0, 0, notificationId, false, "");
		#end
	}		
	
	/**
	 * 
	 * @param	scheme : as app://arg0=1&arg1=2
	 * @param	time : in miliseconds
	 * @param	interval : in miliseconds too loop [zero value for disable interval]
	 * @param	clearPrevious : clear all invokes before invoke
	 * @param	data : you can use camma seprated data
	 * @return id of invoke for canceling
	 */
	public static function invokeAppScheme(scheme:String, time:Float, interval:Float = 0, clearPrevious:Bool = false, data:String = ""):Int {
		#if (android && openfl)
		return invoke_jni(scheme, "", Math.round(time), Math.round(interval), -2, clearPrevious, data);
		#end
		return -1;
	}

	/**
	 * 
	 * @param	packageName : as com.company.product
	 * @param	data : you can use camma seprated data
	 * @param	time : in miliseconds
	 * @param	interval : in miliseconds too loop [zero value for disable interval]
	 * @param	clearPreviouses : clear all invokes before invoke
	 * @return id of invoke for canceling
	 */
	public static function invokeApp(packageName:String, time:Float, interval:Float = 0, clearPrevious:Bool = false, data:String = ""):Int {
		#if (android && openfl)
		return invoke_jni("", packageName, Math.round(time), Math.round(interval), -2, clearPrevious, data);
		#end
		return -1;
	}

	/**
	 * @param	scheduleID (if is -1 all invoke will be canceled.)
	 */
	public static function cancelInvokeApp(scheduleID:Int = -1):Void {
		#if (android && openfl)
		invoke_jni("", "", 0, 0, scheduleID, false, "");
		#end
	}

	/**
	 * @return return all sent comma separated data as json string (you can retrieve your data in invoked app)
	 */
	public static function getParams():String {
		#if (android && openfl)
		return params_jni();
		#end
		return "{}";
	}
}
