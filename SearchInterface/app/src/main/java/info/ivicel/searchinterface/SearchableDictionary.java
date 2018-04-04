package info.ivicel.searchinterface;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class SearchableDictionary extends AppCompatActivity {
    private static final String TAG = "SearchableDictionary";
    
    private TextView mTextView;
    private ListView mListView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable_dictionary);
    
        mTextView = findViewById(R.id.text);
        mListView = findViewById(R.id.list);
    
        handleIntent(getIntent());
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
        // this activity has been set "singleTop"
        
        handleIntent(intent);
    }
    
    private void handleIntent(Intent intent) {
        Log.d(TAG, "handleIntent2: " + intent.getAction());
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            // handles a click on a search suggestion; languages activity to show word
            Intent wordIntent = new Intent(this, WordActivity.class);
            wordIntent.setData(intent.getData());
            startActivity(wordIntent);
        } else if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            // handles a search query
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.d(TAG, "handleIntent: " + query);
            showResults(query);
        }
    }
    
    // todo: replace managedQuery to LoaderManager
    private void showResults(String query) {
        Cursor cursor = managedQuery(DictionaryProvider.CONTENT_URI, null, null,
                new String[] {query}, null);
    
        if (cursor == null) {
            mTextView.setText(getString(R.string.no_results, query));
        } else {
            int count = cursor.getCount();
            String countString = getResources().getQuantityString(R.plurals.serach_result, count,
                    count, query);
            mTextView.setText(countString);
    
            String[] from = new String[]{
                    DictionaryDatabase.KEY_WORD,
                    DictionaryDatabase.KEY_DEFINITION
            };
            int[] to = new int[] {
                    R.id.word,
                    R.id.definition
            };
    
            SimpleCursorAdapter words = new SimpleCursorAdapter(this, R.layout.result,
                    cursor, from, to);
            mListView.setAdapter(words);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent wordIntent = new Intent(getApplicationContext(), WordActivity.class);
                    Uri data = Uri.withAppendedPath(DictionaryProvider.CONTENT_URI,
                            String.valueOf(id));
                    wordIntent.setData(data);
                    startActivity(wordIntent);
                }
            });
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
    
        SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView)menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                onSearchRequested();
                return true;
                
            default:
                return false;
        }
    }
}
