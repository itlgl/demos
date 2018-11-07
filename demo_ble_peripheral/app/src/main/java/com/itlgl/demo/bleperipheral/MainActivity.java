package com.itlgl.demo.bleperipheral;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.btn_to_ble_peripheral)
    void toBlePeripheral() {
        startActivity(new Intent(this, BlePeripheralActivity.class));
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.btn_to_ble_host)
    void toBleHost() {
        startActivity(new Intent(this, BleHostActivity.class));
    }
}
