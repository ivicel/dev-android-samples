package info.ivicel.providerwithroomsample.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.VisibleForTesting;

import java.io.ByteArrayOutputStream;

import info.ivicel.providerwithroomsample.BuildConfig;
import info.ivicel.providerwithroomsample.R;

/**
 * Created by Ivicel on 22/01/2018.
 */

@Database(entities = {Cheese.class}, version = 1)
public abstract class SampleDatabase extends RoomDatabase {
    @SuppressWarnings("WeakerAccess")
    public abstract CheeseDao cheese();
    
    private static SampleDatabase sInstance;
    
    public static synchronized SampleDatabase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = Room.databaseBuilder(context.getApplicationContext(), SampleDatabase.class,
                    "ex").build();
            sInstance.populateInitialData(context);
        }
        return sInstance;
    }
    
    @VisibleForTesting
    public static void switchToInMemory(Context context) {
        sInstance = Room.inMemoryDatabaseBuilder(context, SampleDatabase.class).build();
    }
    
    private void populateInitialData(Context context) {
        if (cheese().count() == 0) {
            Cheese cheese = new Cheese();
            
            Drawable drawable;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                drawable = context.getDrawable(R.mipmap.ic_launcher);
            } else {
                drawable = context.getResources().getDrawable(R.mipmap.ic_launcher);
            }
            Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            byte[] picture = out.toByteArray();
            beginTransaction();
            try {
                for (int i = 0; i < Cheese.CHEESES.length; i++) {
                    cheese.name = Cheese.CHEESES[i];
                    cheese.picture = picture;
                    cheese().insert(cheese);
                }
                setTransactionSuccessful();
            } finally {
                endTransaction();
            }
        }
    }
}
