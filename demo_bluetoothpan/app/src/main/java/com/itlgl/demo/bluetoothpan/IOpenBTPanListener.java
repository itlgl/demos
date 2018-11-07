package com.itlgl.demo.bluetoothpan;

public interface IOpenBTPanListener {
    void onBluetoothPanOpen();
    void onBluetoothPanClose();
    void onFailure();
}
