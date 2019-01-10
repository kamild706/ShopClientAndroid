package pl.p32.shopclient.viewmodel;

import android.app.Application;
import android.util.Log;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import pl.p32.shopclient.model.Category;
import pl.p32.shopclient.model.Product;
import pl.p32.shopclient.repository.CategoryRepository;
import pl.p32.shopclient.repository.ProductCategoryRepository;

public class CategoryProductViewModel extends AndroidViewModel {

    private final CategoryRepository categoryRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private MutableLiveData<Category> chosenCategory = new MutableLiveData<>();
    private LiveData<List<Category>> categories;
    private LiveData<List<Product>> products;

    public CategoryProductViewModel(@NonNull Application application) {
        super(application);
        categoryRepository = CategoryRepository.getInstance(application);
        productCategoryRepository = ProductCategoryRepository.getInstance(application);
        categories = categoryRepository.getCategories();

        products = Transformations.switchMap(chosenCategory, (category) ->
                productCategoryRepository.getProductsForCategory(category.getId())
        );
    }

    public LiveData<List<Product>> getProducts() {
        return products;
    }

    public LiveData<List<Category>> getCategories() {
        return categories;
    }

    public void chooseCategory(Category category) {
        Log.d("MYAPP", category.toString());
        chosenCategory.setValue(category);
    }

    public LiveData<Category> getChosenCategory() {
        return chosenCategory;
    }
}
