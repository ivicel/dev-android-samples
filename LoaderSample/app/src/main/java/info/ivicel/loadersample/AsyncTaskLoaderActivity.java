package info.ivicel.loadersample;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.support.v4.content.AsyncTaskLoader;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.io.File;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AsyncTaskLoaderActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    
        FragmentManager fm = getSupportFragmentManager();
        if (fm.findFragmentById(android.R.id.content) == null) {
            fm.beginTransaction().add(android.R.id.content, new AppListFragment()).commit();
        }
    }


    public static class AppEntry {
        private final AppListLoader mLoader;
        private final ApplicationInfo mInfo;
        private String mLabel;
        private Drawable mIcon;
        private boolean mMounted;
        private final File mApkFile;

        public AppEntry(AppListLoader loader, ApplicationInfo info) {
            mLoader = loader;
            mInfo = info;
            mApkFile = new File(info.sourceDir);
        }

        public ApplicationInfo getApplicationInfo() {
            return mInfo;
        }

        public String getLabel() {
            return mLabel;
        }

        public Drawable getIcon() {
            if (mIcon == null) {
                if (mApkFile.exists()) {
                    mIcon = mInfo.loadIcon(mLoader.mPm);
                    return mIcon;
                } else {
                    mMounted = false;
                }
            } else if (!mMounted) {
                if (mApkFile.exists()) {
                    mMounted = true;
                    mIcon = mInfo.loadIcon(mLoader.mPm);
                    return mIcon;
                }
            } else {
                return mIcon;
            }

            return mLoader.getContext().getResources().getDrawable(
                    android.R.drawable.sym_def_app_icon);
        }

        @Override
        public String toString() {
            return mLabel;
        }

        void loadLabel(Context context) {
            if (mLabel == null || !mMounted) {
                mMounted = false;
                mLabel = mInfo.packageName;
            }
        }
    }

    public static final Comparator<AppEntry> ALPHA_COMPARATOR = new Comparator<AppEntry>() {
        private final Collator sCollator = Collator.getInstance();

        @Override
        public int compare(AppEntry o1, AppEntry o2) {
            return sCollator.compare(o1.getLabel(), o2.getLabel());
        }
    };

    public static class InterestingConfigChanges {
        final Configuration mLastConfiguration = new Configuration();
        int mLastDensity;

        boolean applyNewConfig(Resources res) {
            int configChanges = mLastConfiguration.updateFrom(res.getConfiguration());
            boolean densityChanged = mLastDensity != res.getDisplayMetrics().densityDpi;
            if (densityChanged || (configChanges & (ActivityInfo.CONFIG_LOCALE |
                    ActivityInfo.CONFIG_UI_MODE | ActivityInfo.CONFIG_SCREEN_LAYOUT)) != 0) {
                mLastDensity = res.getDisplayMetrics().densityDpi;
                return true;
            }
            return false;
        }
    }

    public static class PackageIntentReceiver extends BroadcastReceiver {
        final AppListLoader mLoader;

        public PackageIntentReceiver(AppListLoader loader) {
            mLoader = loader;
            IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
            filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
            filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
            filter.addDataScheme("package");
            mLoader.getContext().registerReceiver(this, filter);

            IntentFilter sdFilter = new IntentFilter();
            sdFilter.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE);
            sdFilter.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE);
            mLoader.getContext().registerReceiver(this, sdFilter);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            mLoader.onContentChanged();
        }
    }

    public static class AppListLoader extends AsyncTaskLoader<List<AppEntry>> {
        final InterestingConfigChanges mLastConfig = new InterestingConfigChanges();
        final PackageManager mPm;

        List<AppEntry> mApps;
        PackageIntentReceiver mPackageObserver;

        public AppListLoader(Context context) {
            super(context);

            mPm = getContext().getPackageManager();
        }

        @Override
        public List<AppEntry> loadInBackground() {
            List<ApplicationInfo> apps = mPm.getInstalledApplications(
                    PackageManager.GET_UNINSTALLED_PACKAGES |
                            PackageManager.GET_DISABLED_UNTIL_USED_COMPONENTS);

            if (apps == null) {
                apps = new ArrayList<>();
            }

            final Context context = getContext();
            List<AppEntry> entries = new ArrayList<>(apps.size());
            for (int i = 0; i < apps.size(); i++) {
                AppEntry entry = new AppEntry(this, apps.get(i));
                entry.loadLabel(context);
                entries.add(entry);
            }

            Collections.sort(entries, ALPHA_COMPARATOR);
            return entries;
        }

        @Override
        public void deliverResult(List<AppEntry> data) {
            if (isReset()) {
                if (data != null) {
                    onReleaseResources(data);
                }
            }

            List<AppEntry> oldApps = mApps;
            mApps = data;

            if (isStarted()) {
                super.deliverResult(data);
            }

            if (oldApps != null) {
                onReleaseResources(oldApps);
            }
        }

        @Override
        protected void onStartLoading() {
            if (mApps != null) {
                deliverResult(mApps);
            }

            if (mPackageObserver == null) {
                mPackageObserver = new PackageIntentReceiver(this);
            }

            boolean configChange = mLastConfig.applyNewConfig(getContext().getResources());
            if (takeContentChanged() || mApps == null || configChange) {
                forceLoad();
            }
        }

        @Override
        protected void onStopLoading() {
            cancelLoad();
        }

        @Override
        public void onCanceled(List<AppEntry> data) {
            super.onCanceled(data);

            onReleaseResources(data);
        }

        @Override
        protected void onReset() {
            super.onReset();

            onStopLoading();

            if (mApps != null) {
                onReleaseResources(mApps);
                mApps = null;
            }

            if (mPackageObserver != null) {
                getContext().unregisterReceiver(mPackageObserver);
                mPackageObserver = null;
            }
        }

        protected void onReleaseResources(List<AppEntry> apps) {

        }
    }

    public static class AppListAdapter extends ArrayAdapter<AppEntry> {
        private final LayoutInflater mInflater;

        public AppListAdapter(@NonNull Context context) {
            super(context, android.R.layout.simple_list_item_2);
    
            mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
    
        public void setData(List<AppEntry> data) {
            clear();
            if (data != null) {
                addAll(data);
            }
        }
    
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view;
    
            if (convertView == null) {
                view = mInflater.inflate(R.layout.activity_async_task_loader, parent, false);
            } else {
                view = convertView;
            }
    
            AppEntry item = getItem(position);
            ((ImageView)view.findViewById(R.id.icon)).setImageDrawable(item.getIcon());
            ((TextView)view.findViewById(R.id.text)).setText(item.getLabel());
            
            return view;
        }
    }
    
    public static class AppListFragment extends ListFragment implements
            LoaderManager.LoaderCallbacks<List<AppEntry>>,
            SearchView.OnCloseListener,
            SearchView.OnQueryTextListener {
        private static final String TAG = "AppListFragment";
        
        AppListAdapter mAdapter;
        
        SearchView mSearchView;
        String mCurFilter;
    
        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            
            setEmptyText("No Applications");
            
            setHasOptionsMenu(true);
    
            mAdapter = new AppListAdapter(getContext());
            setListAdapter(mAdapter);
            setListShown(false);
            getLoaderManager().initLoader(0, null, this);
        }
    
        @Override
        public boolean onQueryTextSubmit(String query) {
            return true;
        }
    
        @Override
        public boolean onQueryTextChange(String newText) {
            mCurFilter = !TextUtils.isEmpty(newText) ? newText : null;
            mAdapter.getFilter().filter(mCurFilter);
            return true;
        }
    
        @Override
        public boolean onClose() {
            if (!TextUtils.isEmpty(mSearchView.getQuery())) {
                mSearchView.setQuery(null, true);
            }
            return true;
        }
    
        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            Log.d(TAG, "onListItemClick: " + id);
        }
    
        public static class MySearchView extends SearchView {
            public MySearchView(Context context) {
                super(context);
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
            item.setActionView(mSearchView);
            
        }
    
        @Override
        public Loader<List<AppEntry>> onCreateLoader(int id, Bundle args) {
            return new AppListLoader(getContext());
        }
    
        @Override
        public void onLoadFinished(Loader<List<AppEntry>> loader,
                List<AppEntry> data) {
            mAdapter.setData(data);
    
            if (isResumed()) {
                setListShown(true);
            } else {
                setListShownNoAnimation(true);
            }
        }
    
        @Override
        public void onLoaderReset(Loader<List<AppEntry>> loader) {
            mAdapter.setData(null);
        }
    }
}
