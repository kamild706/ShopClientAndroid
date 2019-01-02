package pl.p32.shopclient.db.dao;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import pl.p32.shopclient.model.Category;
import pl.p32.shopclient.model.Product;
import pl.p32.shopclient.model.ProductCategory;

@Dao
public interface ProductCategoryDao {

    @Query("SELECT * FROM products INNER JOIN product_category " +
            "ON products.id = product_category.productId " +
            "WHERE product_category.categoryId = :categoryId")
    LiveData<List<Product>> getProductsForCategory(String categoryId);

    @Query("SELECT * FROM categories INNER JOIN product_category " +
            "ON categories.id = product_category.categoryId " +
            "WHERE product_category.productId = :productId")
    LiveData<List<Category>> getCategoriesForProduct(String productId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<ProductCategory> productCategories);
}
