package info.ivicel.dynamicui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static info.ivicel.dynamicui.BuildConfig.DEBUG;

/**
 * Created by Ivicel on 07/10/2017.
 */

public class MainFragment extends Fragment {
    private static final String TAG = "MainFragment";
    
    private RecyclerView mRecyclerView;
    private OnArticleTitleSelectedListener mTitleSelectedListener;
    private List<Article> mArticles;

    public interface OnArticleTitleSelectedListener {
        void onArticleTitleSelected(View view, Article article);
    }
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        initData();
    }
    
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    
        try {
            mTitleSelectedListener = (OnArticleTitleSelectedListener)getActivity();
        } catch (ClassCastException e) {
            if (DEBUG) {
                throw new ClassCastException(getActivity().toString() + " must implements " +
                        "OnArticleTitleSelectedListener.");
            }
        }
    }
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
    
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(new ArticleAdapter(mArticles));
        
        return view;
    }
    
    public static MainFragment newInstance() {
        return new MainFragment();
    }
    
    private class ArticleHolder extends RecyclerView.ViewHolder {
        private TextView mTitleTextView;
        
        public ArticleHolder(View itemView) {
            super(itemView);
            mTitleTextView = (TextView)itemView;
        }
        
        public void bindArticle(final Article article) {
            mTitleTextView.setText(article.getTitle());
            mTitleTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTitleSelectedListener.onArticleTitleSelected(v, article);
                }
            });
        }
    }
    
    private class ArticleAdapter extends RecyclerView.Adapter<ArticleHolder> {
        private List<Article> mArticles;
    
        public ArticleAdapter(List<Article> articles) {
            mArticles = articles;
        }
    
        @Override
        public ArticleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = getLayoutInflater().inflate(android.R.layout.simple_list_item_1,
                    parent, false);
            
            return new ArticleHolder(v);
        }
    
        @Override
        public void onBindViewHolder(ArticleHolder holder, int position) {
            Article article = mArticles.get(position);
            holder.bindArticle(article);
        }
    
        @Override
        public int getItemCount() {
            return mArticles.size();
        }
    }
    
    private void initData() {
        mArticles = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            Article article = new Article();
            article.setTitle("This is title " + i);
            article.setBody("This is the article body of title " + i);
            mArticles.add(article);
        }
    }
}
