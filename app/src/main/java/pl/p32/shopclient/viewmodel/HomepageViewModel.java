package pl.p32.shopclient.viewmodel;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import pl.p32.shopclient.model.Product;
import pl.p32.shopclient.repository.ProductRepository;

public class HomepageViewModel extends AndroidViewModel {

    private final ProductRepository repository;
    private final LiveData<List<Product>> randomProducts;

    public HomepageViewModel(@NonNull Application application) {
        super(application);
        repository = ProductRepository.getInstance(application);
        randomProducts = repository.getProducts();
    }

    public LiveData<List<Product>> getRandomProducts() {
        return randomProducts;
    }
}
