package com.pulllayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by kai.wang on 3/17/14.
 */
public class PullToZoomLayout extends PullToZoomBase {
    private float downY = 0.0f;

    private View headerView;
    private int headerHeight;
    private int maxHeight;
    private int currentHeight;
    private int autoHideHeader;

    private final int DIRECTION_UP = 1;
    private final int DIRECTION_DOWN = 2;

    private int prvDirection = 0;

    public PullToZoomLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.pull);
        int layout = a.getResourceId(R.styleable.pull_header, 0);
        maxHeight = a.getLayoutDimension(R.styleable.pull_maxHeight, 0);
        int minHeight = a.getLayoutDimension(R.styleable.pull_minHeight, 0);
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
        if (release) {
            // zooming
            if (headerView.getHeight() > headerHeight) {
                // todo reduce
                resizeHeight(headerHeight);
                currentHeight = headerHeight;
            }
            prvDirection = 0;
            return;
        } else {
            resizeHeader(distance, upwards);
            prvDirection = upwards ? DIRECTION_UP : DIRECTION_DOWN;
        }
    }

    private void resizeHeader(int distance, boolean upwards) {
        distance = (int)(distance/1.5f);
        Log.i("","distance:"+distance);
        // zoom out
        if (upwards && headerView.getHeight() > headerHeight) {
            int tmpHeight = currentHeight - distance;
            if (tmpHeight < headerHeight) {
                tmpHeight = headerHeight;
            }
            currentHeight = tmpHeight;
            resizeHeight(currentHeight);

        } else if (upwards && prvDirection == 0) {
            // margin to hide
            adjustHeader();
        }else if(!upwards && headerShowing){
            // margin to show
            if(headerView.getHeight() < headerHeight){

            }else{
                // zoom in
                Log.i("","currentHeight:"+currentHeight);
                currentHeight += distance;
                if(currentHeight > maxHeight){
                    currentHeight = maxHeight;
                }
                resizeHeight(currentHeight);
            }
        }
        // todo zoom in

        // todo hide

        // todo headerHeight==headerView.getHeight()

    }

    private void resizeHeight(int resizeHeight) {
        Log.i("","resizeHeight:"+resizeHeight);
        LayoutParams params = (LayoutParams) headerView.getLayoutParams();
        if (params == null) {
            params = new LayoutParams(LayoutParams.MATCH_PARENT, resizeHeight);
        }else{
            params.height = resizeHeight;
        }
        headerView.setLayoutParams(params);
    }

    protected boolean isHeaderShowing(){
        return headerShowing;
    }

}
