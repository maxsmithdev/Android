package com.example.vincent.zxingdemo.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vincent.zxingdemo.R;
import com.example.vincent.zxingdemo.tools.Utils;
import com.google.zxing.WriterException;

/**
 * Created by vincent on 9/7/2016.
 */

public class QRCodeGenerateActivity extends AppCompatActivity {

    private ImageView mQRCodeImage;
    private TextView mQRCodeText;

    private String mQRContent = "https://www.google.com";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_generate);

        mQRCodeImage = (ImageView)findViewById(R.id.qrcode_image);
        mQRCodeText = (TextView)findViewById(R.id.code_text);

        int codeWidth = getResources().getDimensionPixelOffset(R.dimen.qrcode_width);
        int codeHeight = getResources().getDimensionPixelOffset(R.dimen.qrcode_height);
        Bitmap qrcodeBitmap = null;
        try {
            qrcodeBitmap = Utils.genQRCode(mQRContent, codeWidth, codeHeight);
            mQRCodeImage.setImageBitmap(qrcodeBitmap);
            mQRCodeText.setText(mQRContent);
        } catch (WriterException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
