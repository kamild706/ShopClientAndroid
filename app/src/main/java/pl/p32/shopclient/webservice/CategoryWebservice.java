package pl.p32.shopclient.webservice;

import java.util.List;

import pl.p32.shopclient.model.Category;
import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoryWebservice {

    @GET("categories")
    Call<List<Category>> getCategories();
}
