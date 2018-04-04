package info.ivicel.searchinterface;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Property;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.support.v7.widget.SearchView;

/**
 * Created by Ivicel on 20/12/2017.
 */

public class MySearchView extends SearchView {
    private static final String TAG = "MySearchView";
    
    private View mShadowView;
    private SearchMenuArrowDrawable mArrowDrawable;
    private OnMenuClickListener mOnMenuClickListener;
    private ImageView mMenuView;
    private boolean mIsArrow;
    private OnShadowViewVisiableChangedListener mOnShadowViewVisiableChangedListener;
    
    public interface OnShadowViewVisiableChangedListener {
        void setOnShadowViewVisiableChanged(View v);
    }
    
    public void setOnShadowViewVisiableChangedListener(OnShadowViewVisiableChangedListener listener) {
        mOnShadowViewVisiableChangedListener = listener;
    }
    
    public interface OnMenuClickListener {
        void onMenuClickListener(View view);
    }
    
    public void setOnMenuClickListener(OnMenuClickListener listener) {
        mOnMenuClickListener = listener;
    }
    
    
    public MySearchView(Context context) {
        this(context, null);
    }
    
    public MySearchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    
    public MySearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    
    private void init(Context context) {
        setIconifiedByDefault(false);
        setImeOptions(EditorInfo.IME_ACTION_SEARCH);
    
        mArrowDrawable = new SearchMenuArrowDrawable(context);
        
        mMenuView = findViewById(R.id.search_mag_icon);
        mMenuView.setImageDrawable(mArrowDrawable);
        
    }
    
    @Override
    protected void onFocusChanged(boolean gainFocus, int direction,
            @Nullable Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    
        mIsArrow = gainFocus;
        Log.d(TAG, "onFocusChanged: " + mIsArrow);
        mArrowDrawable.startAnimator(mIsArrow);
        
        if (mShadowView != null) {
            int result = gainFocus ? View.VISIBLE : View.INVISIBLE;
            mShadowView.setVisibility(result);
        }
    }
    
    
    
    public void setShadowView(View view) {
        mShadowView = view;
    }
    
    static class SearchMenuArrowDrawable extends DrawerArrowDrawable {
        
        static final float STATE_ARROW = 1.0f;
        static final float STATE_HAMBURGER = 0.0f;
    
        SearchMenuArrowDrawable(Context context) {
            super(context);
        }
    
        private static final Property PROGRESS =
                new Property<DrawerArrowDrawable, Float>(Float.class, "PROGRESS") {
            @Override
            public void set(DrawerArrowDrawable object, Float value) {
                object.setProgress(value);
            }

            @Override
            public Float get(DrawerArrowDrawable object) {
                return object.getProgress();
            }
        };

        
        void startAnimator(boolean isHumberge) {
            ObjectAnimator animator;
            if (isHumberge) {
                animator = ObjectAnimator.ofFloat(this, PROGRESS, STATE_HAMBURGER, STATE_ARROW);
            } else {
                animator = ObjectAnimator.ofFloat(this, PROGRESS, STATE_ARROW, STATE_HAMBURGER);
            }
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.setDuration(5000);
            setVerticalMirror(true);
        }
    }
    
    public void startAnimation() {
        mArrowDrawable.startAnimator(mIsArrow);
        mIsArrow = !mIsArrow;
    }
}
