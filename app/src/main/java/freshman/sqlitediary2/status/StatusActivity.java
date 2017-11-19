package freshman.sqlitediary2.status;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Interpolator;
import android.widget.TextView;

import freshman.sqlitediary2.R;

public class StatusActivity extends AppCompatActivity {

    TextView mCounterText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        mCounterText = (TextView)findViewById(R.id.counter_text);

        AnimateCounter animateCounter = new AnimateCounter.Builder(mCounterText)
                .setCount(0, 56)
                .setDuration(2000)
                .build();
        animateCounter.execute();
    }
}
