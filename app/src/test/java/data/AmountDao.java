package data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface AmountDao {

    @Insert
    void insert(ConvertedAmount amount);

    @Delete
    void delete(ConvertedAmount amount);

    @Query("SELECT * FROM converted_amounts")
    LiveData<List<ConvertedAmount>> getAllAmounts();
}

