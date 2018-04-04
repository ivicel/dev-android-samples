package info.ivicel.searchinterface;

import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Created by Ivicel on 18/12/2017.
 */

public class DictionaryDatabase {
    private static final String TAG = "DictionaryDatabase";
    
    public static final String KEY_WORD = SearchManager.SUGGEST_COLUMN_TEXT_1;
    public static final String KEY_DEFINITION = SearchManager.SUGGEST_COLUMN_TEXT_2;
    
    private static final String DATABASE_NAME = "dictionary";
    private static final String FTS_VIRTUAL_TABLE = "FTSdictionary";
    private static final int DATABASE_VERSION = 2;
    
    private static final HashMap<String, String> mColumMap = buildColumMap();
    private final DictionaryOpenHelper mDictionaryOpenHelper;
    
    public DictionaryDatabase(Context context) {
        mDictionaryOpenHelper = new DictionaryOpenHelper(context);
    }
    
    private static HashMap<String, String> buildColumMap() {
        HashMap<String, String> map = new HashMap<>();
        map.put(KEY_WORD, KEY_WORD);
        map.put(KEY_DEFINITION, KEY_DEFINITION);
        map.put(BaseColumns._ID, "rowid AS " + BaseColumns._ID);
        map.put(SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID, "rowid AS " +
                SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID);
        map.put(SearchManager.SUGGEST_COLUMN_SHORTCUT_ID, "rowid AS " +
                SearchManager.SUGGEST_COLUMN_SHORTCUT_ID);
        
        return map;
    }
    
    
    public Cursor getWord(String rowId, String[] columns) {
        String selection = "rowid = ?";
        String[] selectionArgs = new String[] {rowId};
    
        return query(selection, selectionArgs, columns);
    }
    
    public Cursor getWordMatches(String query, String[] columns) {
        String selction = KEY_WORD + " MATCH ? ";
        String[] selectionArgs = new String[] {query + "*"};
    
        return query(selction, selectionArgs, columns);
    }
    
    private Cursor query(String selection, String[] selectionArgs, String[] columns) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(FTS_VIRTUAL_TABLE);
        builder.setProjectionMap(mColumMap);
    
        Cursor cursor = builder.query(mDictionaryOpenHelper.getReadableDatabase(), columns,
                selection, selectionArgs, null, null, null);
        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;
    }
    
    private static class DictionaryOpenHelper extends SQLiteOpenHelper {
        private final Context mHelperContext;
        private SQLiteDatabase mDatabase;
        private static final String FTS_TABLE_CREATE =
                "CREATE VIRTUAL TABLE " + FTS_VIRTUAL_TABLE + " USING fts3 (" +
                KEY_WORD + ", " +
                KEY_DEFINITION + ");";
    
        public DictionaryOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            mHelperContext = context;
        }
    
        @Override
        public void onCreate(SQLiteDatabase db) {
            mDatabase = db;
            mDatabase.execSQL(FTS_TABLE_CREATE);
            loadDictionary();
        }
    
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion +
                    ", which will destroy all old data.");
            db.execSQL("DROP TABLE IF EXISTS " + FTS_VIRTUAL_TABLE);
            onCreate(db);
        }
        
        private void loadDictionary() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        loadWords();
                    } catch (IOException ioe) {
                        throw new RuntimeException(ioe);
                    }
                }
            }).start();
        }
        
        private void loadWords() throws IOException {
            Log.d(TAG, "Loading words....");
            final Resources resources = mHelperContext.getResources();
            InputStream inputStream = resources.openRawResource(R.raw.definitions);
    
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] strings = TextUtils.split(line, "-");
                    if (strings.length < 2) {
                        continue;
                    }
                    long id = addWord(strings[0].trim(), strings[1].trim());
                    if (id < 0) {
                        Log.e(TAG, "unable to add word: " + strings[0].trim());
                    }
                }
            }
            Log.d(TAG, "DONE laoding words.");
        }
    
        public long addWord(String word, String definition) {
            ContentValues initialValues = new ContentValues();
            initialValues.put(KEY_WORD, word);
            initialValues.put(KEY_DEFINITION, definition);
            return mDatabase.insert(FTS_VIRTUAL_TABLE, null, initialValues);
        }
    }
}
