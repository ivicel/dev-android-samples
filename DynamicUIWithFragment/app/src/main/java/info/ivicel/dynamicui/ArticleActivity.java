package info.ivicel.dynamicui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Created by Ivicel on 07/10/2017.
 */

public class ArticleActivity extends BaseFragmentActivity {
    private static final String TAG = "ArticleActivity";
    private static final String ARTICLE = "article";
    
    private Article mArticle;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mArticle = (Article)getIntent().getSerializableExtra(ARTICLE);
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public Fragment createFragment() {
        return ArticleFragment.newInstance(mArticle);
    }
    
    
    public static Intent newIntent(Context context, Article article) {
        Intent intent = new Intent(context, ArticleActivity.class);
        intent.putExtra(ARTICLE, article);
        return intent;
    }
}
