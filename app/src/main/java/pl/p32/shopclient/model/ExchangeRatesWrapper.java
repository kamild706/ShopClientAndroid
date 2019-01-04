package pl.p32.shopclient.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExchangeRatesWrapper {

    @SerializedName("rates")
    @Expose
    private ExchangeRates exchangeRates;

    public ExchangeRates getExchangeRates() {
        return exchangeRates;
    }

    public void setExchangeRates(ExchangeRates exchangeRates) {
        this.exchangeRates = exchangeRates;
    }

    @Override
    public String toString() {
        return "ExchangeRatesWrapper{" +
                ", exchangeRates=" + exchangeRates +
                '}';
    }
}
