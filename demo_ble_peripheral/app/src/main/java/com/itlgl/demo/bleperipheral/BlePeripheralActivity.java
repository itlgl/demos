package com.itlgl.demo.bleperipheral;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BlePeripheralActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION = 1;
    private static final int REQUEST_ENABLE_BT = 2;
//    @BindView(R.id.log_listview)
//    ListView logListView;
//
//    ArrayAdapter<String> logAdapter;
    @BindView(R.id.tv_log)
    TextView tv_log;
    @BindView(R.id.btn_open_peripheral)
    Button btn_open_peripheral;
    @BindView(R.id.btn_close_peripheral)
    Button btn_close_peripheral;

    BLEPeripheral blePeripheral;

    private IAdvertisingCallback advertisingCallback = new IAdvertisingCallback() {
        @Override
        public void onAdvertising(final boolean result) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    addLog("开启从设备广播" + (result ? "成功" : "失败"));
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble_peripheral);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkBLEPermission();
    }

    private void initBLEPeripheral() {
        if(blePeripheral == null) {
            blePeripheral = BLEPeripheral.getInstance();
            try {
                blePeripheral.init(this, new ILogger() {
                    @Override
                    public void log(String msg) {
                        addLogInThread(msg);
                    }
                });

                openBluetooth();
            } catch (Exception e) {
                e.printStackTrace();
                btn_open_peripheral.setEnabled(false);
                btn_close_peripheral.setEnabled(false);
                addLog("设备不支持ble从设备," + e);
            }
        }
    }

//    private void initListView() {
//        logAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
//        logListView.setAdapter(logAdapter);
//    }

    @OnClick(R.id.tv_clear_log)
    void clearLog() {
        tv_log.setText("");
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.btn_open_peripheral)
    void openPeripheralClick() {
        initBLEPeripheral();

        try {
            blePeripheral.startAdvertising(advertisingCallback);
        } catch (Exception e) {
            e.printStackTrace();
            addLog("开启从设备异常," + e);
        }
    }

    private void checkBLEPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSION);
            } else {
                openBluetooth();
            }
        } else {
            openBluetooth();
        }
    }

    void openBluetooth() {
        initBLEPeripheral();

        if(!blePeripheral.isBluetoothEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.btn_close_peripheral)
    void closePeripheralClick() {
        if(blePeripheral != null) {
            blePeripheral.stopAdvertising();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_PERMISSION) {
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                addLog("申请位置权限被拒绝");
            } else {
                openBluetooth();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if(resultCode == Activity.RESULT_OK) {
                    try {
                        blePeripheral.startAdvertising(advertisingCallback);
                    } catch (Exception e) {
                        e.printStackTrace();
                        addLog("开启从设备异常," + e);
                    }
                } else {
                    addLog("打开蓝牙失败");
                }
                break;
        }
    }

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
