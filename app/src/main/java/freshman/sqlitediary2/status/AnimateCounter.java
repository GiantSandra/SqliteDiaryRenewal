package freshman.sqlitediary2.status;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.view.animation.Interpolator;
import android.widget.TextView;

/**
 * Created by gin on 2017/11/19.
 */

public class AnimateCounter {

    private TextView mView;
    private long mDuration;
    private float mStartValue;
    private float mEndValue;
    private int mPrecision;
    private Interpolator mInterpolator;
    private ValueAnimator mValueAnimator;
    private AnimateCounterListener mListener;

    public void execute() {
        mValueAnimator = ValueAnimator.ofFloat(mStartValue, mEndValue);
        mValueAnimator.setDuration(mDuration);
        mValueAnimator.setInterpolator(mInterpolator);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float current = Float.valueOf(animation.getAnimatedValue().toString());
                mView.setText(String.format("%." + mPrecision + "f", current));
            }
        });
        mValueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (mListener != null) {
                    mListener.onAnimateCounterEnd();
                }
            }
        });
        mValueAnimator.start();
    }

    public static class Builder {
        private long mDuration = 2000;
        private float mStartValue = 0;
        private float mEndValue = 10;
        private int mPrecision = 0;
        private Interpolator mInterpolator = null;
        private TextView mView;
        public Builder(TextView view){
            if(view == null) {
                throw new IllegalArgumentException("View cannot be null");
            }
            mView = view;
        }

        public Builder setCount(final int start, final int end) {
            if (start == end) {
                throw new IllegalArgumentException("Start and End must be different");
            }
            mStartValue = start;
            mEndValue = end;
            mPrecision = 0;
            return this;
        }

        public Builder setCount(final float start, final float end, final int precision) {
            if (Math.abs(start - end) < 0.001) {
                throw new IllegalArgumentException("Start and End must be different");
            }
            if (precision < 0) {
                throw new IllegalArgumentException("Precision can't be negative");
            }
            mStartValue = start;
            mEndValue = end;
            mPrecision = precision;
            return this;
        }

        public Builder setDuration(long duration) {
            if (duration <= 0) {
                throw new IllegalArgumentException("Duration can't be negative");
            }
            mDuration = duration;
            return this;
        }

        public Builder setInterpolator(Interpolator interpolator) {
            mInterpolator = interpolator;
            return this;
        }

        public AnimateCounter build(){
            return new AnimateCounter(this);
        }
    }

    private AnimateCounter(Builder builder) {
        mView = builder.mView;
        mDuration = builder.mDuration;
        mStartValue = builder.mStartValue;
        mEndValue = builder.mEndValue;
        mPrecision = builder.mPrecision;

    }

    public void stop() {
        if (mValueAnimator.isRunning()) {
            mValueAnimator.cancel();
        }
    }

    public void setAnimateCounterListener(AnimateCounterListener listener) {
        mListener = listener;
    }

    public interface  AnimateCounterListener {
        void onAnimateCounterEnd();
    }

}
