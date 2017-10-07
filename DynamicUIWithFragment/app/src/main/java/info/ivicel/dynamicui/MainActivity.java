package info.ivicel.dynamicui;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends BaseFragmentActivity implements
        MainFragment.OnArticleTitleSelectedListener {
    private static final String TAG = "MainActivity";
    
    
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }
    
    @Override
    public Fragment createFragment() {
        return MainFragment.newInstance();
    }
    
    @Override
    protected void onStart() {
        super.onStart();
    
    }
    
    @Override
    public void onArticleTitleSelected(View view, Article article) {
        if (findViewById(R.id.article_fragment) != null) {
            ArticleFragment articleFragment = ArticleFragment.newInstance(article);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.article_fragment, articleFragment).commit();
        } else {
            Intent intent = ArticleActivity.newIntent(this, article);
            startActivity(intent);
        }
    }
}
