package pl.p32.shopclient.ui.productdetails;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import pl.p32.shopclient.GlideApp;
import pl.p32.shopclient.R;
import pl.p32.shopclient.SnackbarMessage;
import pl.p32.shopclient.databinding.ActivityProductDetailsBinding;
import pl.p32.shopclient.databinding.ContentProductDetailsBinding;
import pl.p32.shopclient.model.Product;
import pl.p32.shopclient.ui.barcode.BarcodeActivity;
import pl.p32.shopclient.ui.cart.CartActivity;
import pl.p32.shopclient.ui.categoryproduct.CategoryProductActivity;
import pl.p32.shopclient.ui.currencypicker.CurrencyDialogFragment;
import pl.p32.shopclient.ui.homepage.HomepageActivity;
import pl.p32.shopclient.utils.CurrencyFormatter;
import pl.p32.shopclient.viewmodel.ProductDetailsViewModel;

public class ProductDetailsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String PRODUCT_ID = "PRODUCT_ID";

    private ProductDetailsViewModel model;
    private ContentProductDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        ActivityProductDetailsBinding layout = DataBindingUtil.setContentView(this,
                R.layout.activity_product_details);
        binding = layout.app.vals;

        setupToolbar();
        setupNavigation();

        binding.incrementButton.setOnClickListener(v -> increment());
        binding.decrementButton.setOnClickListener(v -> decrement());
        binding.addToCartButton.setOnClickListener(v -> addToCart());

        model = ViewModelProviders.of(this).get(ProductDetailsViewModel.class);

        String productId = getIntent().getStringExtra(PRODUCT_ID);
        model.init(productId);
        model.getProduct().observe(this, this::handleProduct);
        model.getOrderQuantity().observe(this, this::updateOrderQuantity);

        setupSnackbar();
    }

    private void handleProduct(Product product) {
        binding.setProduct(product);

        CurrencyFormatter formatter = CurrencyFormatter.getInstance();
        binding.productPrice.setText(formatter.getFormattedCurrency(product.getPrice()));
    }

    private void updateOrderQuantity(int quantity) {
        binding.orderQuantity.setText(String.valueOf(quantity));
    }

    private void setupSnackbar() {
        model.getSnackbarMessage().observe(this,
                (SnackbarMessage.SnackbarObserver) snackbarMessageResourceId ->
                        Snackbar.make(findViewById(android.R.id.content), getText(snackbarMessageResourceId),
                                Snackbar.LENGTH_SHORT).show());
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("");

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
    }

    private void setupNavigation() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void addToCart() {
        model.addProductToCart();
    }

    private void decrement() {
        model.decrementOrderQuantity();
    }

    private void increment() {
        model.incrementOrderQuantity();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.product_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_currency) {
            FragmentManager fm = getSupportFragmentManager();
            CurrencyDialogFragment fragment = CurrencyDialogFragment.newInstance();
            fragment.show(fm, "currency_dialog_fragment");
            recreate();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent = null;

        if (id == R.id.nav_main) {
            intent = new Intent(this, HomepageActivity.class);
        } else if (id == R.id.nav_categories) {
            intent = new Intent(this, CategoryProductActivity.class);
        } else if (id == R.id.nav_cart) {
            intent = new Intent(this, CartActivity.class);
        } else if (id == R.id.nav_qr_scan) {
            intent = new Intent(this, BarcodeActivity.class);
        }

        if (intent != null)
            startActivity(intent);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @BindingAdapter("app:imageUrl")
    public static void imageUrl(ImageView imageView, String url) {
        GlideApp
                .with(imageView.getContext())
                .load(url)
                .placeholder(R.drawable.placeholder)
                .into(imageView);
    }

//    public static void
}
