package com.itlgl.demo.bleperipheral;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.itlgl.java.util.ByteUtil;

import java.text.MessageFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BleHostActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SCAN_BLE = 1;
    private static final int REQUEST_PERMISSION = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    @BindView(R.id.tv_device_info)
    TextView tv_device_info;
    @BindView(R.id.tv_log)
    TextView tv_log;

    private String deviceName;
    private String deviceAddress;
    private BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothGatt bluetoothGatt;
    private BluetoothGattCharacteristic writeChannel;
    private BluetoothGattCharacteristic notifyChannel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble_host);
        ButterKnife.bind(this);
    }

    void initBleParam() {
        boolean isSupportBle = false;

        do {
            bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (bluetoothManager == null) {
                addLog("获取BluetoothManager失败");
                isSupportBle = false;
                break;
            }
            bluetoothAdapter = bluetoothManager.getAdapter();
            if (bluetoothAdapter == null) {
                addLog("获取BluetoothAdapter失败");
                isSupportBle = false;
                break;
            }

            if(!bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
            isSupportBle = true;
        } while(false);

        if(!isSupportBle) {
            addLog("设备不支持ble!");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkBLEPermission();
    }

    private void checkBLEPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSION);
            } else {
                initBleParam();
            }
        } else {
            initBleParam();
        }
    }

    @OnClick(R.id.tv_clear_log)
    void clearLog() {
        tv_log.setText("");
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.btn_scan)
    void btnScanClick() {
        startActivityForResult(new Intent(this, DeviceScanActivity.class), REQUEST_CODE_SCAN_BLE);
    }

    @OnClick(R.id.btn_connect)
    void btnConnectClick() {
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(deviceAddress);
        bluetoothGatt = device.connectGatt(this, false, bluetoothGattCallback);
    }

    @OnClick(R.id.btn_disconnect)
    void btnDisconnectClick() {
        if(bluetoothGatt != null) {
            bluetoothGatt.disconnect();
            addLog("断开连接...");
        } else {
            addLog("未连接设备");
        }
    }

    @OnClick(R.id.btn_open_notify)
    void btnOpenNotifyClick() {
        bluetoothGatt.setCharacteristicNotification(notifyChannel, true);
        BluetoothGattDescriptor descriptor = notifyChannel.getDescriptor(BLEPeripheral.UUID_DESCRIPTOR);
        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        bluetoothGatt.writeDescriptor(descriptor);
    }

    @OnClick(R.id.btn_write)
    void btnWriteClick() {
        writeChannel.setValue("hello".getBytes());
        bluetoothGatt.writeCharacteristic(writeChannel);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_SCAN_BLE:
                if(resultCode == RESULT_OK) {
                    deviceName = data.getStringExtra(DeviceScanActivity.EXTRAS_DEVICE_NAME);
                    deviceAddress = data.getStringExtra(DeviceScanActivity.EXTRAS_DEVICE_ADDRESS);
                    tv_device_info.setText("name=" + deviceName + ",address=" + deviceAddress);
                    addLog("扫描设备信息：\nname=" + deviceName + "mac=" + deviceAddress);
                }
                break;
            case REQUEST_ENABLE_BT:
                if(resultCode == Activity.RESULT_OK) {
                    addLog("蓝牙已打开");
                } else {
                    addLog("打开蓝牙失败");
                }
                break;
        }
    }

    private BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            BluetoothDevice device = gatt.getDevice();
            addLogInThread(MessageFormat.format("[onConnectionStateChange] device={0},status={1},newState={2}",
                    device.getAddress(), status, newState));
            switch (newState) {
                case BluetoothProfile.STATE_CONNECTING:
                    addLogInThread(String.format("蓝牙[%s]正在连接中", device.getAddress()));
                    break;
                case BluetoothProfile.STATE_CONNECTED:
                    addLogInThread(String.format("蓝牙[%s]已经连接", device.getAddress()));
                    // 如果正在进行连接操作，后续进行发现服务的操作，发现服务失败了直接断开连接
                    addLogInThread("开始发现服务");
                    boolean ret = gatt.discoverServices();
                    if (!ret) {
                        addLogInThread(String.format("扫描设备[%s]服务失败!", device.getAddress()));
                        btnDisconnectClick();
                    }
                    break;
                case BluetoothProfile.STATE_DISCONNECTING:
                    addLogInThread(String.format("蓝牙[%s]连接正在断开中", device.getAddress()));
                    break;
                case BluetoothProfile.STATE_DISCONNECTED:
                    addLogInThread(String.format("蓝牙[%s]连接已经断开", device.getAddress()));
                    bluetoothGatt.close();
                    addLogInThread("bluetoothGatt.close()");
                    break;
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            BluetoothDevice device = gatt.getDevice();
            addLogInThread(MessageFormat.format("[onServicesDiscovered] device={0},status={1}",
                    device.getAddress(), status));
            BluetoothGattService service = gatt.getService(BLEPeripheral.UUID_SERVICE);
            writeChannel = service.getCharacteristic(BLEPeripheral.UUID_CHAR_WRITE);
            notifyChannel = service.getCharacteristic(BLEPeripheral.UUID_CHAR_NOTIFY);
            addLogInThread("找到write通道和notify通道");
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
            BluetoothDevice device = gatt.getDevice();
            addLogInThread(MessageFormat.format("[onDescriptorWrite] device={0},status={1}",
                    device.getAddress(), status));
            if(status == BluetoothGatt.GATT_SUCCESS && descriptor.getUuid().equals(BLEPeripheral.UUID_DESCRIPTOR)) {
                byte[] value = descriptor.getValue();
                switch (value[0]) {
                    case 0:
                        addLogInThread("关闭notify写成功");
                        break;
                    case 1:
                        addLogInThread("打开notify写成功");
                        break;
                    case 2:
                        addLogInThread("关闭indicate写成功");
                        break;
                }
            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            BluetoothDevice device = gatt.getDevice();
            addLogInThread(MessageFormat.format("[onCharacteristicWrite] device={0},status={1}",
                    device.getAddress(), status));
            if(status == BluetoothGatt.GATT_SUCCESS) {
                addLogInThread("写入值成功，value=" + ByteUtil.toHex(characteristic.getValue()));
            } else {
                addLogInThread("写入值失败");
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            BluetoothDevice device = gatt.getDevice();
            addLogInThread(MessageFormat.format("[onCharacteristicWrite] device={0}", device.getAddress()));
            byte[] value = characteristic.getValue();
            addLogInThread("收到notify数据，hex=" + ByteUtil.toHex(value) + ",str=" + new String(value));
        }
    };

    void addLog(String msg) {
        if(tv_log == null) {
            return;
        }
        tv_log.append("\n" + msg);

    }

    void addLogInThread(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                addLog(msg);
            }
        });
    }
}
