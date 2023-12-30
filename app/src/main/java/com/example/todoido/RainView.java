package com.example.todoido;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.content.ContextCompat;

public class RainView extends View {

    private static final int NUM_SNOWFLAKES = 100;
    private static final long DELAY = 5L;

    private SnowFlake[] snowflakes = new SnowFlake[NUM_SNOWFLAKES];

    public RainView(Context context) {
        super(context);
    }

    public RainView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RainView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void resize(int width, int height) {
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.snowflake);  // 눈송이 이미지를 가져옵니다.

        for (int i = 0; i < NUM_SNOWFLAKES; i++) {
            snowflakes[i] = SnowFlake.create(width, height, drawable);  // SnowFlake.create 메소드에 Drawable 객체를 전달합니다.
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw || h != oldh) {
            resize(w, h);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (SnowFlake snowFlake : snowflakes) {
            if (snowFlake != null) {
                snowFlake.draw(canvas);
            }
        }
        getHandler().postDelayed(runnable, DELAY);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            invalidate();
        }
    };
}
