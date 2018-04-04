package info.ivicel.viewpagetransformer;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Ivicel on 10/10/2017.
 */

public class MainActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        
        findViewById(R.id.view_pager_zoom_out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ZoomOutAnimationActivity.class);
                startActivity(i);
            }
        });
        
        findViewById(R.id.view_pager_depth_pager).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, DepthAnimationActiviy.class);
                startActivity(i);
            }
        });
        
        findViewById(R.id.crossing_fade).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CrossingFadeActivity.class);
                startActivity(i);
            }
        });
        
        findViewById(R.id.card_flip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CardFlipAnimationActivity.class);
                startActivity(i);
            }
        });
        
        findViewById(R.id.zoom_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        
            }
        });
        
        findViewById(R.id.view_pager_indicator).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ViewPagerIndicatorActivity.class);
                startActivity(i);
            }
        });
    }
}
