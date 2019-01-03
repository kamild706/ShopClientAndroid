package pl.p32.shopclient.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExchangeRates {

    @SerializedName("rates")
    @Expose
    private Rates rates;

    public Rates getRates() {
        return rates;
    }

    public void setRates(Rates rates) {
        this.rates = rates;
    }

    @Override
    public String toString() {
        return "ExchangeRates{" +
                ", rates=" + rates +
                '}';
    }
}
