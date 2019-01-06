package pl.p32.shopclient.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
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
    private MutableLiveData<Integer> productQuantity = new MutableLiveData<>(1);
    private final SnackbarMessage mSnackbarText = new SnackbarMessage();

    public ProductDetailsViewModel(@NonNull Application application) {
        super(application);
        repository = ProductRepository.getInstance(application);
        cartRepository = CartRepository.getInstance(application);
    }

    public SnackbarMessage getSnackbarMessage() {
        return mSnackbarText;
    }

    public LiveData<Product> getProduct(String productId) {
        if (product == null)
            product = repository.getProduct(productId);
        return product;
    }

    public void incrementProductQuantity() {
        if (productQuantity.getValue() == null || product.getValue() == null) return;
        if (productQuantity.getValue() + 1 <= product.getValue().getCardinality()) {
            productQuantity.setValue(productQuantity.getValue() + 1);
        }
    }

    public void decrementProductQuantity() {
        if (productQuantity.getValue() == null) return;
        if (productQuantity.getValue() > 1) {
            productQuantity.setValue(productQuantity.getValue() - 1);
        }
    }

    public LiveData<Integer> getProductQuantity() {
        return productQuantity;
    }

    public void addProductToCart() {
        Product product = this.product.getValue();
        if (product == null) return;

        if (cartRepository.isItemInCart(product)) {
            mSnackbarText.setValue(R.string.product_already_in_cart);
        } else {
            CartItem item = new CartItem();
            item.setProductId(product.getId());
            item.setQuantity(productQuantity.getValue());

            cartRepository.addToCart(item);
            mSnackbarText.setValue(R.string.product_added_to_cart);
        }
    }
}
