package com.example.administrator.zxingdemo;

import android.content.pm.PackageManager;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

public class MainActivity extends AppCompatActivity implements DecoratedBarcodeView.TorchListener {

    Button btn_switch;
    DecoratedBarcodeView barcodeView;
    private CaptureManager captureManager;
    private boolean isLightOn = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_switch = (Button) findViewById(R.id.btn_switch);
        barcodeView = (DecoratedBarcodeView) findViewById(R.id.dbv_custom);
        barcodeView.setTorchListener(this);
        //判断如果没有闪光灯就去掉开启闪光灯按钮
        if (!hasFlash()){
            btn_switch.setVisibility(View.GONE);
        }
        //初始化捕获二维码功能管理者
        captureManager = new CaptureManager(this,barcodeView);
        captureManager.initializeFromIntent(getIntent(),savedInstanceState);
        captureManager.decode();
        //选择闪光灯
        btn_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLightOn){
                    barcodeView.setTorchOff();
                }else {
                    barcodeView.setTorchOn();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        captureManager.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        captureManager.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        captureManager.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        captureManager.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event) || barcodeView.onKeyDown(keyCode,event);
    }

    private boolean hasFlash(){
        return getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    @Override
    public void onTorchOn() {
        btn_switch.setText("关闭闪光灯");
        isLightOn = true;
    }

    @Override
    public void onTorchOff() {
        btn_switch.setText("打开闪光灯");
        isLightOn = false;
    }
}
