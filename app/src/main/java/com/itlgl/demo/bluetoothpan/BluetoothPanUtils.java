package com.itlgl.demo.bluetoothpan;

import android.bluetooth.BluetoothDevice;

import java.lang.reflect.Method;
import java.util.List;

public class BluetoothPanUtils {
    public static boolean isTetheringOn(Object bluetoothPanObj) throws Exception {
        Class bluetoothPanClass = Class.forName("android.bluetooth.BluetoothPan");
        Method methodIsTetheringOn = bluetoothPanClass.getMethod("isTetheringOn");
        return (boolean) methodIsTetheringOn.invoke(bluetoothPanObj);
    }

    public static void setBluetoothTethering(Object bluetoothPanObj, boolean flag) throws Exception {
        Class bluetoothPanClass = Class.forName("android.bluetooth.BluetoothPan");
        Method methodSetBluetoothTethering = bluetoothPanClass.getMethod("setBluetoothTethering", boolean.class);
        methodSetBluetoothTethering.invoke(bluetoothPanObj, flag);
    }

    public static List<BluetoothDevice> getConnectedDevices(Object bluetoothPanObj) throws Exception {
        Class bluetoothPanClass = Class.forName("android.bluetooth.BluetoothPan");
        Method methodGetConnectedDevices = bluetoothPanClass.getMethod("getConnectedDevices");
        return (List<BluetoothDevice>) methodGetConnectedDevices.invoke(bluetoothPanObj);
    }

    public static void disconnect(Object bluetoothPanObj, BluetoothDevice device) throws Exception {
        Class bluetoothPanClass = Class.forName("android.bluetooth.BluetoothPan");
        Method methodDisconnect = bluetoothPanClass.getMethod("disconnect", BluetoothDevice.class);
        methodDisconnect.invoke(bluetoothPanObj, device);
    }

    public static boolean connect(Object bluetoothPanObj, BluetoothDevice device) throws Exception {
        Class bluetoothPanClass = Class.forName("android.bluetooth.BluetoothPan");
        Method methodDisconnect = bluetoothPanClass.getMethod("connect", BluetoothDevice.class);
        return (boolean) methodDisconnect.invoke(bluetoothPanObj, device);
    }
}
