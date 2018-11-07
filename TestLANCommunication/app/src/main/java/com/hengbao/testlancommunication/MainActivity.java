package com.hengbao.testlancommunication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.NetworkUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_log)
    TextView tv_log;

    ServerSocket serverSocket;

    boolean isClientRunning = false;

    synchronized void setClientRunning(boolean b) {
        isClientRunning = b;
    }

    synchronized boolean getClientRunning() {
        return isClientRunning;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.btn_start_server)
    void startServerClick(View v) {
        // 初始化server socket
        try {
            serverSocket = new ServerSocket(12345);//监听本机的12345端口
            addLogSync("server开始监听");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 开启udp广播
        new Thread() {
            @Override
            public void run() {
                try {
                    while (!serverSocket.isClosed()) {
                        String ip = NetworkUtils.getIPAddress(true);
                        DatagramSocket sender = new DatagramSocket();
                        // String ip = serverSocket.getLocalSocketAddress().toString();
                        int port = serverSocket.getLocalPort();
                        // addLogSync("server ip=" + ip + ",port=" + port);
                        byte[] msg = ("@" + ip + ":" + port).getBytes();
                        DatagramPacket packet = new DatagramPacket(msg, msg.length, InetAddress.getByName("255.255.255.255"), 9999);
                        sender.send(packet);

                        Thread.sleep(2000);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                addLogSync("udp广播停止");
            }
        }.start();

        // 等待client socket连接
        new Thread() {
            @Override
            public void run() {
                Socket socket = null;
                try {
                    while (true) {
                        socket = serverSocket.accept();
                        addLogSync("收到一个client连接：ip=" + socket.getInetAddress());
                        byte[] buffer = new byte[2048];
                        while (true) {
                            int len = socket.getInputStream().read(buffer);
                            // 如果client主动关闭socket，server收不到响应，只是读取到的都是空数据
                            if(len == 0 || len == -1) {
                                break;
                            }
                            String msg = null;
                            try {
                                msg = new String(buffer, 0, len);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            addLogSync("client -> " + msg);

                            socket.getOutputStream().write("hello,client".getBytes());
                            addLogSync("server -> hello,client");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                addLogSync("server监听终止");
            }
        }.start();
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.btn_stop_server)
    void stopServerClick(View v) {
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
                addLog("serverSocket close exception:" + e);
            }
            serverSocket = null;
            addLog("serverSocket close");
        }
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.btn_start_client)
    void startClientClick(View v) {
        new Thread() {
            @Override
            public void run() {
                setClientRunning(true);

                String ip = null;
                int port = 0;

                try {
                    // 寻找udp广播
                    //1
                    DatagramSocket serverSocket = new DatagramSocket(9999);
                    //2
                    byte[] buffer = new byte[2048];
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    while (getClientRunning()) {
                        try {
                            //3 当程序运行起来之后,receive方法会一直处于监听状态
                            serverSocket.receive(packet);
                            //从包中将数据取出
                            String msg = new String(buffer, 0, packet.getLength());
                            Log.i("test", "ip msg=" + msg);
                            if (msg != null && msg.startsWith("@")) {
                                ip = msg.substring(1, msg.indexOf(":"));
                                port = Integer.parseInt(msg.substring(msg.indexOf(":") + 1));
                                break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if(ip != null || port != 0) {
                        addLogSync("client找到可用服务器：" + ip + ":" + port);

                        // 连接服务器
                        Socket socket = new Socket(ip, port);
                        while (getClientRunning()) {
                            socket.getOutputStream().write("hello,server".getBytes());
                            addLogSync("client -> hello,server");
                            int len = socket.getInputStream().read(buffer);
                            String msg = null;
                            try {
                                msg = new String(buffer, 0, len);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            addLogSync("server -> " + msg);

                            // 客户端每次休息2s再发送，不然会死循环
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        socket.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    addLogSync("client start exception:" + e);
                }

                setClientRunning(false);
                addLogSync("client停止");
            }
        }.start();
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.btn_stop_client)
    void stopClientClick(View v) {
        setClientRunning(false);
    }

    static final SimpleDateFormat dateFormat = new SimpleDateFormat("[HH:mm:ss]");

    void addLog(String msg) {
        if (msg != null) {
            // log倒序显示log，这样不用翻页往下找了
            if(!(tv_log.getText() instanceof Editable)) {
                tv_log.setText(tv_log.getText(), TextView.BufferType.EDITABLE);
            }
            String log = dateFormat.format(new Date()) + " " + msg + "\n";
            tv_log.getEditableText().insert(0, log);

            // log正序显示的代码
//            tv_log.append(dateFormat.format(new Date()));
//            tv_log.append(msg + "\n");
            Log.i("test", msg);
        }
    }

    void addLogSync(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                addLog(msg);
            }
        });
    }
}
