package com.example.vincent.capturescreen;

import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    private LinearLayout mMainContent;
    private ImageView mGif;
    private SimpleTarget target = new SimpleTarget<GifDrawable>() {
        @Override
        public void onResourceReady(GifDrawable bitmap, GlideAnimation glideAnimation) {
            int count = bitmap.getFrameCount();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String dirname = Environment.getExternalStorageDirectory() + "/SampleCapture";
        File dir = new File(dirname);
        if(!dir.exists()){
            dir.mkdir();
        }

        mMainContent = (LinearLayout)findViewById(R.id.main_content);
        mGif = (ImageView)findViewById(R.id.gif);
        Glide.with(this).load("https://media.giphy.com/media/9fbYYzdf6BbQA/giphy.gif").asGif().into(target);

        Button captureScreenBtn = (Button)findViewById(R.id.button_capture_screen);
        Button captureViewBtn = (Button)findViewById(R.id.button_capture_view);
        captureViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        captureScreenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMainContent.setDrawingCacheEnabled(true);
                Bitmap bitmap = mMainContent.getDrawingCache();
                File imageFile = new File(Environment.getExternalStorageDirectory() + "/SampleCapture/captured_bitmap.png");
                if (imageFile.exists()) {
                    imageFile.delete();
                }

                try {
                    FileOutputStream out = new FileOutputStream(imageFile);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                Toast.makeText(MainActivity.this, "Capture Successful.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
