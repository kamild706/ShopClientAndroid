package pl.p32.shopclient.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import pl.p32.shopclient.model.Rates;

@Dao
public interface RatesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Rates rates);

    @Query("SELECT * FROM rates WHERE id = 1")
    LiveData<Rates> loadRates();
}
