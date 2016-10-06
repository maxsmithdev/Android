package com.example.vincent.spinnerdatetime;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button mDatePickerBtn;
    private Button mTimePickerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatePickerBtn = (Button)findViewById(R.id.date_picket);
        mTimePickerBtn = (Button)findViewById(R.id.time_picket);

        mDatePickerBtn.setOnClickListener(this);
        mTimePickerBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.date_picket:
                startActivity(new Intent(this, DatePickerActivity.class));
                break;
            case R.id.time_picket:
                startActivity(new Intent(this, TimePicketActivity.class));
                break;
        }
    }
}
