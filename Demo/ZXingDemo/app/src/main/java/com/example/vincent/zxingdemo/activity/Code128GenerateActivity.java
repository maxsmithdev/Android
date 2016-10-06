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

public class Code128GenerateActivity extends AppCompatActivity {

    private ImageView mBarcodeImage;
    private TextView mBarcodeText;

    private String mBarcodeContent = "A-10-2001-22201-Z";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_generate);

        mBarcodeImage = (ImageView)findViewById(R.id.barcode_image);
        mBarcodeText = (TextView)findViewById(R.id.code_text);

        int codeWidth = getResources().getDimensionPixelOffset(R.dimen.barcode_width);
        int codeHeight = getResources().getDimensionPixelOffset(R.dimen.barcode_height);
        Bitmap barcodeBitmap = null;
        try {
            barcodeBitmap = Utils.genBarcodeCode(mBarcodeContent, codeWidth, codeHeight);
            mBarcodeImage.setImageBitmap(barcodeBitmap);
            mBarcodeText.setText(mBarcodeContent);
        } catch (WriterException e) {
            e.printStackTrace();
        }

    }
}
