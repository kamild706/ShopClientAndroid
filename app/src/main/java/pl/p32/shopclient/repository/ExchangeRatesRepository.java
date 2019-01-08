package pl.p32.shopclient.repository;

import android.app.Application;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import androidx.lifecycle.LiveData;
import pl.p32.shopclient.db.AppDatabase;
import pl.p32.shopclient.db.dao.ExchangeRatesDao;
import pl.p32.shopclient.model.ExchangeRates;

public class ExchangeRatesRepository {

    private static ExchangeRatesRepository instance;
    private ExchangeRatesDao exchangeRatesDao;

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
        exchangeRatesDao = database.exchangeRatesDao();
    }

    public ExchangeRates getExchangeRates() {
        Callable<ExchangeRates> callable = () -> exchangeRatesDao.loadRates();
        Future<ExchangeRates> future = Executors.newSingleThreadExecutor().submit(callable);
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
