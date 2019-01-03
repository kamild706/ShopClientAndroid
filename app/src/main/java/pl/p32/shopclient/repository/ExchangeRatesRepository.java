package pl.p32.shopclient.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import pl.p32.shopclient.db.AppDatabase;
import pl.p32.shopclient.db.dao.RatesDao;
import pl.p32.shopclient.model.Rates;

public class ExchangeRatesRepository {

    private static ExchangeRatesRepository instance;
    private RatesDao ratesDao;

    public static ExchangeRatesRepository getInstance(Application application) {
        if (instance == null) {
            synchronized (ExchangeRatesRepository.class) {
                if (instance == null) {
                    instance = new ExchangeRatesRepository(application);
                }
            }
        }
        return instance;
    }

    private ExchangeRatesRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        ratesDao = database.exchangeRatesDao();
    }

    public LiveData<Rates> getExchangeRates() {
        return ratesDao.loadRates();
    }
}
