package com.ferfalk.simplesearchview.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import android.view.View;
import android.view.animation.Interpolator;


/**
 * @author Fernando A. H. Falkiewicz
 */

public class SimpleAnimationUtils {
    public static final int ANIMATION_DURATION_DEFAULT = 250;

    public static Animator fadeIn(@NonNull View view, int duration, @Nullable final AnimationListener listener) {
        if (view.getAlpha() == 1f) {
            view.setAlpha(0);
        }

        ObjectAnimator anim = ObjectAnimator.ofFloat(view, "alpha", 1f);
        anim.addListener(new DefaultActionAnimationListener(view, listener) {
            @Override
            void defaultOnAnimationStart(@NonNull View view) {
                view.setVisibility(View.VISIBLE);
            }
        });

        anim.setDuration(duration);
        anim.setInterpolator(getDefaultInterpolator());
        return anim;
    }

    public static Animator fadeOut(@NonNull View view, int duration, @Nullable final AnimationListener listener) {
        ObjectAnimator anim = ObjectAnimator.ofFloat(view, "alpha", 0f);
        anim.addListener(new DefaultActionAnimationListener(view, listener) {
            @Override
            void defaultOnAnimationEnd(@NonNull View view) {
                view.setVisibility(View.GONE);
            }
        });

        anim.setDuration(duration);
        anim.setInterpolator(getDefaultInterpolator());
        return anim;
    }

    public static Animator verticalSlideView(@NonNull View view, int fromHeight, int toHeight, int duration) {
        return verticalSlideView(view, fromHeight, toHeight, duration, null);
    }

    private static Animator verticalSlideView(@NonNull View view, int fromHeight, int toHeight, int duration, @Nullable final AnimationListener listener) {
        ValueAnimator anim = ValueAnimator
                .ofInt(fromHeight, toHeight);

        anim.addUpdateListener(animation -> {
            view.getLayoutParams().height = (int) (Integer) animation.getAnimatedValue();
            view.requestLayout();
        });

        anim.addListener(new DefaultActionAnimationListener(view, listener));

        anim.setDuration(duration);
        anim.setInterpolator(getDefaultInterpolator());
        return anim;
    }


    private static Interpolator getDefaultInterpolator() {
        return new FastOutSlowInInterpolator();
    }


    public interface AnimationListener {
        /**
         * @return return true to override the default behaviour
         */
        boolean onAnimationStart(@NonNull View view);

        /**
         * @return return true to override the default behaviour
         */
        boolean onAnimationEnd(@NonNull View view);

        /**
         * @return return true to override the default behaviour
         */
        boolean onAnimationCancel(@NonNull View view);
    }

    private static class DefaultActionAnimationListener extends AnimatorListenerAdapter {
        private View view;
        private AnimationListener listener;

        DefaultActionAnimationListener(@NonNull View view, @Nullable AnimationListener listener) {
            this.view = view;
            this.listener = listener;
        }

        @Override
        public void onAnimationStart(Animator animation) {
            if (listener == null || !listener.onAnimationStart(view)) {
                defaultOnAnimationStart(view);
            }
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if (listener == null || !listener.onAnimationEnd(view)) {
                defaultOnAnimationEnd(view);
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            if (listener == null || !listener.onAnimationCancel(view)) {
                defaultOnAnimationCancel(view);
            }
        }

        void defaultOnAnimationStart(@NonNull View view) {
            // No default action
        }

        void defaultOnAnimationEnd(@NonNull View view) {
            // No default action
        }

        @SuppressWarnings("unused")
        void defaultOnAnimationCancel(@NonNull View view) {
            // No default action
        }
    }
}
