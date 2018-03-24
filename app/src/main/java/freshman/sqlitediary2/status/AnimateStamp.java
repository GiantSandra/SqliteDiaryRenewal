package freshman.sqlitediary2.status;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.util.Log;
import android.view.animation.Interpolator;
import android.widget.ImageView;

import freshman.sqlitediary2.R;


/**
 * Created by gin on 2017/11/23.
 */

public class AnimateStamp {

    private static final String TAG = "AnimateStamp";

    private ImageView mView;
    private long mDuration;
    private float mStartValue;
    private float mEndValue;
    private int mPrecision;
    private Interpolator mInterpolator;
    private ValueAnimator mValueAnimator;
    private AnimateStamp.AnimateStampListener mListener;
    private int[] mAnimRes;

    // アニメーション素材
    private static final int anim0 = R.drawable.anim_stamp_0;
    private static final int anim1 = R.drawable.anim_stamp_1;
    private static final int anim2 = R.drawable.anim_stamp_2;
    private static final int anim3 = R.drawable.anim_stamp_3;
    private static final int anim4 = R.drawable.anim_stamp_4;
    private static final int anim5 = R.drawable.anim_stamp_5;
    private static final int anim6 = R.drawable.anim_stamp_6;
    private static final int anim7 = R.drawable.anim_stamp_7;
    private static final int anim8 = R.drawable.anim_stamp_8;
    private static final int anim9 = R.drawable.anim_stamp_9;
    private static final int anim10 = R.drawable.anim_stamp_10;
    private static final int anim11 = R.drawable.anim_stamp_11;
    private static final int anim12 = R.drawable.anim_stamp_12;
    private static final int anim13 = R.drawable.anim_stamp_13;
    private static final int anim14 = R.drawable.anim_stamp_14;
    private static final int anim15 = R.drawable.anim_stamp_15;
    private static final int anim16 = R.drawable.anim_stamp_16;



    public boolean execute() {
        mValueAnimator = ValueAnimator.ofFloat(mStartValue, mEndValue);
        mValueAnimator.setDuration(mDuration);
        mValueAnimator.setInterpolator(mInterpolator);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float current = Float.valueOf(animation.getAnimatedValue().toString());
                mView.setImageResource(mAnimRes[(int)current]);
            }
        });
        mValueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (mListener != null) {
                    mListener.onAnimateStampEnd();
                }
            }
        });
        mValueAnimator.start();
        Log.i(TAG, "execute()");
        return true;
    }

    public void waitForSec(long millis){
        try{
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static class Builder {
        private long mDuration = 500;
        private float mStartValue = 0;
        private float mEndValue = 10;
        private int mPrecision = 0;
        private Interpolator mInterpolator = null;
        private ImageView mView;
        private int[] mAnimRes;

        public Builder(ImageView view){
            if(view == null) {
                throw new IllegalArgumentException("View cannot be null");
            }
            mAnimRes = new int[]{anim0, anim1, anim2, anim3, anim4, anim5, anim6, anim7, anim8,
                                 anim9, anim10, anim11, anim12, anim13, anim14, anim15, anim16};
            //mAnimRes = new int[]{anim9, anim10, anim11, anim12, anim13, anim14, anim15, anim16};
            int start = 0;
            int end = mAnimRes.length - 1;
            setCount(start, end);
            mView = view;
        }


        private AnimateStamp.Builder setCount(final int start, final int end) {
            if (start == end) {
                throw new IllegalArgumentException("Start and End must be different");
            }
            mStartValue = start;
            mEndValue = end;
            mPrecision = 0;
            return this;
        }


        private AnimateStamp.Builder setCount(final float start, final float end, final int precision) {
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

        public AnimateStamp.Builder setDuration(long duration) {
            if (duration <= 0) {
                throw new IllegalArgumentException("Duration can't be negative");
            }
            mDuration = duration;
            return this;
        }

        public AnimateStamp.Builder setInterpolator(Interpolator interpolator) {
            mInterpolator = interpolator;
            return this;
        }

        public AnimateStamp build(){
            return new AnimateStamp(this);
        }
    }

    private AnimateStamp(AnimateStamp.Builder builder) {
        mView = builder.mView;
        mDuration = builder.mDuration;
        mStartValue = builder.mStartValue;
        mEndValue = builder.mEndValue;
        mPrecision = builder.mPrecision;
        mAnimRes = builder.mAnimRes;
    }

    public void stop() {
        if (mValueAnimator.isRunning()) {
            mValueAnimator.cancel();
        }
    }

    public void setAnimateStampListener(AnimateStamp.AnimateStampListener listener) {
        mListener = listener;
    }

    public interface  AnimateStampListener {
        void onAnimateStampEnd();
    }
}
