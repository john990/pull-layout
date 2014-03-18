package com.pulllayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.pulllayout.util.AnimUtil;

/**
 * Created by kai.wang on 3/17/14.
 */
public class PullToZoomLayout extends PullToZoomBase {

    private View headerView;
    private int headerHeight;
    private int maxHeight;
    private int currentHeight;

    private boolean zooming = false;

    public PullToZoomLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.pull);
        int layout = a.getResourceId(R.styleable.pull_header, 0);
        maxHeight = a.getLayoutDimension(R.styleable.pull_maxHeaderHeight, 0);
        int minHeight = a.getLayoutDimension(R.styleable.pull_minHeaderHeight, 0);
        a.recycle();
        if (layout == 0) {
            throw new RuntimeException("PullToZoomLayout haven't header view.");
        }
        if (maxHeight == 0) {
            throw new RuntimeException("PullToZoomLayout maxHeight must be set.");
        }

        headerView = LayoutInflater.from(context).inflate(layout, null);
        addHeaderView(headerView,minHeight);
        headerHeight = getHeaderHeight();
        currentHeight = headerHeight;
        headerShowing = true;
    }


    @Override
    public void move(int distance, boolean upwards, boolean release) {
        // illegal distance
        if(distance > 30) return;

        if (release) {
            // zooming
            if (headerView.getHeight() > headerHeight) {
                AnimUtil.collapse(headerView,headerHeight);
                currentHeight = headerHeight;
            }
            zooming = false;
            return;
        } else {
            zooming = true;
            resizeHeader(distance, upwards);
        }
    }

    private void resizeHeader(int distance, boolean upwards) {
        distance = (int)(distance/1.5f);
        // zoom out
        if (upwards && headerView.getHeight() > headerHeight) {
            int tmpHeight = currentHeight - distance;
            if (tmpHeight < headerHeight) {
                tmpHeight = headerHeight;
            }
            currentHeight = tmpHeight;
            resizeHeight(currentHeight);

        } if(!upwards && headerView.getHeight() >= headerHeight ){
                // zoom in
                currentHeight += distance;
                if(currentHeight > maxHeight){
                    currentHeight = maxHeight;
                }
                resizeHeight(currentHeight);
        }

    }

    private void resizeHeight(int resizeHeight) {
        LayoutParams params = (LayoutParams) headerView.getLayoutParams();
        if (params == null) {
            params = new LayoutParams(LayoutParams.MATCH_PARENT, resizeHeight);
        }else{
            params.height = resizeHeight;
        }
        headerView.setLayoutParams(params);
    }

    protected boolean isHeaderZooming(){
        return zooming;
    }

}
