package pl.p32.shopclient.viewmodel;

import android.app.Application;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.function.Function;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import pl.p32.shopclient.db.dao.CartItemDao;
import pl.p32.shopclient.model.CartItem;
import pl.p32.shopclient.model.Product;
import pl.p32.shopclient.repository.CartRepository;
import pl.p32.shopclient.repository.ProductRepository;

public class CartViewModel extends AndroidViewModel {

    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final LiveData<List<CartItemDao.OrderedProduct>> cartItems;
    private final LiveData<BigDecimal> totalPrice;

    public CartViewModel(@NonNull Application application) {
        super(application);
        productRepository = ProductRepository.getInstance(application);
        cartRepository = CartRepository.getInstance(application);
        cartItems = cartRepository.getAllItems();
        totalPrice = Transformations.map(cartItems, (items) -> {
            BigDecimal sum = new BigDecimal(0);
            for (CartItemDao.OrderedProduct item : items) {
                BigDecimal quantity = BigDecimal.valueOf(item.quantity);
                BigDecimal productValue = quantity.multiply(item.price);
                sum = sum.add(productValue);
            }

            return sum;
        });
    }

    public LiveData<BigDecimal> getTotalPrice() {
        return totalPrice;
    }

    public LiveData<List<CartItemDao.OrderedProduct>> getCartItems() {
        return cartItems;
    }

    public void finalizeOrder() {
        List<CartItemDao.OrderedProduct> items = cartItems.getValue();
        if (items == null) return;

        items.forEach(item -> {
            Product product = new Product();
            product.setId(item.id);
            product.setCardinality(item.cardinality - item.quantity);

            productRepository.updateProduct(product);
        });

        cartRepository.clearCart();
    }

    public void deleteItem(CartItemDao.OrderedProduct item) {
        cartRepository.deleteItemById(item.orderId);
    }
}
