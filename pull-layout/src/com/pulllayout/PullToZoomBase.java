package com.pulllayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by kai.wang on 3/17/14.
 */
abstract class PullToZoomBase extends PullBase {
    private float downY = 0.0f;

    public PullToZoomBase(Context context, AttributeSet attrs) {
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
            if (headerShowing) {
                result = true;
            }
        }
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
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
        int travel = (int) (downY - movingY);
        boolean up = travel > 0;
        travel = Math.abs(travel);

        move(travel, up, actionUp);
    }
}
