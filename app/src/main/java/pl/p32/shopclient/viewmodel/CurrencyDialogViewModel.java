package pl.p32.shopclient.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import pl.p32.shopclient.R;
import pl.p32.shopclient.SingleLiveEvent;

public class CurrencyDialogViewModel extends AndroidViewModel {

    public static final String CURRENCY = "CURRENCY";
    private SharedPreferences sharedPreferences;
    private SingleLiveEvent<Void> currencyChanged = new SingleLiveEvent<>();
    private String currency;

    public CurrencyDialogViewModel(@NonNull Application application) {
        super(application);
        sharedPreferences = application.getSharedPreferences(
                application.getString(R.string.preference_currencies), Context.MODE_PRIVATE);
        currency = sharedPreferences.getString(CURRENCY, "PLN");
    }

    public SingleLiveEvent<Void> isCurrencyChanged() {
        return currencyChanged;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(int currencyId) {
        if (currencyId == R.id.pln)
            currency = "PLN";
        else if (currencyId == R.id.usd)
            currency = "USD";
        else if (currencyId == R.id.eur)
            currency = "EUR";
        else if (currencyId == R.id.gbp)
            currency = "GBP";

        updatePreferences();
        currencyChanged.call();
    }

    private void updatePreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CURRENCY, currency);
        editor.commit();
    }
}
