package pl.p32.shopclient.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.math.BigDecimal;
import java.math.RoundingMode;

import pl.p32.shopclient.R;
import pl.p32.shopclient.model.ExchangeRates;
import pl.p32.shopclient.repository.ExchangeRatesRepository;

public class CurrencyFormatter {

    private String currency;
    private static CurrencyFormatter instance;
    private SharedPreferences preferences;
    private ExchangeRates rates;

    public static CurrencyFormatter getInstance(Application context) {
        if (instance == null) {
            synchronized (CurrencyFormatter.class) {
                if (instance == null) {
                    instance = new CurrencyFormatter(context);
                }
            }
        }
        return instance;
    }

    public static CurrencyFormatter getInstance() {
        return instance;
    }

    private CurrencyFormatter(Application context) {
        preferences = context.getSharedPreferences(
                context.getString(R.string.preference_currencies), Context.MODE_PRIVATE);
        currency = preferences.getString("CURRENCY", "PLN");

        preferences.registerOnSharedPreferenceChangeListener((sharedPreferences, key) -> onCurrencyChanged());

        ExchangeRatesRepository repository = ExchangeRatesRepository.getInstance(context);
        rates = repository.getExchangeRates();
    }

    private void onCurrencyChanged() {
        currency = preferences.getString("CURRENCY", "PLN");
    }

    public String getFormattedCurrency(BigDecimal amount) {
        BigDecimal converted = convertToCurrency(amount);
        switch (currency) {
            case "PLN":
                return converted.toString() + " zł";
            case "USD":
                return "$ " + converted.toString();
            case "EUR":
                return converted + " €";
            case "GBP":
                return "£ " + converted.toString();
            default:
                return converted.toString();
        }
    }

    public BigDecimal convertToCurrency(BigDecimal amount) {
        BigDecimal factor = BigDecimal.ONE;

        switch (currency) {
            case "PLN":
                factor = BigDecimal.ONE;
                break;
            case "USD":
                factor = BigDecimal.valueOf(rates.getUsd());
                break;
            case "EUR":
                factor = BigDecimal.valueOf(rates.getEur());
                break;
            case "GBP":
                factor = BigDecimal.valueOf(rates.getGbp());
                break;
        }

//        return amount.divide(factor, 2, BigDecimal.ROUND_HALF_EVEN);
        Log.d("CONVERTER", amount.toString());
        Log.d("CONVERTER", amount.multiply(factor).toString());
        Log.d("CONVERTER", currency);
        return amount.multiply(factor).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_EVEN);
    }
}
