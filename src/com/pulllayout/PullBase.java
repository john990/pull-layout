package com.pulllayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;

/**
 * Created by kai.wang on 3/17/14.
 */
class PullBase extends LinearLayout{

    private View headerView;
    private int headerViewHeight;

    protected View contentView;

    protected boolean headerShowing = false;
    private int currentHeaderMargin = 0;
    private int maxMargin;

    protected boolean containAbsListView = false;

    public PullBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
    }

    protected void addHeaderView(View headerView) {
        addHeaderView(headerView,0);
    }

    protected void addHeaderView(View headerView,int height) {
        this.headerView = headerView;
        addView(headerView,0);
        measureHeader(headerView,height);
        headerViewHeight = headerView.getMeasuredHeight();
        LayoutParams p = (LayoutParams) headerView.getLayoutParams();
//        p.topMargin = -headerViewHeight;

        currentHeaderMargin = -headerViewHeight;
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

    public void hideHeader() {
        LayoutParams p = (LayoutParams) headerView.getLayoutParams();
        p.topMargin = -headerViewHeight;
        headerView.setLayoutParams(p);
        currentHeaderMargin = -headerViewHeight;
        headerShowing = false;
    }

    public void showHeader() {
        LayoutParams p = (LayoutParams) headerView.getLayoutParams();
        p.topMargin = 0;
        headerView.setLayoutParams(p);
        currentHeaderMargin = 0;
        headerShowing = true;
    }

    protected void setMaxMargin(int maxMargin){
        this.maxMargin = maxMargin;
    }

    public int getHeaderHeight() {
        return headerViewHeight;
    }

    public View getHeaderView() {
        return headerView;
    }

    protected void addContentView(View contentView) {
        this.contentView = contentView;
        if(contentView instanceof AbsListView){
            containAbsListView = true;
        }
        addView(contentView);
    }

    protected View getContentView() {
        return contentView;
    }

    /**
     * 設置header view 的 margin
     *
     * @param margin
     */
    public final void adjustHeader(int margin, boolean up) {
        if (up) {
            currentHeaderMargin = currentHeaderMargin - margin;
            if (currentHeaderMargin < -headerViewHeight || Math.abs(currentHeaderMargin + headerViewHeight) < 20) {
                currentHeaderMargin = -headerViewHeight;
            }
        } else {
            currentHeaderMargin = currentHeaderMargin + margin;
            if(currentHeaderMargin > maxMargin || Math.abs(currentHeaderMargin) < 20){
                currentHeaderMargin = maxMargin;
            }
        }
        LayoutParams p = (LayoutParams) headerView.getLayoutParams();
        p.topMargin = currentHeaderMargin;
        headerView.setLayoutParams(p);
        headerShowing = currentHeaderMargin >= -headerViewHeight;
    }

    public int getCurrentHeaderMargin() {
        return currentHeaderMargin;
    }



}
