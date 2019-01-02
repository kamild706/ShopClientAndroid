package pl.p32.shopclient.db.dao;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import pl.p32.shopclient.model.Product;

@Dao
public interface ProductDao {

    @Query("SELECT * FROM products")
    LiveData<List<Product>> loadAllProducts();

    @Query("SELECT * FROM products where id = :productId")
    LiveData<Product> loadProduct(String productId);

    @Query("SELECT * FROM products WHERE name Like :query")
    LiveData<List<Product>> searchAllProducts(String query);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Product product);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Product> products);
}
