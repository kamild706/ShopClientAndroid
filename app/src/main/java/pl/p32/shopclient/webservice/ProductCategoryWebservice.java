package pl.p32.shopclient.webservice;

import java.util.List;

import pl.p32.shopclient.model.ProductCategory;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ProductCategoryWebservice {

    @GET("product-category")
    Call<List<ProductCategory>> getProductCategory();
}
