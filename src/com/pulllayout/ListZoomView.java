package com.pulllayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.pulllayout.util.AnimUtil;

/**
 * Created by kai.wang on 3/18/14.
 */
public class ListZoomView extends ListView {
    private int maxHeaderHeight = 0;
    private int headerHeight;
    private int currentHeaderHeight;
    private boolean zooming = false;

    private float downY = 0.0f;

    private View headerView;
    public ListZoomView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.pull);
        int layout = a.getResourceId(R.styleable.pull_header, 0);
        maxHeaderHeight = a.getLayoutDimension(R.styleable.pull_maxHeaderHeight, 0);
        int minHeaderHeight = a.getLayoutDimension(R.styleable.pull_minHeaderHeight, 0);
        a.recycle();
        if (layout == 0) {
            throw new RuntimeException("ListZoomView haven't header view.");
        }
        if (maxHeaderHeight == 0) {
            throw new RuntimeException("ListZoomView maxHeaderHeight must be set.");
        }

        headerView = LayoutInflater.from(context).inflate(layout, null);
        addHeaderView(headerView,minHeaderHeight);
        currentHeaderHeight = headerHeight;
    }

    protected void addHeaderView(View headerView,int height) {
        this.headerView = headerView;
        addHeaderView(headerView);
        measureHeader(headerView,height);
        headerHeight = headerView.getMeasuredHeight();
        LayoutParams p = (LayoutParams) headerView.getLayoutParams();
    }

    /**
     * measure header view's width and height
     *
     * @param headerView
     */
    private void measureHeader(View headerView,int height) {
        LayoutParams p = (LayoutParams) headerView.getLayoutParams();
        if (p == null) {
            int h = height == 0 ? ViewGroup.LayoutParams.WRAP_CONTENT : height;
            p = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, h);
        }else if(height != 0){
            p.height = height;
        }
        headerView.setLayoutParams(p);
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        headerView.measure(childWidthSpec, childHeightSpec);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        int action = event.getAction();
        boolean r = false;
        if (action == MotionEvent.ACTION_DOWN && headerView.getTop() == getTop()) {
            downY = event.getRawY();
        } else if (action == MotionEvent.ACTION_MOVE && headerView.getTop() == getTop()) {
            boolean upwards = downY - event.getRawY() > 0;
            if(headerView.getHeight() >= headerHeight){
                r = !(upwards && headerView.getTop() == getTop());
            }
        }
        return r;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        if (action == MotionEvent.ACTION_MOVE) {
            boolean upwards = downY - event.getRawY() > 0;
            if (headerView.getHeight() >= headerHeight && headerView.getTop() == getTop()) {
                if (!(upwards && headerView.getHeight() == headerHeight)) {
                    computeTravel(event, false);
                    downY = event.getRawY();
                    return true;
                }
            }
        } else if (action == MotionEvent.ACTION_UP && headerView.getTop() == getTop()) {
            computeTravel(event, true);
        }
        return super.onTouchEvent(event);
    }

    /**
     * 计算并调整header显示的高度
     *
     * @param ev
     * @param actionUp
     */
    private void computeTravel(MotionEvent ev, boolean actionUp) {
        float movingY = ev.getRawY();
        int travel = (int) (downY - movingY);
        boolean up = travel > 0;
        travel = Math.abs(travel);

        move(travel, up, actionUp);
    }

    public void move(int distance, boolean upwards, boolean release) {
        // illegal distance
        if(distance > 30) return;

        if (release) {
            // zooming
            if (headerView.getHeight() > headerHeight) {
                AnimUtil.collapse(headerView, headerHeight);
                currentHeaderHeight = headerHeight;
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
            int tmpHeight = currentHeaderHeight - distance;
            if (tmpHeight < headerHeight) {
                tmpHeight = headerHeight;
            }
            currentHeaderHeight = tmpHeight;
            resizeHeight(currentHeaderHeight);

        } if(!upwards && headerView.getHeight() >= headerHeight ){
            // zoom in
            currentHeaderHeight += distance;
            if(currentHeaderHeight > maxHeaderHeight){
                currentHeaderHeight = maxHeaderHeight;
            }
            resizeHeight(currentHeaderHeight);
        }

    }

    private void resizeHeight(int resizeHeight) {
        LayoutParams params = (LayoutParams) headerView.getLayoutParams();
        if (params == null) {
            params = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, resizeHeight);
        }else{
            params.height = resizeHeight;
        }
        headerView.setLayoutParams(params);
    }


}
