package com.itlgl.demo.bleperipheral;

import android.Manifest;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattServerCallback;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.itlgl.java.util.ByteUtil;

import java.util.UUID;

/**
 * 模拟ble低功耗蓝牙从设备
 * 1、调用startAdvertising方法开始广播
 * bluetoothLeAdvertiser.startAdvertising(getAdvertiseSettings(), getAdvertiseData(), advertiseCallback)
 * 2、在advertiseCallback中会回调开启广播成功或者失败，成功以后再添加广播的service
 * 3、添加servic成功以后，从设备广播开启成功
 * 4、在bluetoothGattServerCallback的回调中，对应的处理外部设备的连接、断开、发送read、write等请求
 *
 * 因为是测试demo，所以notify通道默认就开启了，即使外部连接不开启notify一样能收到响应
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public enum BLEPeripheral {
    instance;

    public static final UUID UUID_SERVICE = UUID.fromString("48EB0001-F352-5FA0-9B06-8FCAA22602CF");

    public static final UUID UUID_DESCRIPTOR = UUID.fromString("00002902-0000-1000-8000-00805F9B34FB");

    public static final UUID UUID_CHAR_READ = UUID.fromString("48EB0002-F352-5FA0-9B06-8FCAA22602CF");
    public static final UUID UUID_CHAR_WRITE = UUID.fromString("48EB0003-F352-5FA0-9B06-8FCAA22602CF");
    public static final UUID UUID_CHAR_INDICATE = UUID.fromString("48EB0004-F352-5FA0-9B06-8FCAA22602CF");
    public static final UUID UUID_CHAR_NOTIFY = UUID.fromString("48EB0005-F352-5FA0-9B06-8FCAA22602CF");

    private Context context;
    private ILogger logger;
    private IAdvertisingCallback advertisingCallback;

    private BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothLeAdvertiser bluetoothLeAdvertiser;
    private BluetoothGattServer bluetoothGattServer;

    private BluetoothGattCharacteristic bleReadCharacteristic;
    private BluetoothGattCharacteristic bleWriteCharacteristic;
    private BluetoothGattCharacteristic bleIndicateCharacteristic;
    private BluetoothGattCharacteristic bleNotifyCharacteristic;

    /**
     * 其他数字可能出现不能广播的问题。
     */
    private int timeoutMillis = 0;
    private boolean connectable = true;
    private int txPowerLevel = AdvertiseSettings.ADVERTISE_TX_POWER_HIGH;
    private int advertiseMode = AdvertiseSettings.ADVERTISE_MODE_BALANCED;

    public static BLEPeripheral getInstance() {
        return instance;
    }

    public void init(Context context, ILogger logger) throws Exception {
        if(logger == null) {
            this.logger = new ILogger() {
                @Override
                public void log(String msg) {
                    Log.i("BLEPeripheral", msg);
                }
            };
        } else {
            this.logger = logger;
        }

        if (context == null) {
            throw new Exception("入参Context为空!");
        }
        this.context = context;
        if (!isBluetoothLeValid()) {
            throw new Exception("设备不支持BLE");
        }
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            throw new Exception("操作BLE需要获取'BLUETOOTH'权限");
        }
        // 检查是否拥有位置权限
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            throw new Exception("操作BLE需要获取'ACCESS_COARSE_LOCATION'权限");
//            throw new Exception("自Android 6.0开始需要打开位置权限'ACCESS_COARSE_LOCATION'才可以搜索到Ble设备");
        }
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            throw new Exception("扫描周围BLE设备需要获取'BLUETOOTH_ADMIN'权限");
        }
        bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        if (bluetoothManager == null) {
            throw new Exception("获取BluetoothManager失败");
        }
        bluetoothAdapter = bluetoothManager.getAdapter();
        if (bluetoothAdapter == null) {
            throw new Exception("获取BluetoothAdapter失败");
        }

        if (!isBluetoothLeAdvertiser()) {
            throw new Exception("当前设备不支持BLE从设备通讯");
        }
        // Added in API level 21
        bluetoothLeAdvertiser = bluetoothAdapter.getBluetoothLeAdvertiser();
        if (bluetoothLeAdvertiser == null) {
            throw new Exception("当前设备不支持BLE从设备广播");
        }
    }

    /**
     * 判断是否支持BLe蓝牙设备
     *
     * @return 系统是否支持BLE
     */
    public boolean isBluetoothLeValid() {
        // 从Android 4.3＋开始才支持
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2 &&
                context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }

    /**
     * 设备是否支持蓝牙低功耗从设备广播
     *
     * @return 是否支持BLE从设备广播
     */
    public boolean isBluetoothLeAdvertiser() {
        // 从Android 5.0＋开始才支持
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public void setPeripheralName(String name) {
        bluetoothAdapter.setName(name);
    }

    public boolean isBluetoothEnabled() {
        return bluetoothAdapter.isEnabled();
    }

    public void startAdvertising(IAdvertisingCallback callback) throws Exception {
        this.advertisingCallback = callback;
        logger.log("startAdvertising()");
        this.bluetoothGattServer = bluetoothManager.openGattServer(context, bluetoothGattServerCallback);
        if (this.bluetoothGattServer == null) {
            throw new Exception("BluetoothManager.openGattServer结果返回为NULL");
        }
        this.bluetoothLeAdvertiser.startAdvertising(getAdvertiseSettings(), getAdvertiseData(), advertiseCallback);
    }

    public void stopAdvertising() {
        logger.log("stopAdvertising()");
        this.advertisingCallback = null;
        if(bluetoothGattServer != null) {
            this.bluetoothGattServer.clearServices();
            for (BluetoothDevice device : bluetoothManager.getConnectedDevices(BluetoothProfile.GATT_SERVER)) {
                this.bluetoothGattServer.cancelConnection(device);
            }
            this.bluetoothGattServer.close();

            this.bluetoothLeAdvertiser.stopAdvertising(advertiseCallback);

            bluetoothGattServer = null;
        }
    }

    private AdvertiseSettings getAdvertiseSettings() {
        AdvertiseSettings.Builder settingsBuilder = new AdvertiseSettings.Builder();
        settingsBuilder.setTimeout(timeoutMillis);
        settingsBuilder.setConnectable(connectable);
        settingsBuilder.setTxPowerLevel(txPowerLevel);
        settingsBuilder.setAdvertiseMode(advertiseMode);
        return settingsBuilder.build();
    }

    private AdvertiseData getAdvertiseData() {
        AdvertiseData.Builder dataBuilder = new AdvertiseData.Builder();
        dataBuilder.setIncludeDeviceName(true);
        dataBuilder.setIncludeTxPowerLevel(true);
        // 增加这两个会出现无法被扫描到的问题
        // dataBuilder.addServiceUuid(ParcelUuid.fromString(Const.UUID_SERVICE));
        // dataBuilder.addServiceUuid((new ParcelUuid(UUID.randomUUID())));
        return dataBuilder.build();
    }

    private AdvertiseCallback advertiseCallback = new AdvertiseCallback() {
        @Override
        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
            super.onStartSuccess(settingsInEffect);
            // 广播成功以后添加service
            boolean result = bluetoothGattServer.addService(getPeripheralGattService());

        }

        @Override
        public void onStartFailure(int errorCode) {
            super.onStartFailure(errorCode);
            logger.log("开启BLE从设备广播失败,错误码:" + errorCode);
            if(advertisingCallback != null) {
                advertisingCallback.onAdvertising(false);
            }
        }
    };

    private BluetoothGattService getPeripheralGattService() {
        BluetoothGattService service = new BluetoothGattService(UUID_SERVICE,
                BluetoothGattService.SERVICE_TYPE_PRIMARY);

        // 添加一个read的通道
        BluetoothGattCharacteristic characteristicRead = new BluetoothGattCharacteristic(UUID_CHAR_READ,
                BluetoothGattCharacteristic.PROPERTY_READ,
                BluetoothGattCharacteristic.PERMISSION_READ);
        service.addCharacteristic(characteristicRead);

        // write通道描述符
        BluetoothGattDescriptor writeDescriptor = new BluetoothGattDescriptor(UUID_DESCRIPTOR,
                BluetoothGattCharacteristic.PERMISSION_WRITE);
        // writeDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        // 添加一个write通道
        BluetoothGattCharacteristic characteristicWrite = new BluetoothGattCharacteristic(UUID_CHAR_WRITE,
                BluetoothGattCharacteristic.PROPERTY_WRITE,
                BluetoothGattCharacteristic.PERMISSION_WRITE);
        characteristicWrite.addDescriptor(writeDescriptor);
        service.addCharacteristic(characteristicWrite);

        // indicate通道描述符
        BluetoothGattDescriptor indicateDescriptor = new BluetoothGattDescriptor(UUID_DESCRIPTOR,
                BluetoothGattCharacteristic.PERMISSION_WRITE);
        indicateDescriptor.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
        // 添加一个indicate通道
        BluetoothGattCharacteristic characteristicIndicate = new BluetoothGattCharacteristic(UUID_CHAR_INDICATE,
                BluetoothGattCharacteristic.PROPERTY_INDICATE,
                BluetoothGattCharacteristic.PERMISSION_WRITE);
        characteristicIndicate.addDescriptor(indicateDescriptor);
        service.addCharacteristic(characteristicIndicate);


        // notify通道描述符
        BluetoothGattDescriptor notifyDescriptor = new BluetoothGattDescriptor(UUID_DESCRIPTOR,
                BluetoothGattCharacteristic.PERMISSION_WRITE);
        notifyDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        // 添加一个indicate通道
        BluetoothGattCharacteristic characteristicNofity = new BluetoothGattCharacteristic(UUID_CHAR_NOTIFY,
                BluetoothGattCharacteristic.PROPERTY_NOTIFY,
                BluetoothGattCharacteristic.PERMISSION_WRITE);
        characteristicNofity.addDescriptor(notifyDescriptor);
        service.addCharacteristic(characteristicNofity);

        return service;
    }

    private BluetoothGattServerCallback bluetoothGattServerCallback = new BluetoothGattServerCallback() {
        @Override
        public void onServiceAdded(int status, BluetoothGattService service) {
            super.onServiceAdded(status, service);
            logger.log("BluetoothGattServerCallback onServiceAdded,status=" + status + ",service.uuid=" + service.getUuid().toString());

            if(status == BluetoothGatt.GATT_SUCCESS) {
                logger.log("添加service成功");
                advertisingCallback.onAdvertising(true);
            } else {
                logger.log("添加service失败");
                advertisingCallback.onAdvertising(false);
            }

            bleReadCharacteristic = service.getCharacteristic(UUID_CHAR_READ);
            bleWriteCharacteristic = service.getCharacteristic(UUID_CHAR_WRITE);
            bleIndicateCharacteristic = service.getCharacteristic(UUID_CHAR_INDICATE);
            bleNotifyCharacteristic = service.getCharacteristic(UUID_CHAR_NOTIFY);
        }

        @Override
        public void onConnectionStateChange(BluetoothDevice device, int status, int newState) {
            super.onConnectionStateChange(device, status, newState);
            logger.log("BluetoothGattServerCallback onConnectionStateChange,device.mac=" + device.getAddress()
                    + ",status=" + status + ",newState=" + newState);
            if (newState == BluetoothProfile.STATE_CONNECTED) { //连接成功
                logger.log("BluetoothGattServerCallback [-设备连接成功-]");
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {  //连接失败
                logger.log("BluetoothGattServerCallback [-设备断开连接-]");
                bluetoothGattServer.cancelConnection(device);
            }
        }

        @Override
        public void onCharacteristicReadRequest(BluetoothDevice device, int requestId, int offset,
                                                BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicReadRequest(device, requestId, offset, characteristic);
            logger.log("BluetoothGattServerCallback onCharacteristicReadRequest,device.mac=" + device.getAddress()
                    + ",requestId=" + requestId + ",offset=" + offset + ",characteristic.uuid=" + characteristic.getUuid().toString());
            // 当收到read请求时，返回读取成功
            bluetoothGattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, offset,
                    characteristic.getValue());

            // 返回数据
            bleNotifyCharacteristic.setValue("read request".getBytes());
            bluetoothGattServer.notifyCharacteristicChanged(device, bleNotifyCharacteristic, false);
        }

        @Override
        public void onCharacteristicWriteRequest(BluetoothDevice device, int requestId,
                BluetoothGattCharacteristic characteristic, boolean preparedWrite, boolean responseNeeded,
                int offset, byte[] value) {
            super.onCharacteristicWriteRequest(device, requestId, characteristic, preparedWrite,
                    responseNeeded, offset, value);
            logger.log("BluetoothGattServerCallback onCharacteristicWriteRequest,device.mac=" + device.getAddress()
                    + ",requestId=" + requestId + ",characteristic.uuid=" + characteristic.getUuid().toString() +
                    ",preparedWrite=" + preparedWrite + ",responseNeeded=" + responseNeeded);
            logger.log("BluetoothGattServerCallback onCharacteristicWriteRequest,offset=" + offset + "value=" + ByteUtil.toHex(value));
            // 当收到write请求时，返回写入成功
            bluetoothGattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, offset, value);

            // 返回数据
            bleNotifyCharacteristic.setValue("write request".getBytes());
            bluetoothGattServer.notifyCharacteristicChanged(device, bleNotifyCharacteristic, false);
        }

        @Override
        public void onNotificationSent(BluetoothDevice device, int status) {
            super.onNotificationSent(device, status);
            logger.log("BluetoothGattServerCallback onNotificationSent,device.mac=" + device + ",status=" + status);
        }

        @Override
        public void onDescriptorWriteRequest(BluetoothDevice device, int requestId, BluetoothGattDescriptor descriptor,
                                             boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
            super.onDescriptorWriteRequest(device, requestId, descriptor, preparedWrite, responseNeeded, offset, value);
            logger.log("BluetoothGattServerCallback onDescriptorWriteRequest,device.mac=" + device.getAddress() +
                    ",requestId=" + requestId + ",descriptor.uuid=" + descriptor.getUuid().toString() +
                    ",preparedWrite=" + preparedWrite + ",responseNeeded=" + responseNeeded);
            logger.log("BluetoothGattServerCallback onDescriptorWriteRequest,offset=" + offset + "value=" + ByteUtil.toHex(value));
            // 向Descriptor写值时，返回写入成功
            // 正常的蓝牙控制下，向Descriptor一般是开启notify/indicate等，然后再开启notify返回，这个程序简化了，notify默认开启
            bluetoothGattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, offset, value);
        }

        @Override
        public void onDescriptorReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattDescriptor descriptor) {
            super.onDescriptorReadRequest(device, requestId, offset, descriptor);
            logger.log("BluetoothGattServerCallback onDescriptorReadRequest,device.mac=" + device.getAddress() +
                    ",requestId=" + requestId + ",descriptor.uuid=" + descriptor.getUuid().toString());
            bluetoothGattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, offset, descriptor.getValue());
        }

        @Override
        public void onMtuChanged(BluetoothDevice device, int mtu) {
            super.onMtuChanged(device, mtu);
            logger.log("BluetoothGattServerCallback onMtuChanged,device.mac=" + device.getAddress() + ",mtu=" + mtu);
        }
    };
}
