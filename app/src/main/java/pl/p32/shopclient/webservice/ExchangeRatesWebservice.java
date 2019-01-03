package pl.p32.shopclient.webservice;

import pl.p32.shopclient.model.ExchangeRates;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ExchangeRatesWebservice {

    @GET("latest?base=PLN&symbols=USD,EUR,GBP")
    Call<ExchangeRates> getExchangeRates();
}
