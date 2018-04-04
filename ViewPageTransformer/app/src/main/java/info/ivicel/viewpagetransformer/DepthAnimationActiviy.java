package info.ivicel.viewpagetransformer;

import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class DepthAnimationActiviy extends AppCompatActivity {
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
            private static final float MIN_SCALE = 0.75f;
            
            @Override
            public void transformPage(View page, float position) {
                int pageWidth = page.getWidth();
                
                if (position <= -1) {
                    page.setAlpha(0);
                    if (Build.VERSION.SDK_INT >= 21) {
                        page.setTranslationZ(0);
                    }
                } else if (position <= 0 && position > -1) {
                    page.setAlpha(1);
                    page.setScaleX(1);
                    page.setScaleY(1);
                    page.setTranslationX(0);
                    if (Build.VERSION.SDK_INT >= 21) {
                        page.setTranslationZ(1);
                    }
                } else if (position <= 1 && position > 0) {
                    page.setAlpha(1 - position);
                    page.setTranslationX(-pageWidth * position / 2);
                    float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
                    page.setScaleX(scaleFactor);
                    page.setScaleY(scaleFactor);
                    if (Build.VERSION.SDK_INT >= 21) {
                        page.setTranslationZ(0);
                    }
                } else {
                    page.setAlpha(0);
                    if (Build.VERSION.SDK_INT >= 21) {
                        page.setTranslationZ(0);
                    }
                }
            }
        });
    }
}
