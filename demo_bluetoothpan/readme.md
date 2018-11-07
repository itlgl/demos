# 蓝牙共享网络demo

## 如何测试：
> 1、 两部手机，一部手机模拟手表端，一部模拟手机端
> 2、手表端点击“开启蓝牙广播(server)”
> 3、手机端点击“开启BluetoothPan”，等待蓝牙共享功能开启，点击“连接已绑定设备列表”，选择手表设备mac地址，连接
> 4、手表端会在连接成功后直接连接手机端的蓝牙共享网络。至此，手表端具有了上网能力，手机端和手表端可以通过经典蓝牙socket传输数据。
> 5、手机端可点击“通过蓝牙发送消息”和手机端进行通信

## 测试截图
![1.png](https://raw.githubusercontent.com/itlgl/demos/master/demo_bluetoothpan/screenshot/1.png)
![2.png](https://raw.githubusercontent.com/itlgl/demos/master/demo_bluetoothpan/screenshot/2.png)

## 参考
 - [蓝牙共享网络BluetoothPan](https://blog.csdn.net/z191726501/article/details/53071151)
 - [Android 蓝牙开发（十一）Pan蓝牙共享网络分析](https://blog.csdn.net/vnanyesheshou/article/details/72638637)
 - [Setting源码-曾博文-CSDN下载](https://download.csdn.net/download/z191726501/9675605)
 - [Android移动开发-蓝牙（BlueTooth）设备检测连接的实现](https://blog.csdn.net/fukaimei/article/details/78837578)
 - [github.com/fukaimei/BluetoothTest](https://github.com/fukaimei/BluetoothTest)