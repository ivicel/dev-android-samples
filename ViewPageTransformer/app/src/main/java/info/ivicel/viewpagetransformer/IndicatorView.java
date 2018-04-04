package info.ivicel.viewpagetransformer;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;

/**
 * Created by Ivicel on 16/10/2017.
 */

public class IndicatorView extends ViewPager  {
    private static final String TAG = "IndicatorView";
    
    /* 选中和非选中图 */
    private Drawable mIndicator;
    /* 指示图标大小, 取宽, 高的最大值 */
    private int mIndicatorSize;
    /* 整个指标器宽度 */
    private int mWidth;
    /* 图标加空格和padding宽度 */
    private int mContextWidth;
    /* 图标个数 */
    private int mCount;
    /* 指标器间隔 */
    private int mMargin;
    /* 当前选中 */
    private int mSelectItem;
    /* 滑动偏移量 */
    private float mOffset;
    /* 是否时时刷新 */
    private boolean mSmooth;
    private ViewPager.OnPageChangeListener mPagerChangeListener;
    
    
    public IndicatorView(Context context) {
        this(context, null);
    }
    
    public IndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    
        TypedArray typedArray = getResources().obtainAttributes(attrs, R.styleable.IndicatorView);
        
        int N = typedArray.getIndexCount();
        for (int i = 0; i < N; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.IndicatorView_indicator_icon:
                    mIndicator = typedArray.getDrawable(i);
                    break;
                // case R.styleable.IndicatorView_indicator_margin:
                //     float defaultMargin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5,
                //             getResources().getDisplayMetrics());
                //     mMargin = (int)typedArray.getDimension(attr, defaultMargin);
                //     break;
                // case R.styleable.IndicatorView_indicator_smooth:
                //     mSmooth = typedArray.getBoolean(attr, false);
                //     break;
            }
        }
        typedArray.recycle();
        initIndicator();
    }
    
    private void initIndicator() {
        mIndicatorSize = Math.max(mIndicator.getIntrinsicWidth(), mIndicator.getIntrinsicHeight());
        mIndicator.setBounds(0, 0, mIndicator.getIntrinsicWidth(), mIndicator.getIntrinsicWidth());
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        
        // setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }
    
    private int measureWidth(int widthMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        int desired = getPaddingLeft() + getPaddingRight() + mIndicatorSize * mCount + mMargin * (mCount + 1);
        mContextWidth = desired;
        int width;
    
        if (mode == MeasureSpec.EXACTLY) {
            width = Math.max(desired, size);
        } else if (mode == MeasureSpec.AT_MOST) {
            width = Math.min(desired, size);
        } else {
            width = desired;
        }
        mWidth = width;
        return width;
    }
    
    private int measureHeight(int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        int desired = getPaddingTop() + getPaddingBottom() + mIndicatorSize;
        int height;
    
        if (mode == MeasureSpec.EXACTLY) {
            height = size;
        } else if (mode == MeasureSpec.AT_MOST) {
            height = Math.min(desired, size);
        } else {
            height = desired;
        }
        
        return height;
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        // canvas.save();
        
        // int left = mWidth / 2 - mContextWidth / 2 + getPaddingLeft();
        // canvas.translate(left, getPaddingTop());
        // for (int i = 0; i < mCount; i++) {
        //     mIndicator.setState(EMPTY_STATE_SET);
        //     mIndicator.draw(canvas);
        //     canvas.translate(mIndicatorSize + mMargin, 0);
        // }
        //
        // canvas.restore();
        // float leftDraw = (mIndicatorSize + mMargin) * (mOffset);
        // canvas.translate(leftDraw, getPaddingTop());
        // canvas.translate(leftDraw, 0);
    
        // mIndicator.setState(SELECTED_STATE_SET);
        mIndicator.draw(canvas);
    }
    
    // @Override
    // public void setAdapter(PagerAdapter adapter) {
    //     super.setAdapter(adapter);
    // }
    
    // public void setViewPager(ViewPager viewPager) {
    //     if (viewPager == null) {
    //         return;
    //     }
    //
    //     PagerAdapter pagerAdapter = viewPager.getAdapter();
    //     if (pagerAdapter == null) {
    //         throw new RuntimeException(getClass().getSimpleName() + " no adapter.");
    //     }
    //     mCount = pagerAdapter.getCount();
    //     viewPager.addOnPageChangeListener(this);
    //     mSelectItem = viewPager.getCurrentItem();
    //
    //     invalidate();
    // }
    
    // @Override
    // public void addOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
    //     this.mPagerChangeListener = listener;
    // }
    //
    // @Override
    // public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    //     super.onPageScrolled(position, positionOffset, positionOffsetPixels);
    //
    //     if (mSmooth) {
    //         mSelectItem = position;
    //         mOffset = positionOffset;
    //         invalidate();
    //     }
    //
    //     if (mPagerChangeListener != null) {
    //         mPagerChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
    //     }
    // }
    //
    // @Override
    // public void onPageSelected(int position) {
    //     mSelectItem = position;
    //     invalidate();
    //
    //     if (mPagerChangeListener != null) {
    //         mPagerChangeListener.onPageSelected(position);
    //     }
    // }
    //
    // @Override
    // public void onPageScrollStateChanged(int state) {
    //     if (mPagerChangeListener != null) {
    //         mPagerChangeListener.onPageScrollStateChanged(state);
    //     }
    // }
}
