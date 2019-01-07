package pl.p32.shopclient.ui.homepage;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.navigation.NavigationView;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pl.p32.shopclient.R;
import pl.p32.shopclient.model.Product;
import pl.p32.shopclient.ui.barcode.BarcodeActivity;
import pl.p32.shopclient.ui.cart.CartActivity;
import pl.p32.shopclient.ui.categoryproduct.CategoryProductActivity;
import pl.p32.shopclient.ui.productdetails.ProductDetailsActivity;
import pl.p32.shopclient.viewmodel.HomepageViewModel;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class HomepageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ProductsGridAdapter.ItemClickListener {

    private ProductsGridAdapter mAdapter;
    private HomepageViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        setupToolbar();
        setupNavigation();
        setupRecyclerView();

        mViewModel = ViewModelProviders.of(this).get(HomepageViewModel.class);
        mViewModel.getRandomProducts().observe(this, products -> mAdapter.setProducts(products));
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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

    private void setupRecyclerView() {
        RecyclerView mRecyclerView = findViewById(R.id.products_for_customer);
        mRecyclerView.setNestedScrollingEnabled(false);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new ProductsGridAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setClickListener(this);
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
        getMenuInflater().inflate(R.menu.homepage_menu, menu);
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

        if (id == R.id.nav_main) {
            // we are here
        } else if (id == R.id.nav_categories) {
            Intent intent = new Intent(this, CategoryProductActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_cart) {
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_qr_scan) {
            Intent intent = new Intent(this, BarcodeActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, ProductDetailsActivity.class);
        Product product = mAdapter.getItem(position);
        intent.putExtra(ProductDetailsActivity.PRODUCT_ID, product.getId());
        startActivity(intent);
    }
}
