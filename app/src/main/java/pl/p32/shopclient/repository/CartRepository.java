package pl.p32.shopclient.repository;

import android.app.Application;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import androidx.lifecycle.LiveData;
import pl.p32.shopclient.db.AppDatabase;
import pl.p32.shopclient.db.dao.CartItemDao;
import pl.p32.shopclient.model.CartItem;
import pl.p32.shopclient.model.Product;

public class CartRepository {

    private static CartRepository instance;
    private CartItemDao cartItemDao;

    private CartRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        cartItemDao = database.cartItemDao();
    }

    public static CartRepository getInstance(Application application) {
        if (instance == null) {
            synchronized (CartRepository.class) {
                if (instance == null) {
                    instance = new CartRepository(application);
                }
            }
        }
        return instance;
    }

    public void addToCart(CartItem item) {
        Executors.newSingleThreadExecutor().execute(() -> cartItemDao.insert(item));
//        cartItemDao.insert(item);
    }

    public LiveData<List<CartItemDao.OrderedProduct>> getAllItems() {
        return cartItemDao.loadAll();
    }

    public void deleteItemById(int id) {
        Executors.newSingleThreadExecutor().execute(() -> cartItemDao.delete(id));
    }

    public void clearCart() {
        Executors.newSingleThreadExecutor().execute(() -> cartItemDao.deleteAll());
    }

    public boolean isItemInCart(Product product) {
        Callable<Boolean> callable = () -> cartItemDao.getCartItemByProductId(product.getId()) != null;
        Future<Boolean> future = Executors.newSingleThreadExecutor().submit(callable);
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return true;
//        return cartItemDao.getCartItemByProductId(product.getId()) != null;
    }
}
