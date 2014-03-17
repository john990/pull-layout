package com.pulllayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by kai.wang on 3/17/14.
 */
public class PullToZoomLayout extends PullToZoomBase {
    private float downY = 0.0f;

    private View headerView;
    private int headerHeight;

    protected int maxHeight;

    public PullToZoomLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.pull);
        int layout = a.getResourceId(R.styleable.pull_header, 0);
        maxHeight = a.getLayoutDimension(R.styleable.pull_maxHeight,0);
        a.recycle();
        if (layout == 0) {
            throw new RuntimeException("PullToZoomLayout haven't header view.");
        }
        if(maxHeight == 0){
            throw new RuntimeException("PullToZoomLayout maxHeight must be set.");
        }

        headerView = LayoutInflater.from(context).inflate(layout, null);
        addHeaderView(headerView);
        headerHeight = getHeaderHeight();
    }


    @Override
    public void move(int distance, boolean upwards, boolean release) {
        if(headerView.getHeight() > headerHeight){

        }
    }

}
