package com.example.administrator.zxingdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.journeyapps.barcodescanner.DecoratedBarcodeView;

public class CustomScanAct extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_scan);
    }
}
