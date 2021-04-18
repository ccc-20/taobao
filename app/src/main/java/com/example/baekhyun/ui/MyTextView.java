package com.example.baekhyun.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.baekhyun.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MyTextView extends ViewGroup {

    private List<String> mList=new ArrayList<>();
    private static final float DEFAULT=10;
    private static float mwidth=DEFAULT;
    private static float mheight=DEFAULT;
    private int mSize;
    private int mItemHeight;
    private onMytextListener mListener=null;

    public static float getMwidth() {
        return mwidth;
    }

    public static void setMwidth(float mwidth) {
        MyTextView.mwidth = mwidth;
    }

    public static float getMheight() {
        return mheight;
    }

    public static void setMheight(float mheight) {
        MyTextView.mheight = mheight;
    }

    public MyTextView(Context context) {
        this(context,null);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MyTextView);
        mwidth=ta.getDimension(R.styleable.MyTextView_mywidth,DEFAULT);
        mheight=ta.getDimension(R.styleable.MyTextView_myhright,DEFAULT);
        ta.recycle();
    }
    public int getsize(){
        return mList.size();
    }
    public void setList(List<String> list){
        removeAllViews();
        this.mList.clear();
        this.mList.addAll(list);
        //Collections.reverse(mList);
        for (String s : mList) {
            TextView view = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.mytext, this, false);
            view.setText(s);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onMytextclick(s);
                }
            });
            addView(view);
        }
    }


    private List<List<View>> lines=new ArrayList<>();
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(getChildCount()==0) return;
        List<View> line=null;
        lines.clear();
        mSize = MeasureSpec.getSize(widthMeasureSpec)-getPaddingLeft()-getPaddingRight();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View itemView = getChildAt(i);
            if (itemView.getVisibility()!=VISIBLE) {
                continue;
            }
            measureChild(itemView,widthMeasureSpec,heightMeasureSpec);
            if(line==null){
                line=new ArrayList<>();
                line.add(itemView);
                lines.add(line);
            }else{
                if (canAdd(itemView,line)) {
                    line.add(itemView);
                }else{
                    line=new ArrayList<>();
                    line.add(itemView);
                    lines.add(line);
                }
            }
        }
        mItemHeight = getChildAt(0).getMeasuredHeight();
        int sizeHeight = (int) (lines.size() * mItemHeight + mheight * (lines.size() + 1)+0.5f);
        setMeasuredDimension(mSize,sizeHeight);
    }

    private boolean canAdd(View itemView, List<View> line) {
        int width=itemView.getMeasuredWidth();
        for (View view : line) {
            width+=view.getMeasuredWidth();
        }
        width+=mwidth*(line.size()+1);
        return width<=mSize;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int topoffset= (int) mwidth;
        for(List<View> views:lines){
            int leftoffset= (int) mwidth;
            for(View view:views){
                view.layout(leftoffset,topoffset,leftoffset+view.getMeasuredWidth(),topoffset+view.getMeasuredHeight());
                leftoffset+=view.getMeasuredWidth()+mwidth;
            }
            topoffset+=mItemHeight+mheight;
        }
    }
    public void setOnMytextListener(onMytextListener listener){
        this.mListener=listener;
    }

    public interface onMytextListener{
        void onMytextclick(String text);
    }
}
