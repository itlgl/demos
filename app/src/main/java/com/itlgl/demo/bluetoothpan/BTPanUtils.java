package com.itlgl.demo.bluetoothpan;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class BTPanUtils {
    private static final String TAG = "BTPanUtils";
    private AtomicReference<Object> bluetoothPan = new AtomicReference<Object>();
    private BluetoothAdapter bluetoothAdapter;
    private Context context;

    // private IOpenBTPanListener openBTPanListener;
    private ILogCallback logCallback;

    public void init(Context context) {
        this.context = context;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothAdapter.getProfileProxy(context.getApplicationContext(), profileServiceListener,
                5/*BluetoothProfile.PAN*/);
    }

    public void setLogCallback(ILogCallback logCallback) {
        this.logCallback = logCallback;
    }

    private BluetoothProfile.ServiceListener profileServiceListener = new BluetoothProfile.ServiceListener() {

        @Override
        public void onServiceConnected(int profile, BluetoothProfile proxy) {
            Log.i("BTPanUtils", "onServiceConnected,proxy=" + proxy);
            log("onServiceConnected,proxy=" + proxy);
            bluetoothPan.set(proxy);
        }

        @Override
        public void onServiceDisconnected(int profile) {
            Log.i("BTPanUtils", "onServiceDisconnected");
            log("onServiceDisconnected");
            bluetoothPan.set(null);
        }
    };

//    public void openBluetoothPan(IOpenBTPanListener listener) {
//        this.openBTPanListener = listener;
//        bluetoothAdapter.setName("HengbaoPhone");
//        try {
//            Object bluetoothPanObj = bluetoothPan.get();
//            if (bluetoothPanObj != null) {
//                Log.i("BTPanUtils", "IsTetheringOn=" + BluetoothPanUtils.isTetheringOn(bluetoothPanObj));
//                log("IsTetheringOn=" + BluetoothPanUtils.isTetheringOn(bluetoothPanObj));
//                BluetoothPanUtils.setBluetoothTethering(bluetoothPanObj, true);
//                boolean isTetheringOn = BluetoothPanUtils.isTetheringOn(bluetoothPanObj);
//                Log.i("BTPanUtils", "IsTetheringOn=" + isTetheringOn);
//                log("IsTetheringOn=" + isTetheringOn);
//                if(openBTPanListener != null) {
//                    if(isTetheringOn) {
//                        openBTPanListener.onBluetoothPanOpen();
//                    } else {
//                        openBTPanListener.onBluetoothPanClose();
//                    }
//                }
//            } else {
//                log("bluetoothPanObj 为空！");
//                openBTPanListener.onFailure();
//            }
//        } catch (Exception e) {
//            Log.i("BTPanUtils", "openBTPan Exception", e);
//            log("openBTPan Exception " + e);
//            openBTPanListener.onFailure();
//        }
//    }
//
//    public void closeBluetoothPan(IOpenBTPanListener listener) {
//        this.openBTPanListener = listener;
//        bluetoothAdapter.setName("HengbaoPhone");
//        try {
//            Object bluetoothPanObj = bluetoothPan.get();
//            if (bluetoothPanObj != null) {
//                Log.i("BTPanUtils", "IsTetheringOn=" + BluetoothPanUtils.isTetheringOn(bluetoothPanObj));
//                log("IsTetheringOn=" + BluetoothPanUtils.isTetheringOn(bluetoothPanObj));
//                BluetoothPanUtils.setBluetoothTethering(bluetoothPanObj, false);
//                boolean isTetheringOn = BluetoothPanUtils.isTetheringOn(bluetoothPanObj);
//                Log.i("BTPanUtils", "IsTetheringOn=" + isTetheringOn);
//                log("IsTetheringOn=" + isTetheringOn);
//                if(openBTPanListener != null) {
//                    if(isTetheringOn) {
//                        openBTPanListener.onBluetoothPanOpen();
//                    } else {
//                        openBTPanListener.onBluetoothPanClose();
//                    }
//                }
//            } else {
//                log("bluetoothPanObj 为空！");
//                openBTPanListener.onFailure();
//            }
//        } catch (Exception e) {
//            Log.i("BTPanUtils", "closeBTPan Exception", e);
//            log("closeBTPan Exception " + e);
//            openBTPanListener.onFailure();
//        }
//    }

    public boolean isTetheringOn() throws Exception {
        Object bluetoothPanObj = bluetoothPan.get();
        if(bluetoothPanObj == null) {
            log("bluetoothPanObj 为空！");
            throw new Exception("bluetoothPanObj 为空！");
        }
        return BluetoothPanUtils.isTetheringOn(bluetoothPanObj);
    }

    public boolean setBluetoothTethering(boolean flag) throws Exception {
        Object bluetoothPanObj = bluetoothPan.get();
        if(bluetoothPanObj == null) {
            log("bluetoothPanObj 为空！");
            throw new Exception("bluetoothPanObj 为空！");
        }
        BluetoothPanUtils.setBluetoothTethering(bluetoothPanObj, flag);
        return true;
    }

    public boolean connectBTPan(BluetoothDevice device) throws Exception {
        Object bluetoothPanObj = bluetoothPan.get();
        if(bluetoothPanObj == null) {
            log("bluetoothPanObj 为空！");
            throw new Exception("bluetoothPanObj 为空！");
        }
        List<BluetoothDevice> sinks = BluetoothPanUtils.getConnectedDevices(bluetoothPanObj);
        if (sinks != null) {
            for (BluetoothDevice sink : sinks) {
                BluetoothPanUtils.disconnect(bluetoothPanObj, sink);
            }
        }
        return BluetoothPanUtils.connect(bluetoothPanObj, device);
    }

    void log(String msg) {
        if(logCallback != null) {
            logCallback.log(msg);
        }
    }
}
