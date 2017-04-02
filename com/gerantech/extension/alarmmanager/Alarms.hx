package com.gerantech.extension.alarmmanager;

/**
 * ...
 * @author Mansour Djawadi
 */

 
#if cpp
	import cpp.Lib;
#elseif neko
#end

#if (android && openfl)
	import openfl.utils.JNI;
#end

 
class Alarms 
{
	#if (android && openfl)
	private static var notify_jni = JNI.createStaticMethod ("com.gerantech.extension.alarms.AlarmsExtension", "notification", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;IIIZ)I");
	private static var invoke_jni = JNI.createStaticMethod ("com.gerantech.extension.alarms.AlarmsExtension", "invokeApp", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;IIIZ)I");
	private static var params_jni = JNI.createStaticMethod ("com.gerantech.extension.alarms.AlarmsExtension", "getParams", "()Ljava/lang/String;");
	#end

	/**
	 * 
	 * @param	ticker : notification ticker
	 * @param	title : notification title
	 * @param	message : notification body
	 * @param	time : notification time in miliseconds
	 * @param	interval : loop time in miliseconds[zero value for disable interval] 
	 * @param	info : notification info
	 * @param	data : camma separated data (you can retrieve in getparams method
	 * @param	iconURL : notification icon url
	 * @param	soundURL : notification sound url
	 * @param	clearPreviouses : cancel and delete all previous notification
	 * @return id of notification for canceling
	 */
	public static function scheduleLocalNotification(ticker:String, title:String, message:String, time:Float, interval:Float=0, info:String="", data:String="", iconURL:String="", soundURL:String="", clearPreviouses:Bool=false):Int
	{
		#if (android && openfl)
		return notify_jni(ticker, title, message, info, data, iconURL, soundURL, cast(time,Int), cast(interval,Int), -2, clearPreviouses);
		#end
	}
	
	
	/**
	 * @param	notificationID : Cancel previous notification by id<br>if id equals -1 all notifications have been canceled.
	 */
	public static function cancelLocalNotifications(notificationID:Int = -1):Void
	{
		#if (android && openfl)
		notify_jni("", "", "", "", "", "", "", 0, 0, notificationID, false);
		#end
	}		
	
	/**
	 * 
	 * @param	scheme : as app://arg0=1&arg1=2
	 * @param	data : you can use camma seprated data
	 * @param	time : in miliseconds
	 * @param	interval : in miliseconds too loop [zero value for disable interval]
	 * @param	clearPreviouses : clear all invokes before invoke
	 * @return id of invoke for canceling
	 */
	public static function invokeAppScheme(scheme:String, data:String, time:Float, interval:Float=0, clearPreviouses:Bool=false):Int
	{
		#if (android && openfl)
		return invoke_jni(scheme, "", data, cast(time,Int), cast(interval,Int), -2, clearPreviouses);
		#end
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
	public static function invokeApp(packageName:String, data:String, time:Float, interval:Float=0, clearPreviouses:Bool=false):Int
	{
		#if (android && openfl)
		return invoke_jni("", packageName, data, cast(time,Int), cast(interval,Int), -2, clearPreviouses);
		#end
	}

	/**
	 * @param	scheduleID (if is -1 all invoke will be canceled.)
	 */
	public static function cancelInvokeApp(scheduleID:Int = -1):Void
	{
		#if (android && openfl)
		invoke_jni("", "", "", 0, 0, scheduleID, false);
		#end
	}

	/**
	 * @return return all sent comma separated data as json string (you can retrieve your data in invoked app)
	 */
	public static function getParams():String
	{
		#if (android && openfl)
		return params_jni();
		#end
	}
}