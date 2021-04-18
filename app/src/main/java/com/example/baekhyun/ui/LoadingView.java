package com.example.baekhyun.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.example.baekhyun.R;

public class LoadingView extends androidx.appcompat.widget.AppCompatImageView {
    private float mDegree=0;
    private boolean mNeed=true;

    public LoadingView(Context context) {
        this(context,null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setImageResource(R.mipmap.loading);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mNeed=true;
        startRotate();
    }

    private void startRotate() {
        post(new Runnable() {
            @Override
            public void run() {
                mDegree += 10;
                if (mDegree >= 360)
                    mDegree = 0;
                invalidate();
                if (getVisibility() != VISIBLE || !mNeed) {
                    removeCallbacks(this);
                } else {
                    postDelayed(this, 20);
                }
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopRotate();
    }

    private void stopRotate() {
        mNeed=false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.rotate(mDegree,getWidth()/2,getHeight()/2);
        super.onDraw(canvas);
    }
}
