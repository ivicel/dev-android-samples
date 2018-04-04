package info.ivicel.viewpagetransformer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CrossingFadeActivity extends AppCompatActivity {
    
    private TextView mTextView;
    private ProgressBar mProgressBar;
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toggle_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toogle:
                toggleAnimator();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crossing_fade);
    
        mTextView = findViewById(R.id.text_view);
        mProgressBar = findViewById(R.id.progress_bar);
        
    }
    
    private void toggleAnimator() {
        mTextView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
    
        ObjectAnimator textViewAnimator = ObjectAnimator.ofFloat(mTextView, "alpha", 0f, 1.0f);
        textViewAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                mTextView.setVisibility(View.VISIBLE);
            }
        });
        ObjectAnimator progressBarAnimator =
                ObjectAnimator.ofFloat(mProgressBar, "alpha", 1.0f, 0f);
        progressBarAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressBar.setVisibility(View.GONE);
            }
        });
        textViewAnimator.setDuration(2000);
        progressBarAnimator.setDuration(2000);
    
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(progressBarAnimator).before(textViewAnimator);
        animatorSet.start();
    }
}
