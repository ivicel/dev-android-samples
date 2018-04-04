package info.ivicel.loadersample;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class CursorLoaderActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cursor_loader);
    
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {
                            Manifest.permission.READ_CONTACTS,
                            Manifest.permission.WRITE_CONTACTS},
                    1);
        } else {
            loadListFragment();
        }
    }
    
    private void loadListFragment() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.findFragmentById(android.R.id.content) == null) {
            fm.beginTransaction().add(android.R.id.content, new CursorLoaderFragment())
                    .commit();
        }
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                loadListFragment();
            }
        }
    }
    
    public static class CursorLoaderFragment extends ListFragment implements
            SearchView.OnQueryTextListener, SearchView.OnCloseListener,
            LoaderManager.LoaderCallbacks<Cursor> {
        SimpleCursorAdapter mAdapter;
        SearchView mSearchView;
        String mCurFilter;
    
        static final String[] CONTACTS_SUMMARY_PROJECTION = new String[]{
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.CONTACT_STATUS,
                ContactsContract.Contacts.CONTACT_PRESENCE,
                ContactsContract.Contacts.PHOTO_ID,
                ContactsContract.Contacts.LOOKUP_KEY
        };
    
        
    
        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            setEmptyText("No phone numbers");
            
            setHasOptionsMenu(true);
            
            mAdapter = new SimpleCursorAdapter(getContext(), android.R.layout.simple_list_item_2,
                    null, new String[] {
                        ContactsContract.Contacts.DISPLAY_NAME,
                        ContactsContract.Contacts.CONTACT_STATUS
                    }, new int[] {android.R.id.text1, android.R.id.text2},
                    0);
            
            setListAdapter(mAdapter);
            setListShown(false);
    
            getLoaderManager().initLoader(0, null, this);
        }
        
        public static class MySearchView extends SearchView {
    
            public MySearchView(Context context) {
                super(context);
            }
    
            public MySearchView(Context context, AttributeSet attrs) {
                super(context, attrs);
            }
    
            @Override
            public void onActionViewCollapsed() {
                setQuery("", false);
                super.onActionViewCollapsed();
            }
        }
    
        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            MenuItem item = menu.add("Search");
            item.setIcon(android.R.drawable.ic_menu_search);
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM |
                    MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
            
            mSearchView = new MySearchView(getContext());
            mSearchView.setOnQueryTextListener(this);
            mSearchView.setOnCloseListener(this);
            mSearchView.setIconifiedByDefault(true);
            item.setActionView(mSearchView);
        }
    
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            Uri baseUri;
    
            if (mCurFilter != null) {
                baseUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI,
                        Uri.encode(mCurFilter));
            } else {
                baseUri = ContactsContract.Contacts.CONTENT_URI;
            }
            
            String select = "((" + ContactsContract.Contacts.DISPLAY_NAME + " NOTNULL) AND (" +
                    ContactsContract.Contacts.HAS_PHONE_NUMBER + "=1) AND (" +
                    ContactsContract.Contacts.DISPLAY_NAME + "!='' ))";
            return new CursorLoader(getContext(), baseUri, CONTACTS_SUMMARY_PROJECTION,
                    select, null,
                    ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC");
        }
    
    
        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            mAdapter.swapCursor(data);
    
            if (isResumed()) {
                setListShown(true);
            } else {
                setListShownNoAnimation(true);
            }
        }
    
        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            mAdapter.swapCursor(null);
        }
    
        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            Log.i("FragmentComplexList", "Item clicked:" + id);
        }
    
        @Override
        public boolean onClose() {
            if (!TextUtils.isEmpty(mSearchView.getQuery())) {
                mSearchView.setQuery(null, true);
            }
            return true;
        }
    
        @Override
        public boolean onQueryTextSubmit(String query) {
            return true;
        }
    
        @Override
        public boolean onQueryTextChange(String newText) {
            String newFilter = !TextUtils.isEmpty(newText) ? newText : null;
            if (mCurFilter == null && newFilter == null) {
                return true;
            }
    
            if (mCurFilter != null && mCurFilter.equals(newFilter)) {
                return true;
            }
            
            mCurFilter = newFilter;
            getActivity().getSupportLoaderManager().restartLoader(0, null, this);
            return true;
        }
        
    }
}
