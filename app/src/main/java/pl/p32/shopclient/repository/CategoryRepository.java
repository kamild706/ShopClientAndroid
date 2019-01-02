package pl.p32.shopclient.repository;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.LiveData;
import pl.p32.shopclient.db.AppDatabase;
import pl.p32.shopclient.db.dao.CategoryDao;
import pl.p32.shopclient.model.Category;

public class CategoryRepository {

    private static CategoryRepository instance;
    private CategoryDao categoryDao;

    private CategoryRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        categoryDao = database.categoryDao();
    }

    public static CategoryRepository getInstance(Application application) {
        if (instance == null) {
            synchronized (CategoryRepository.class) {
                if (instance == null) {
                    instance = new CategoryRepository(application);
                }
            }
        }
        return instance;
    }

    public LiveData<List<Category>> getCategories() {
        return categoryDao.loadAllCategories();
    }

    public LiveData<Category> getCategory(String id) {
        return categoryDao.loadCategory(id);
    }
}
