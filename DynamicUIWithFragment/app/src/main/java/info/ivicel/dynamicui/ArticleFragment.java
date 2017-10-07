package info.ivicel.dynamicui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Ivicel on 07/10/2017.
 */

public class ArticleFragment extends Fragment {
    private static final String TAG = "ArticleFragment";
    private static final String ARTICLE = "article";
    
    private Article mArticle;
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mArticle = (Article)getArguments().getSerializable(ARTICLE);
    }
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article, container, false);
        
        TextView textView = view.findViewById(R.id.article_text_view);
        textView.setText(mArticle.getBody());
        
        return view;
    }
    
    public static ArticleFragment newInstance(Article article) {
        Bundle args = new Bundle();
        args.putSerializable(ARTICLE, article);
        ArticleFragment fragment = new ArticleFragment();
        fragment.setArguments(args);
        
        return fragment;
    }
    
}
