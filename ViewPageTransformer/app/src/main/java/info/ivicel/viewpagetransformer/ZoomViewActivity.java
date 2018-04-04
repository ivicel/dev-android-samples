package info.ivicel.viewpagetransformer;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class ZoomViewActivity extends AppCompatActivity {
    private Animator mCurrentAnimator;
    
    private int mShortAnimatorDuration;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_view);
    
        final View thumb1View = findViewById(R.id.thumb_button_1);
        thumb1View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomImageFromThumb(thumb1View, R.drawable.image1);
            }
        });
    
        mShortAnimatorDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);
    }
    
    private void zoomImageFromThumb(final View thumbView, @DrawableRes int resId) {
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }
    
        final ImageView expandedImageView = findViewById(R.id.expanded_image);
        expandedImageView.setImageResource(resId);
        
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();
    
        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.container).getGlobalVisibleRect(finalBounds, globalOffset);
        
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);
        
        float startScale;
        if ((float)finalBounds.width() / finalBounds.height() >
                (float)startBounds.width() / startBounds.height()) {
            startScale = (float)startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            startScale = (float)startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }
        
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);
        
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);
    
        AnimatorSet set = new AnimatorSet();
        
    }
}
