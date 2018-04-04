package info.ivicel.searchinterface;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.support.v7.widget.SearchView;


public class SearchDialogActivity extends AppCompatActivity {
    private static final String TAG = "SearchDialogActivity";
    
    private Toolbar mToolbar;
    private MySearchView mSearchView;
    private View mShadowView;
    private FrameLayout mFrameLayout;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable_dialog);
        
        mFrameLayout = findViewById(R.id.framelayout);
        initSearchView();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
    
        Log.d(TAG, "icon: " + mSearchView.findViewById(R.id.search_mag_icon));
    }
    
    private void initSearchView() {
        mSearchView = findViewById(R.id.my_serach_view);
        mShadowView = findViewById(R.id.background_view);
        
        mSearchView.setShadowView(mShadowView);
        
        //
        //
        mShadowView.setVisibility(View.INVISIBLE);
        mShadowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShadowView.setVisibility(View.INVISIBLE);
                mSearchView.clearFocus();
                mSearchView.startAnimation();
            }
        });
        //
        mSearchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.d(TAG, "onFocusChange: " + hasFocus);
                if (hasFocus) {
                    // mShadowView.setVisibility(View.VISIBLE);
                }
            }
        });
        //
        //
        // mSearchView.setOnClickListener(new View.OnClickListener() {
        //     @Override
        //     public void onClick(View v) {
        //         Log.d(TAG, "onClick: " + v);
        //     }
        // });
    }
}
