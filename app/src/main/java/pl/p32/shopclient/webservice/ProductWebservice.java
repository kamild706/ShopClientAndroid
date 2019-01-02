package pl.p32.shopclient.webservice;

import com.google.gson.JsonObject;

import java.util.List;

import pl.p32.shopclient.model.Product;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ProductWebservice {

    @GET("products")
    Call<List<Product>> getProducts();

    @Headers("Content-Type: application/json")
    @PUT("products/{productId}")
    Call<Product> updateProduct(@Path("productId") String productId, @Body JsonObject body);
}
