package pl.p32.shopclient.db;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;
import pl.p32.shopclient.db.dao.CategoryDao;
import pl.p32.shopclient.db.dao.ExchangeRatesDao;
import pl.p32.shopclient.db.dao.ProductCategoryDao;
import pl.p32.shopclient.db.dao.ProductDao;
import pl.p32.shopclient.model.Category;
import pl.p32.shopclient.model.ExchangeRates;
import pl.p32.shopclient.model.ExchangeRatesWrapper;
import pl.p32.shopclient.model.Product;
import pl.p32.shopclient.model.ProductCategory;
import pl.p32.shopclient.webservice.CategoryWebservice;
import pl.p32.shopclient.webservice.ExchangeRatesWebservice;
import pl.p32.shopclient.webservice.ProductCategoryWebservice;
import pl.p32.shopclient.webservice.ProductWebservice;
import pl.p32.shopclient.webservice.WebserviceConfig;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

@Database(entities = {Product.class, Category.class, ProductCategory.class, ExchangeRates.class}, version = 1, exportSchema = false)
@TypeConverters({BigDecimalConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase instance;
    private static final String DATABASE_NAME = "app-database";

    public abstract ProductDao productDao();
    public abstract CategoryDao categoryDao();
    public abstract ProductCategoryDao productCategoryDao();
    public abstract ExchangeRatesDao exchangeRatesDao();

    public static AppDatabase getInstance(final Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class,
                            DATABASE_NAME)
                            .addCallback(new Callback() {
                                @Override
                                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                                    super.onOpen(db);
                                    Executors.newSingleThreadExecutor().execute(() -> {
                                        instance.fetchDataFromApi();
                                    });
                                }
                            })
                            .build();
                }
            }
        }
        return instance;
    }

    private void fetchDataFromApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WebserviceConfig.MY_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        fetchProducts(retrofit);
        fetchCategories(retrofit);
        fetchProductCategory(retrofit);
        fetchCurrencyRates();
    }

    private void fetchProducts(Retrofit retrofit) {
        ProductWebservice productWebservice = retrofit.create(ProductWebservice.class);
        try {
            Response<List<Product>> products = productWebservice.getProducts().execute();
            if (products.isSuccessful()) {
                productDao().insert(products.body());
            } else {
                Log.d("MYAPP", products.errorBody().string());
            }
        } catch (IOException e) {
            Log.d("MYAPP", "Error during api call for products");
            Log.d("MYAPP", e.getMessage());
        }
    }

    private void fetchCategories(Retrofit retrofit) {
        CategoryWebservice categoryWebservice = retrofit.create(CategoryWebservice.class);
        try {
            Response<List<Category>> categories = categoryWebservice.getCategories().execute();
            if (categories.isSuccessful()) {
                categoryDao().insert(categories.body());
            } else {
                Log.d("MYAPP", categories.errorBody().string());
            }
        } catch (IOException e) {
            Log.d("MYAPP", "Error during api call for categories");
        }
    }

    private void fetchProductCategory(Retrofit retrofit) {
        ProductCategoryWebservice productCategoryWebservice = retrofit.create(ProductCategoryWebservice.class);
        try {
            Response<List<ProductCategory>> productCategory = productCategoryWebservice.getProductCategory().execute();
            if (productCategory.isSuccessful()) {
                productCategoryDao().insert(productCategory.body());
            } else {
                Log.d("MYAPP", productCategory.errorBody().string());
            }
        } catch (IOException e) {
            Log.d("MYAPP", "Error during api call for product-categories");
        }
    }

    private void fetchCurrencyRates() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WebserviceConfig.EXCHANGERATES_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        ExchangeRatesWebservice exchangeRatesWebservice = retrofit.create(ExchangeRatesWebservice.class);

        Response<ExchangeRatesWrapper> response;
        try {
            response = exchangeRatesWebservice.getExchangeRates().execute();
        } catch (IOException e) {
            Log.d("MYAPP", "error while refreshing currencies");
            Log.d("MYAPP", e.getMessage());
            e.printStackTrace();
            return;
        }
        Log.d("MYAPP", response.body().toString());

        if (response.isSuccessful()) {
            ExchangeRatesWrapper exchangeRates = response.body();
                Log.d("MYAPP", response.raw().message());

                ExchangeRates rates = exchangeRates.getExchangeRates();
                Log.d("MYAPP", rates.toString());
                exchangeRatesDao().insert(rates);
        } else {
            try {
                Log.d("MYAPP", response.errorBody().string());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}