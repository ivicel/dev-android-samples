package info.ivicel.searchinterface;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.util.Log;
import android.util.Property;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    DrawerArrowDrawable arrowDrawable;
    private boolean mIsHamburger = true;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    
        findViewById(R.id.dictionary_search_widget).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SearchableDictionary.class));
            }
        });
        
        findViewById(R.id.sample_search_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SearchDialogActivity.class));
            }
        });
    
        arrowDrawable = new DrawerArrowDrawable(this);
        arrowDrawable.setColor(Color.WHITE);
        
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(arrowDrawable);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.d(TAG, "onOptionsItemSelected: " + arrowDrawable.getDirection());
                arrowDrawableAnimate();
                Log.d(TAG, "onOptionsItemSelected: " + arrowDrawable.getDirection());
                break;
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
        Log.d(TAG, "onNewIntent: ");
        super.onNewIntent(intent);
    }

    private static Property<DrawerArrowDrawable, Float> PROGRESS =
            new Property<DrawerArrowDrawable, Float>(Float.class, "progress") {
        @Override
        public Float get(DrawerArrowDrawable object) {
            return object.getProgress();
        }
    
        @Override
        public void set(DrawerArrowDrawable object, Float value) {
            object.setProgress(value);
        }
    };
    
    private void arrowDrawableAnimate() {
        ObjectAnimator animator;
        if (mIsHamburger) {
            // arrowDrawable.setDirection(DrawerArrowDrawable.ARROW_DIRECTION_START);
            animator = ObjectAnimator.ofFloat(arrowDrawable, PROGRESS, 0.0f, 1.0f);
        } else {
            // arrowDrawable.setDirection(DrawerArrowDrawable.ARROW_DIRECTION_END);
            animator = ObjectAnimator.ofFloat(arrowDrawable, PROGRESS, 1.0f, 0.0f);
        }
        mIsHamburger = !mIsHamburger;
        arrowDrawable.setVerticalMirror(mIsHamburger);
        animator.setDuration(500L);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();
    }
}
