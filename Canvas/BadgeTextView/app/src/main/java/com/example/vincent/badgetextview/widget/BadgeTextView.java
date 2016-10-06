package com.example.vincent.badgetextview.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by vincent on 6/7/2016.
 */

public class BadgeTextView extends TextView {


    public BadgeTextView(Context context) {
        super(context);
    }

    public BadgeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        final int height = getHeight();
        final int width = getWidth();

        Paint paint = new Paint();
        // get color
        paint.setColor(Color.RED);
        // get graph mode
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);

        Path path = new Path();

        int size = Math.max(width, height);
        int radio = size / 2;

        setWidth(size);
        setHeight(size);

        path.addCircle(size / 2, size / 2, radio, Path.Direction.CCW);
        canvas.clipPath(path, Region.Op.REPLACE);
        canvas.drawCircle(size / 2, size / 2, radio, paint);
    }

//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//
//        canvas.drawRoundRect();
//
//    }
}
