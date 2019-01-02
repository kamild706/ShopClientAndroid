package pl.p32.shopclient.db.dao;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import pl.p32.shopclient.model.Category;

@Dao
public interface CategoryDao {

    @Query("SELECT * FROM categories")
    LiveData<List<Category>> loadAllCategories();

    @Query("SELECT * FROM categories where id = :categoryId")
    LiveData<Category> loadCategory(String categoryId);

    @Query("SELECT * FROM categories WHERE name Like :query")
    LiveData<List<Category>> searchAllCategories(String query);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Category> categories);
}
