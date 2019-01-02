package pl.p32.shopclient.repository;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.LiveData;
import pl.p32.shopclient.db.AppDatabase;
import pl.p32.shopclient.db.dao.ProductCategoryDao;
import pl.p32.shopclient.model.Category;
import pl.p32.shopclient.model.Product;

public class ProductCategoryRepository {

    private static ProductCategoryRepository instance;
    private ProductCategoryDao productCategoryDao;

    private ProductCategoryRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        productCategoryDao = database.productCategoryDao();
    }

    public static ProductCategoryRepository getInstance(Application application) {
        if (instance == null) {
            synchronized (ProductCategoryRepository.class) {
                if (instance == null) {
                    instance = new ProductCategoryRepository(application);
                }
            }
        }
        return instance;
    }

    public LiveData<List<Product>> getProductsForCategory(String id) {
        return productCategoryDao.getProductsForCategory(id);
    }

    public LiveData<List<Category>> getCategoriesForProduct(String id) {
        return productCategoryDao.getCategoriesForProduct(id);
    }
}
