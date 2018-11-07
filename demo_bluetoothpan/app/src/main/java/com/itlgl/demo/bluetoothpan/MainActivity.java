package com.itlgl.demo.bluetoothpan;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.itlgl.demo.bluetoothpan.task.BlueAcceptTask;
import com.itlgl.demo.bluetoothpan.task.BlueConnectTask;
import com.itlgl.demo.bluetoothpan.task.BlueReceiveTask;

import java.io.IOException;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements BlueAcceptTask.BlueAcceptListener, BlueConnectTask.BlueConnectListener {
    private static final int REQUEST_ENABLE_BT = 1;

    BTPanUtils btPanUtils;
    private BluetoothAdapter bluetoothAdapter;

    @BindView(R.id.tv_log)
    TextView tv_log;

    // --- 手机端变量 ---
    BluetoothSocket clientBluetoothSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btPanUtils = new BTPanUtils();
        btPanUtils.setLogCallback(new ILogCallback() {
            @Override
            public void log(String msg) {
                appendLogThread(msg);
            }
        });
        ButterKnife.bind(this);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "本机未找到蓝牙功能", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            bluetoothPermissions();
        }
    }

    @OnClick(R.id.btn_open_pan)
    void btnOpenPan() {
        try {
            btPanUtils.setBluetoothTethering(true);
            appendLog("bluetooth pan 状态=" + btPanUtils.isTetheringOn());
        } catch (Exception e) {
            e.printStackTrace();
            appendLog("open bluetooth pan exception," + e);
        }
    }

    BluetoothDevice[] bondedDeviceArray;

    @OnClick(R.id.btn_connect_bond_device)
    void btnConnectBondDevice() {
        Set<BluetoothDevice> bondedDeviceSet = bluetoothAdapter.getBondedDevices();
        bondedDeviceArray = new BluetoothDevice[bondedDeviceSet.size()];
        bondedDeviceSet.toArray(bondedDeviceArray);

        String[] bondedDeviceNameArray = new String[bondedDeviceSet.size()];
        for (int i = 0, len = bondedDeviceArray.length; i < len; i++) {
            bondedDeviceNameArray[i] = bondedDeviceArray[i].getName() + "(" + bondedDeviceArray[i].getAddress() + ")";
        }
        new AlertDialog.Builder(this)
                .setTitle("选择一个绑定的设备来连接")
                .setItems(bondedDeviceNameArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BluetoothDevice bd = bondedDeviceArray[which];
                        if(bd != null) {
                            BlueConnectTask blueConnectTask = new BlueConnectTask(bd.getAddress());
                            blueConnectTask.setBlueConnectListener(MainActivity.this);
                            blueConnectTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, bd);
                        }

                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    public void onBlueConnect(String address, BluetoothSocket socket) {
        appendLog("已连接上手表端");
        this.clientBluetoothSocket = socket;
    }

    @OnClick(R.id.btn_send_message)
    void btnSendMessage() {
        if(clientBluetoothSocket != null) {
            String message = System.currentTimeMillis() + "";
            appendLog("发送消息=" + message);
            try {
                clientBluetoothSocket.getOutputStream().write(message.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
                appendLog("发送消息异常=" + e);
            }
        } else {
            appendLog("未连接手表端，不能发送消息");
        }
    }

    @OnClick(R.id.btn_close_pan)
    void btnClosePan() {
        try {
            btPanUtils.setBluetoothTethering(false);
            appendLog("bluetooth pan 状态=" + btPanUtils.isTetheringOn());
        } catch (Exception e) {
            e.printStackTrace();
            appendLog("close bluetooth pan exception," + e);
        }
    }

    @OnClick(R.id.tv_clear_log)
    void tvClearLog() {
        tv_log.setText("");
    }

    @OnClick(R.id.btn_bt_accept)
    void btnBTAccept() {
        BlueAcceptTask acceptTask = new BlueAcceptTask(true);
        acceptTask.setBlueAcceptListener(this);
        acceptTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        appendLog("等待手机端扫描连接...");
    }

    @Override
    public void onBlueAccept(BluetoothSocket socket) {
        if(socket != null) {
            BluetoothDevice bd = socket.getRemoteDevice();
            appendLog("手机端连接了,name=" + bd.getName() + ",address=" + bd.getAddress());

            appendLog("接收手机端的消息");
            BlueReceiveTask receive = new BlueReceiveTask(socket, receivePhoneMessageHandler);
            receive.start();

            appendLog("尝试连接手机端蓝牙共享");
            try {
                boolean result = btPanUtils.connectBTPan(bd);
                appendLog("连接手机端蓝牙共享--" + (result ? "成功" : " 失败"));
            } catch (Exception e) {
                e.printStackTrace();
                appendLog("连接手机端蓝牙共享--异常," + e);
            }

        }
    }

    //收到对方发来的消息
    private Handler receivePhoneMessageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                byte[] readBuf = (byte[]) msg.obj;
                String readMessage = new String(readBuf, 0, msg.arg1);
                appendLog("收到手机端的消息=" + readMessage);
                Log.d("MainActivity", "handleMessage readMessage=" + readMessage);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("我收到消息啦").setMessage(readMessage).setPositiveButton("确定", null);
                builder.create().show();
            }
        }
    };

    @OnClick(R.id.btn_view_baidu)
    void btnViewBaidu() {
        Uri uri = Uri.parse("https://www.baidu.com");
        Intent intent = new Intent("android.intent.action.VIEW", uri);
        startActivity(intent);
    }

    // 定义获取基于地理位置的动态权限
    private void bluetoothPermissions() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        } else {
            if(!bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
            btPanUtils.init(this);
        }
    }

    /**
     * 重写onRequestPermissionsResult方法
     * 获取动态权限请求的结果,再开启蓝牙
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter == null) {
                Toast.makeText(this, "本机未找到蓝牙功能", Toast.LENGTH_SHORT).show();
                finish();
            }

            if(!bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }

            btPanUtils.init(this);
        } else {
            Toast.makeText(this, "用户拒绝了权限", Toast.LENGTH_SHORT).show();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT && resultCode == RESULT_CANCELED) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    void appendLog(String msg) {
        tv_log.append("\n" + msg);
    }

    void appendLogThread(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                appendLog(msg);
            }
        });
    }
}
