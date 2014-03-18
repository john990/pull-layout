package com.pulllayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AbsListView;

/**
 * Created by kai.wang on 3/17/14.
 */
abstract class PullToShowBase extends PullBase {
    private float downY = 0.0f;

    public PullToShowBase(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * call this method when moving
     * @param distance moving distance
     * @param upwards is upwards?
     * @param release is release?
     */
    public abstract void move(int distance, boolean upwards, boolean release);

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean result = false;
        if(contentView == null){
            return true;
        }
        int action = ev.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            downY = ev.getRawY();
        } else if (action == MotionEvent.ACTION_MOVE) {
            if (containAbsListView && ((AbsListView)contentView).getFirstVisiblePosition() == 0) {
                // (header正在显示 || 没显示并且向下拉) && 滑动距离大于10
                if ((headerShowing || downY - ev.getRawY() < 0) && Math.abs(downY - ev.getRawY()) > 10) {
                    result = true;
                }
            } else {
                headerShowing = false;
            }
        }
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN && contentView.getHeight() < getHeight()) {
            return true;
        } else if (action == MotionEvent.ACTION_MOVE) {
            computeTravel(event, false);
            downY = event.getRawY();
            return true;
        } else if (action == MotionEvent.ACTION_UP) {
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
        int travel = (int) ((downY - movingY) / 2);
        boolean up = travel > 0;
        travel = Math.abs(travel);

        if (containAbsListView && ((AbsListView)contentView).getChildCount() != 0 && ((AbsListView)contentView).getChildAt(0).getTop() != 0) {
            ((AbsListView)contentView).setSelection(0);
        }

        move(travel, up, actionUp);
    }
}
