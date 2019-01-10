package pl.p32.shopclient.viewmodel;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import pl.p32.shopclient.model.Product;
import pl.p32.shopclient.repository.ProductRepository;

public class SearchViewModel extends AndroidViewModel {

    private ProductRepository productRepository;
    private LiveData<List<Product>> foundProducts;
    private MutableLiveData<String> query = new MutableLiveData<>();

    public SearchViewModel(@NonNull Application application) {
        super(application);
        productRepository = ProductRepository.getInstance(application);
        foundProducts = Transformations.switchMap(query, (name) ->
            productRepository.searchForProducts(name)
        );
    }

    public LiveData<List<Product>> getFoundProducts() {
        return foundProducts;
    }

    public LiveData<String> getQuery() {
        return query;
    }

    public void search(String name) {
        query.setValue(name);
    }
}
