package com.itlgl.test;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.IccOpenLogicalChannelResponse;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_log)
    TextView tv_log;
    @BindView(R.id.et_aid)
    EditText et_aid;
    @BindView(R.id.et_apdu)
    EditText et_apdu;

    TelephonyManager telephonyManager;
    IccOpenLogicalChannelResponse iccOpenLogicalChannelResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.btn_open)
    void btnOpenClick(View v) {
        try {
            String aid = et_aid.getText().toString().replace(" ", "");

            iccOpenLogicalChannelResponse = telephonyManager.iccOpenLogicalChannel(aid);
            if(iccOpenLogicalChannelResponse.getStatus() == IccOpenLogicalChannelResponse.STATUS_NO_ERROR) {
                appendLog("打开成功:" + iccOpenLogicalChannelResponse);
            } else {
                appendLog("打开失败:" + iccOpenLogicalChannelResponse);
            }
        } catch (Throwable e) {
            e.printStackTrace();
            appendLog("打开失败：" + e);
        }
    }

    @SuppressWarnings("unused")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @OnClick(R.id.btn_open_2)
    void btnOpen2Click(View v) {
        try {
            String aid = et_aid.getText().toString().replace(" ", "");

            iccOpenLogicalChannelResponse = telephonyManager.iccOpenLogicalChannel(aid, 1);
            if(iccOpenLogicalChannelResponse.getStatus() == IccOpenLogicalChannelResponse.STATUS_NO_ERROR) {
                appendLog("打开成功:" + iccOpenLogicalChannelResponse);
            } else {
                appendLog("打开失败:" + iccOpenLogicalChannelResponse);
            }
        } catch (Throwable e) {
            e.printStackTrace();
            appendLog("打开失败：" + e);
        }
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.btn_transmit_apdu)
    void btnTransmitApduClick(View v) {
        try {
            String command = et_apdu.getText().toString().replace(" ", "");
            appendLog("apdu --> " + command);
            int cla, ins, p1, p2, p3;
            String data;
            cla = Integer.parseInt(command.substring(0,2), 16);
            ins = Integer.parseInt(command.substring(2,4), 16);
            p1 = Integer.parseInt(command.substring(4,6), 16);
            p2 = Integer.parseInt(command.substring(6,8), 16);
            p3 = Integer.parseInt(command.substring(8, 10), 16);
            data = command.substring(10);

            String result = telephonyManager.iccTransmitApduLogicalChannel(iccOpenLogicalChannelResponse.getChannel(),
                    cla, ins, p1, p2, p3, data);
            appendLog("apdu <-- " + result);
        } catch (Throwable e) {
            e.printStackTrace();
            appendLog("发送失败：" + e);
        }
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.btn_close)
    void btnCloseClick(View v) {
        try {
            boolean result = telephonyManager.iccCloseLogicalChannel(iccOpenLogicalChannelResponse.getChannel());
            appendLog(result ? "关闭成功" : "关闭失败");
        } catch (Throwable e) {
            e.printStackTrace();
            appendLog("关闭失败：" + e);
        }
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.clear_log)
    void clearLogClick(View v) {
        tv_log.setText("log显示区");
    }

    void appendLog(String msg) {
        if(tv_log.getEditableText() != null) {
            tv_log.getEditableText().insert(0, msg + "\n");
        } else {
            tv_log.append("\n" + msg);
        }
        Log.i("test", msg);
    }
}
