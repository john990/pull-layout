package com.pulllayout.util;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by kai.wang on 3/18/14.
 */
public class AnimUtil {

    // height change animation
    public static void collapse(final View v, final int minHeight) {
        final int initialHeight = v.getMeasuredHeight();

        final int offset = initialHeight - minHeight;
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.getLayoutParams().height = minHeight;
                    v.requestLayout();
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (offset * interpolatedTime);
                    v.requestLayout();
                }
            }
            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        // 1dp/ms
        a.setDuration((int) (minHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }
}
