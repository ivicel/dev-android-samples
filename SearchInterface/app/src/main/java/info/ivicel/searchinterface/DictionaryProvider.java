package info.ivicel.searchinterface;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Ivicel on 17/12/2017.
 */

public class DictionaryProvider extends ContentProvider {
    private static final String TAG = "DictionaryProvider";
    
    private static final String AUTHORITY = "info.ivicel.searchinterface.DictionaryProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/dictionary");
    
    public static final String WORDS_MIME_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.info.ivicel.searchableddict";
    public static final String DEFINITION_MIME_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.info.ivicel.searchableddict";
    
    private DictionaryDatabase mDictionary;
    
    private static final int SEARCH_WORDS = 0;
    private static final int GET_WORD = 1;
    private static final int SEARCH_SUGGEST = 2;
    private static final int REFRESH_SHORTCUT = 3;
    private static final UriMatcher sURIMatcher = buildUriMatcher();
    
    private static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        // get definition
        matcher.addURI(AUTHORITY, "dictionary", SEARCH_WORDS);
        matcher.addURI(AUTHORITY, "dictionary/#", GET_WORD);
        // get suggestions
        matcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY, SEARCH_SUGGEST);
        matcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY + "/*", SEARCH_SUGGEST);
        
        matcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_SHORTCUT, REFRESH_SHORTCUT);
        matcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_SHORTCUT + "/*", REFRESH_SHORTCUT);
        
        return matcher;
    }
    
    @Override
    public boolean onCreate() {
        mDictionary = new DictionaryDatabase(getContext());
        return true;
    }
    
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
            @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        switch (sURIMatcher.match(uri)) {
            case SEARCH_SUGGEST:
                if (selectionArgs == null) {
                    throw new IllegalArgumentException("selectionArgs must be provided for the" +
                            " uri: " + uri);
                }
                return getSuggestions(selectionArgs[0]);
                
            case SEARCH_WORDS:
                if (selectionArgs == null) {
                    throw new IllegalArgumentException("selectionArgs must be provided for the" +
                            " uri: " + uri);
                }
                return search(selectionArgs[0]);
                
            case GET_WORD:
                return getWord(uri);
                
            case REFRESH_SHORTCUT:
                return refreshShortcut(uri);
            
            default:
                throw new IllegalArgumentException("Unknow Uri: " + uri);
        }
    }
    
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (sURIMatcher.match(uri)) {
            case SEARCH_WORDS:
                return WORDS_MIME_TYPE;
            
            case GET_WORD:
                return DEFINITION_MIME_TYPE;
            
            case SEARCH_SUGGEST:
                return SearchManager.SUGGEST_COLUMN_SHORTCUT_ID;
                
            case REFRESH_SHORTCUT:
                return SearchManager.SHORTCUT_MIME_TYPE;
                
            default:
                throw new IllegalArgumentException("Unknow URL: " + uri);
        }
    }
    
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection,
            @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
            @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }
    
    private Cursor getSuggestions(String query) {
        query = query.toLowerCase();
        String[] columns = new String[] {
            BaseColumns._ID,
            DictionaryDatabase.KEY_WORD,
            DictionaryDatabase.KEY_DEFINITION,
            SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID
        };
        return mDictionary.getWordMatches(query, columns);
    }
    
    private Cursor search(String query) {
        query = query.toLowerCase();
        String[] columns = new String[] {
            BaseColumns._ID,
            DictionaryDatabase.KEY_WORD,
            DictionaryDatabase.KEY_DEFINITION
        };
        return mDictionary.getWordMatches(query, columns);
    }
    
    private Cursor getWord(Uri uri) {
        String rowId = uri.getLastPathSegment();
        String[] columns = new String[] {
            DictionaryDatabase.KEY_WORD,
            DictionaryDatabase.KEY_DEFINITION
        };
    
        return mDictionary.getWord(rowId, columns);
    }
    
    private Cursor refreshShortcut(Uri uri) {
        String rowId = uri.getLastPathSegment();
        String[] columns = new String[] {
            BaseColumns._ID,
            DictionaryDatabase.KEY_WORD,
            DictionaryDatabase.KEY_DEFINITION,
            SearchManager.SUGGEST_COLUMN_SHORTCUT_ID,
            SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID
        };
    
        return mDictionary.getWord(rowId, columns);
    }
    
    
}
