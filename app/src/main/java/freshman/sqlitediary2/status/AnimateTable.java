package freshman.sqlitediary2.status;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import freshman.sqlitediary2.R;


/**
 * Created by gin on 2017/11/23.
 */

public class AnimateTable {

    private static String TAG = "AnimateTable";

    private TableLayout mView;
    private long mDuration;
    private float mStartValue;
    private float mEndValue;
    private int mPrecision; // 精度
    private Interpolator mInterpolator;
    private ValueAnimator mValueAnimator;
    private AnimateTableListener mListener;
    private int mRowCount;
    private int mRowChildCount;
    private int mAnimCount = 0;
    private int mAnimRealCount = 0;

    public void execute() {
        mValueAnimator = ValueAnimator.ofFloat(mStartValue, mEndValue);
        mValueAnimator.setDuration(mDuration);
        mValueAnimator.setInterpolator(mInterpolator);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                /*
                float current = Float.valueOf(animation.getAnimatedValue().toString());
                int int_current = (int)current;
                Log.i(TAG,"current : " + current + " int_current : " + int_current + " row : " + ((int)int_current / mRowChildCount) + " row_child : " + (int_current % mRowChildCount));
                */
                int slower = 3;
                if(mAnimCount < 130) {
                    TableRow row = (TableRow) mView.getChildAt((int) mAnimCount / mRowChildCount);
                    ImageView stamp = (ImageView) row.getChildAt(mAnimCount % mRowChildCount).findViewById(R.id.stamp);
                    AnimateStamp animateStamp = new AnimateStamp.Builder(stamp)
                            .setDuration(500)
                            .build();
                    animateStamp.execute();
                }
                if((mAnimRealCount % slower) == 0) {
                    mAnimCount++;
                }
                mAnimRealCount++;
                Log.i(TAG, "mAnimCount : " + mAnimCount);
            }
        });
        mValueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (mListener != null) {
                    mListener.onAnimateTableEnd();
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
        private TableLayout mView;
        public Builder(TableLayout view){
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

        public AnimateTable build(){
            return new AnimateTable(this);
        }
    }

    private AnimateTable(Builder builder) {
        mView = builder.mView;
        mDuration = builder.mDuration;
        mStartValue = builder.mStartValue;
        mEndValue = builder.mEndValue;
        mPrecision = builder.mPrecision;
        mRowCount = builder.mView.getChildCount();
        mRowChildCount = ((TableRow)builder.mView.getChildAt(0)).getVirtualChildCount();
        Log.i(TAG, "row_count : " + mRowCount + " row_child_count : " + mRowChildCount);

    }

    public void stop() {
        if (mValueAnimator.isRunning()) {
            mValueAnimator.cancel();
        }
    }

    public void setAnimateTableListener(AnimateTableListener listener) {
        mListener = listener;
    }

    public interface  AnimateTableListener {
        void onAnimateTableEnd();
    }

}
