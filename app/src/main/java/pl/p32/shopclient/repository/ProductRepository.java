package pl.p32.shopclient.repository;

import android.app.Application;
import android.util.Log;

import com.google.gson.JsonObject;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;

import androidx.lifecycle.LiveData;
import pl.p32.shopclient.db.AppDatabase;
import pl.p32.shopclient.db.dao.ProductDao;
import pl.p32.shopclient.model.Product;
import pl.p32.shopclient.utils.ProductConverter;
import pl.p32.shopclient.webservice.ProductWebservice;
import pl.p32.shopclient.webservice.WebserviceConfig;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ProductRepository {

    private static ProductRepository instance;
    private ProductDao productDao;
    private ProductWebservice productWebservice;

    private ProductRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        productDao = database.productDao();
    }

    public static ProductRepository getInstance(Application application) {
        if (instance == null) {
            synchronized (ProductRepository.class) {
                if (instance == null) {
                    instance = new ProductRepository(application);
                }
            }
        }
        return instance;
    }

    public LiveData<List<Product>> getProducts() {
        return productDao.loadAllProducts();
    }

    public LiveData<Product> getProduct(String id) {
        return productDao.loadProduct(id);
    }

    public void updateProduct(Product product) {
        JsonObject gsonObject;
        try {
            gsonObject = ProductConverter.toJson(product);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        if (productWebservice == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(WebserviceConfig.MY_API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
            productWebservice = retrofit.create(ProductWebservice.class);
        }

        try {
            Response<Product> response = productWebservice.updateProduct(product.getId(), gsonObject).execute();
            if (response.isSuccessful()) {
                Executors.newSingleThreadExecutor().execute(() -> productDao.update(response.body()));
            } else {
                Log.d("MYAPP", response.errorBody().string());
            }
        } catch (IOException e) {
            Log.d("MYAPP", "Error during product update");
            e.printStackTrace();
        }
    }
}
