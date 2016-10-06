package com.example.vincent.zxingdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.vincent.zxingdemo.activity.Code128GenerateActivity;
import com.example.vincent.zxingdemo.activity.QRCodeGenerateActivity;
import com.example.vincent.zxingdemo.activity.QRScannerSurfaceViewActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button mQRCodeBtn;
    private Button mCode128Btn;
    private Button mQRSFScannerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mQRCodeBtn = (Button)findViewById(R.id.button_qr_generate);
        mCode128Btn = (Button)findViewById(R.id.button_code_128_generate);
        mQRSFScannerBtn = (Button)findViewById(R.id.button_qr_surfaceview_scanner);

        mQRCodeBtn.setOnClickListener(this);
        mCode128Btn.setOnClickListener(this);
        mQRSFScannerBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_qr_generate:
                startActivity(new Intent(this, QRCodeGenerateActivity.class));
                break;
            case R.id.button_code_128_generate:
                startActivity(new Intent(this, Code128GenerateActivity.class));
                break;
            case R.id.button_qr_surfaceview_scanner:
                startActivity(new Intent(this, QRScannerSurfaceViewActivity.class));
                break;
        }
    }
}
