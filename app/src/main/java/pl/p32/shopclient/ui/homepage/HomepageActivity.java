package pl.p32.shopclient.ui.homepage;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pl.p32.shopclient.R;
import pl.p32.shopclient.model.Product;
import pl.p32.shopclient.ui.GridSpacingItemDecoration;
import pl.p32.shopclient.ui.barcode.BarcodeActivity;
import pl.p32.shopclient.ui.cart.CartActivity;
import pl.p32.shopclient.ui.categoryproduct.CategoryProductActivity;
import pl.p32.shopclient.ui.currencypicker.CurrencyDialogFragment;
import pl.p32.shopclient.ui.productdetails.ProductDetailsActivity;
import pl.p32.shopclient.ui.search.SearchActivity;
import pl.p32.shopclient.utils.CurrencyFormatter;
import pl.p32.shopclient.viewmodel.HomepageViewModel;

import android.util.Log;
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
        CurrencyFormatter.getInstance(getApplication());

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

        int columns = getResources().getInteger(R.integer.grid_columns);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, columns);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(columns, 25, false));

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

        /*SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        ComponentName cn = new ComponentName(this, SearchActivity.class);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(cn));*/

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
        } else if (id == R.id.action_search) {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent = null;

        if (id == R.id.nav_main) {
            // we are here
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

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, ProductDetailsActivity.class);
        Product product = mAdapter.getItem(position);
        intent.putExtra(ProductDetailsActivity.PRODUCT_ID, product.getId());
        startActivity(intent);
    }
}
