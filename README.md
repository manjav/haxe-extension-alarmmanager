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
Exposed methods are currently:

```Haxe
Alarms.scheduleLocalNotification("ticker", "title", "message", Date.now().getTime() + 3000, 0, "", "hello");
Alarms.cancelLocalNotifications( -1);
Alarms.invokeApp("com.tod.xameen", "welcome", Date.now().getTime() + 10000, 0);
//Alarms.invokeAppScheme("testapp://testoo?a=1&b=2", "welcome", Date.now().getTime() + 10000, 0);
```