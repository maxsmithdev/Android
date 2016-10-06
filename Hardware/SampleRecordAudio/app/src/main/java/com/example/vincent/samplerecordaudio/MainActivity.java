package com.example.vincent.samplerecordaudio;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.Toast;

import com.example.vincent.samplerecordaudio.listener.SimpleMediaPlayerListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener {

    private Chronometer mChronometer;
    private ImageButton mRecordBtn;
    private MediaRecorder mMediaRecorder;
    private MediaPlayer mMediaPlayer;
    private MediaController mMediaController;

    private String mFileName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnPreparedListener(this);

        mMediaController = new MediaController(this);
        mMediaController.setMediaPlayer(new SimpleMediaPlayerListener());

        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/samplerecordaudio.mp4";

        File file = new File(mFileName);
        if(file.exists()){

        }

        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mMediaRecorder.setOutputFile(mFileName);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        mChronometer = (Chronometer)findViewById(R.id.chronometer);
        mChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                chronometer.setText("Chronometer：" + dateFormat.format(new Date()));
            }
        });

        mRecordBtn = (ImageButton)findViewById(R.id.button_record);
        mRecordBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent e) {
                final int action = e.getAction() & e.getActionMasked();

                switch (action){
                    case MotionEvent.ACTION_DOWN:
                        Toast.makeText(MainActivity.this, "Start Audio Recording...", Toast.LENGTH_SHORT).show();
                        mChronometer.setBase(SystemClock.elapsedRealtime());
                        mChronometer.start();

                        try {
                            mMediaRecorder.prepare();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }

                        mMediaRecorder.start();

                        break;
                    case MotionEvent.ACTION_UP:
                        mChronometer.stop();
                        mMediaRecorder.stop();
                        mMediaRecorder.release();
                        Toast.makeText(MainActivity.this, "Stop Audio Record.", Toast.LENGTH_SHORT).show();
                        break;
                }

                return false;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mMediaRecorder != null){
            mMediaRecorder.stop();
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {

    }
}
