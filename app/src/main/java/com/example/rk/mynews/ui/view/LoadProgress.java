package com.example.rk.mynews.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.example.rk.mynews.R;

/**
 * Created by RK on 2015/8/25.
 */
public class LoadProgress extends View {
    Paint paint = new Paint();
    RectF rectF ;
    public int sweepAngle;

    int color;
    int size;
    public LoadProgress(Context context, AttributeSet set) {
        super(context,set);
        TypedArray a=context.getTheme().obtainStyledAttributes(
                set,
                R.styleable.LoadProgress,
                0,0);
        try {
            size=a.getInteger(R.styleable.LoadProgress_LoadProgresssize,100);
            color=a.getColor(R.styleable.LoadProgress_LoadProgresscolor,Color.BLACK);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            a.recycle();
        }


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // È¥¾â³Ý
        rectF=new RectF(0, 0, canvas.getWidth(), canvas.getHeight());
        paint.setAntiAlias(true);
        paint.setAlpha(5);

        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawArc(rectF, 0, sweepAngle, true, paint);
    }

    public void setSweepAngle(int sweepAngle) {
        this.sweepAngle = sweepAngle;
        invalidate();
    }
}
