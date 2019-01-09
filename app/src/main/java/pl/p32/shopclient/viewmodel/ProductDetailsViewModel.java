package pl.p32.shopclient.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import pl.p32.shopclient.SnackbarMessage;
import pl.p32.shopclient.model.CartItem;
import pl.p32.shopclient.model.Product;
import pl.p32.shopclient.repository.CartRepository;
import pl.p32.shopclient.repository.ProductRepository;
import pl.p32.shopclient.R;

public class ProductDetailsViewModel extends AndroidViewModel {

    private final ProductRepository repository;
    private final CartRepository cartRepository;
    private LiveData<Product> product;
    private MutableLiveData<Integer> orderQuantity = new MutableLiveData<>(1);
    private final SnackbarMessage mSnackbarText = new SnackbarMessage();

    public ProductDetailsViewModel(@NonNull Application application) {
        super(application);
        repository = ProductRepository.getInstance(application);
        cartRepository = CartRepository.getInstance(application);
    }

    public void init(String productId) {
        product = repository.getProduct(productId);
    }

    public SnackbarMessage getSnackbarMessage() {
        return mSnackbarText;
    }

    public LiveData<Product> getProduct() {
        return product;
    }

    public void incrementOrderQuantity() {
        if (orderQuantity.getValue() == null || product.getValue() == null) return;
        if (orderQuantity.getValue() + 1 <= product.getValue().getCardinality()) {
            orderQuantity.setValue(orderQuantity.getValue() + 1);
        }
    }

    public void decrementOrderQuantity() {
        if (orderQuantity.getValue() == null) return;
        if (orderQuantity.getValue() > 1) {
            orderQuantity.setValue(orderQuantity.getValue() - 1);
        }
    }

    public LiveData<Integer> getOrderQuantity() {
        return orderQuantity;
    }

    public void addProductToCart() {
        Product product = this.product.getValue();
        if (product == null) return;

        if (cartRepository.isItemInCart(product)) {
            mSnackbarText.setValue(R.string.product_already_in_cart);
        } else {
            CartItem item = new CartItem();
            item.setProductId(product.getId());
            item.setQuantity(orderQuantity.getValue());

            cartRepository.addToCart(item);
            mSnackbarText.setValue(R.string.product_added_to_cart);
        }
    }
}
