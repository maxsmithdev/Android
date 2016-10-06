package com.example.vincent.zxingdemo.activity;

import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import com.example.vincent.zxingdemo.R;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.io.IOException;
import java.util.List;

/**
 * Created by vincent on 9/7/2016.
 */

public class QRScannerSurfaceViewActivity extends AppCompatActivity implements View.OnClickListener, SurfaceHolder.Callback, Camera.AutoFocusCallback {

    private final String TAG = QRScannerSurfaceViewActivity.class.getSimpleName();

    private TextView mCodeText;
    private SurfaceView mSurfaceview;
    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;
    private Camera.PreviewCallback mPreviewCallback = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] bytes, Camera camera) {
            // get picture byte
            Log.i(TAG, "Camera onPreviewFrame called.");
            // Read Range
            Camera.Size size = camera.getParameters().getPreviewSize();

            // Create BinaryBitmap
            PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(
                    bytes, size.width, size.height, 0, 0, size.width, size.height, false);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            // Read QR Code
            Reader reader = new MultiFormatReader();
            Result result = null;
            try {
                result = reader.decode(bitmap);
                String text = result.getText();
                mCodeText.setText(text);
            } catch (NotFoundException e) {
            } catch (ChecksumException e) {
            } catch (FormatException e) {
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscanner);

        mCodeText = (TextView)findViewById(R.id.code_text);
        mSurfaceview = (SurfaceView)findViewById(R.id.surfaceview);
        mSurfaceview.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSurfaceHolder = mSurfaceview.getHolder();
        mSurfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mCamera = Camera.open();
        mCamera.setDisplayOrientation(90);
        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        mCamera.setPreviewCallback(mPreviewCallback);
        Camera.Parameters parameters = mCamera.getParameters();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        } else {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        }

        List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
        Camera.Size previewSize = previewSizes.get(0);
        parameters.setPreviewSize(previewSize.width, previewSize.height);

        mCamera.setParameters(parameters);
        mCamera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        mCamera.setPreviewCallback(null);
        mCamera.release();
        mCamera = null;
    }

    @Override
    public void onClick(View view) {
        if(mCamera != null){
            mCamera.autoFocus(this);
        }
    }

    @Override
    public void onAutoFocus(boolean b, Camera camera) {
        Log.i(TAG, "Camera get focus");
    }
}
