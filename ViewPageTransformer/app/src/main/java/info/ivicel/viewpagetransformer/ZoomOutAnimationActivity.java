package info.ivicel.viewpagetransformer;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class ZoomOutAnimationActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private static final int[] COLORS = new int[]{
            Color.BLACK,
            Color.BLUE,
            Color.CYAN,
            Color.GRAY,
            Color.RED,
            Color.YELLOW
    };
        
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_out);
    
        mViewPager = findViewById(R.id.view_pager);
        
        mViewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return ColorFragment.newInstance(COLORS[position]);
            }
    
            @Override
            public int getCount() {
                return COLORS.length;
            }
        });
        
        mViewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            private static final float MIN_SCALE = 0.85f;
            private static final float MIN_ALPHA = 0.5f;
            
            
            @Override
            public void transformPage(View page, float position) {
                int width = page.getWidth();
                int height = page.getHeight();
    
                if (position < -1) {
                    page.setAlpha(0);
                } else if (position <= 1) {
                    float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                    float vertMargin = height * (1 - scaleFactor) / 2;
                    float horzMargin = width * (1 - scaleFactor) / 2;
                    
                    if (position < 0) {
                        page.setTranslationX(horzMargin - vertMargin / 2);
                    } else {
                        page.setTranslationX(-horzMargin + vertMargin / 2);
                    }
                    
                    page.setScaleX(scaleFactor);
                    page.setScaleY(scaleFactor);
                    
                    page.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) /
                            (1 - MIN_SCALE) * (1 + MIN_ALPHA));
                } else {
                    page.setAlpha(0);
                }
            }
        });
    }
}
