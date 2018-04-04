package info.ivicel.providerwithroomsample.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

/**
 * Created by Ivicel on 22/01/2018.
 */
@Dao
public interface CheeseDao {
    @Query("SELECT COUNT(*) FROM " + Cheese.TABLE_NAME)
    int count();
    
    @Insert
    long insert(Cheese cheese);
    
    @Insert
    long[] insertAll(Cheese[] cheeses);
    
    @Query("SELECT * FROM " + Cheese.TABLE_NAME)
    Cursor selectAll();
    
    @Query("SELECT * FROM " + Cheese.TABLE_NAME + " WHERE " + Cheese.COLUMN_ID + " = :id")
    Cursor selectById(long id);
    
    @Query("DELETE FROM " + Cheese.TABLE_NAME + " WHERE " + Cheese.COLUMN_ID + " = :id")
    int deleteById(long id);
    
    @Update
    int update(Cheese cheese);
}
