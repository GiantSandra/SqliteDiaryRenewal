package freshman.sqlitediary2.status;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.LayoutTransition;
import android.animation.ValueAnimator;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import freshman.sqlitediary2.R;

public class StatusActivity extends AppCompatActivity implements Runnable{

    TextView mCounterText;
    ImageView mAnimatedStamp;
    TableLayout mTableLayout;
    TableRow mTableRow;
    RelativeLayout mContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        mCounterText = (TextView)findViewById(R.id.counter_text);
        mAnimatedStamp = (ImageView)findViewById(R.id.stamp);
        mTableLayout = (TableLayout)findViewById(R.id.stamp_table);
        mContainer = (RelativeLayout)findViewById(R.id.activity_status);


        AnimateCounter animateCounter = new AnimateCounter.Builder(mCounterText)
                .setCount(0, 56)
                .setDuration(8000)
                .build();
        animateCounter.execute();

        ((ViewGroup) findViewById(R.id.stamp_table)).getLayoutTransition()
                .enableTransitionType(LayoutTransition.CHANGING);

        AnimateTable animateTable = new AnimateTable.Builder(mTableLayout)
                .setCount(0,129)
                .setDuration(11000)
                .build();
        animateTable.execute();

    }

    @Override
    protected void onResume() {
        super.onResume();
        /*
        Thread thread = new Thread(this);
        thread.start();
        */
    }

    private final Handler handler = new Handler();
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            for(int i = 0; i < 2; i++) {
                for(int j = 0; j < 2; j++) {
                    TableRow row = (TableRow) mTableLayout.getChildAt(i);
                    ((ViewGroup)row).getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                    ((ViewGroup)row.getChildAt(j)).getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                    ImageView stamp = (ImageView) row.getChildAt(j).findViewById(R.id.stamp);
                    AnimateStamp animateStamp = new AnimateStamp.Builder(stamp)
                            .setDuration(1000)
                            .build();
                    animateStamp.execute();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };
    @Override
    public void run() {
        handler.post(runnable);
    }
}
