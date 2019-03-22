package com.handy.keepalive.app;

import android.content.ComponentName;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.handy.keepalive.BaseServiceConnection;
import com.handy.keepalive.utils.ServiceUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    protected TextView tvContent;
    protected Button btnDefaultStart;
    protected Button btnDefaultStop;
    protected Button btnAliveStart;
    protected Button btnAliveStop;

    private BaseServiceConnection baseServiceConnection = new BaseServiceConnection() {
        @Override
        public void onConnected(ComponentName name, IBinder service) {
            super.onConnected(name, service);
            TimeLockService.MyBinder binder = (TimeLockService.MyBinder) service;
            binder.setResultListener(new TimeLockService.ResultListener() {
                @Override
                public void onResult(final String string) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvContent.setText(string);
                        }
                    });
                }
            });
        }

        @Override
        public void onDisconnected(ComponentName name) {
            super.onDisconnected(name);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvContent.setText("Hello World");
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvContent = findViewById(R.id.tv_content);
        btnDefaultStart = findViewById(R.id.btn_default_start);
        btnDefaultStop = findViewById(R.id.btn_default_stop);
        btnAliveStart = findViewById(R.id.btn_alive_start);
        btnAliveStop = findViewById(R.id.btn_alive_stop);

        btnDefaultStart.setOnClickListener(this);
        btnDefaultStop.setOnClickListener(this);
        btnAliveStart.setOnClickListener(this);
        btnAliveStop.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_default_start:
                ServiceUtil.startService(MainActivity.this, TimeLockService.class);
                break;
            case R.id.btn_default_stop:
                ServiceUtil.stopService(MainActivity.this, TimeLockService.class);
                break;
            case R.id.btn_alive_start:
                ServiceUtil.bindService(MainActivity.this, TimeLockService.class, baseServiceConnection);
                break;
            case R.id.btn_alive_stop:
                ServiceUtil.unbindService(MainActivity.this, TimeLockService.class, baseServiceConnection);
                break;
            default:
        }
    }
}
