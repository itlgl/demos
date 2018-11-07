# TestSendApduToSIMByTelephonyManager

使用TelephonyManager在已root的5.0以上版本的手机上给SIM卡发送apdu

## 如何使用

#### 1. 将编译完成的apk放到/system/priv-app目录下，重启手机。
由于system下有访问权限的问题，可以使用RE管理器操作，先将apk使用adb push到sdcard上面，再通过RE管理器复制到/system/priv-app目录下。
Android4.4开始只有priv-app下的app拥有系统权限（https://github.com/android-cn/android-discuss/issues/36），4.4之前的手机放到/system/app目录下。
复制完成以后重启手机。

#### 2. app需要声明权限
```xml
<uses-permission android:name="android.permission.MODIFY_PHONE_STATE"
        tools:ignore="ProtectedPermissions" />
```

## 截图和log

![截图](https://github.com/itlgl/demos/raw/master/TestSendApduToSIMByTelephonyManager/screenshot/1.png)

部分log：
```console
08-15 17:43:53.972 2671-2671/com.itlgl.test I/test: 打开成功:Channel: 2 Status: 1
08-15 17:43:59.912 2671-2671/com.itlgl.test I/test: apdu --> 00a4040000
08-15 17:43:59.922 2671-2671/com.itlgl.test I/test: apdu <-- 6f00
08-15 17:45:24.472 2671-2671/com.itlgl.test I/test: apdu --> 00a404000fA0000005591010FFFFFFFF8900000100
08-15 17:45:24.552 2671-2671/com.itlgl.test I/test: apdu <-- 6121
08-15 17:45:56.362 2671-2671/com.itlgl.test I/test: apdu --> 00c00021
08-15 17:45:56.372 2671-2671/com.itlgl.test W/System.err: java.lang.StringIndexOutOfBoundsException: length=8; regionStart=8; regionLength=2
        at java.lang.String.startEndAndLength(String.java:504)
        at java.lang.String.substring(String.java:1333)
        at com.itlgl.test.MainActivity.btnTransmitApduClick(MainActivity.java:88)
        at com.itlgl.test.MainActivity_ViewBinding$3.doClick(MainActivity_ViewBinding.java:62)
        at butterknife.internal.DebouncingOnClickListener.onClick(DebouncingOnClickListener.java:22)
        at android.view.View.performClick(View.java:4785)
        at android.view.View$PerformClick.run(View.java:19869)
        at android.os.Handler.handleCallback(Handler.java:739)
        at android.os.Handler.dispatchMessage(Handler.java:95)
        at android.os.Looper.loop(Looper.java:155)
        at android.app.ActivityThread.main(ActivityThread.java:5696)
        at java.lang.reflect.Method.invoke(Native Method)
        at java.lang.reflect.Method.invoke(Method.java:372)
        at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:1028)
        at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:823)
08-15 17:45:56.382 2671-2671/com.itlgl.test I/test: 发送失败：java.lang.StringIndexOutOfBoundsException: length=8; regionStart=8; regionLength=2
08-15 17:46:14.432 2671-2671/com.itlgl.test I/test: apdu --> 00c0002100
08-15 17:46:14.442 2671-2671/com.itlgl.test I/test: apdu <-- 6f1f8410a0000005591010ffffffff8900000100a5049f6501ffe00582030202009000
```