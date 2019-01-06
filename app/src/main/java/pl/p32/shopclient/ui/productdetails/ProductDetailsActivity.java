package pl.p32.shopclient.ui.productdetails;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;
import pl.p32.shopclient.GlideApp;
import pl.p32.shopclient.R;
import pl.p32.shopclient.SnackbarMessage;
import pl.p32.shopclient.model.Product;
import pl.p32.shopclient.viewmodel.ProductDetailsViewModel;

public class ProductDetailsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String PRODUCT_ID = "PRODUCT_ID";

    private ProductDetailsViewModel model;
    private ImageView imageView;
    private TextView nameView;
    private TextView descView;
    private TextView cardinalityView;
    private TextView priceView;
    private TextView numberView;
    private Button incrementButton;
    private Button decrementButton;
    private Button addToCartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        setupToolbar();
        setupNavigation();

        imageView = findViewById(R.id.product_image);
        nameView = findViewById(R.id.product_name);
        descView = findViewById(R.id.product_description);
        cardinalityView = findViewById(R.id.product_cardinality);
        priceView = findViewById(R.id.product_price);
        numberView = findViewById(R.id.product_count);

        incrementButton = findViewById(R.id.increment);
        decrementButton = findViewById(R.id.decrement);
        addToCartButton = findViewById(R.id.addToCart);

        incrementButton.setOnClickListener(v -> increment());
        decrementButton.setOnClickListener(v -> decrement());
        addToCartButton.setOnClickListener(v -> addToCart());

        model = ViewModelProviders.of(this).get(ProductDetailsViewModel.class);

        String productId = getIntent().getStringExtra(PRODUCT_ID);
        model.getProduct(productId).observe(this, this::showProduct);
        model.getProductQuantity().observe(this, num -> numberView.setText(String.valueOf(num)));

        setupSnackbar();
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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void setupNavigation() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void addToCart() {
        model.addProductToCart();
    }

    private void decrement() {
        model.decrementProductQuantity();
    }

    private void increment() {
        model.incrementProductQuantity();
    }

    private void showProduct(Product product) {
        nameView.setText(product.getName());
        descView.setText(product.getDescription());
        cardinalityView.setText(String.valueOf(product.getCardinality()));
        priceView.setText(String.valueOf(product.getPrice()));
        GlideApp
                .with(this)
                .load(product.getImageUrl())
                .placeholder(R.drawable.placeholder)
                .into(imageView);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
