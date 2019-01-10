package pl.p32.shopclient.ui.cart;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.math.BigDecimal;
import java.util.List;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pl.p32.shopclient.R;
import pl.p32.shopclient.db.dao.CartItemDao;
import pl.p32.shopclient.ui.barcode.BarcodeActivity;
import pl.p32.shopclient.ui.categoryproduct.CategoryProductActivity;
import pl.p32.shopclient.ui.currencypicker.CurrencyDialogFragment;
import pl.p32.shopclient.ui.homepage.HomepageActivity;
import pl.p32.shopclient.ui.search.SearchActivity;
import pl.p32.shopclient.utils.CurrencyFormatter;
import pl.p32.shopclient.viewmodel.CartViewModel;

public class CartActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, CartItemListAdapter.ItemClickListener {

    private CartViewModel mViewModel;
    private CartItemListAdapter mAdapter;
    private Button proceedButton;
    private TextView priceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        setupToolbar();
        setupNavigation();
        setupRecyclerView();

        proceedButton = findViewById(R.id.proceed_button);
        priceView = findViewById(R.id.price_view);

        mViewModel = ViewModelProviders.of(this).get(CartViewModel.class);
        mViewModel.getCartItems().observe(this, this::onItemsReceived);
        mViewModel.getTotalPrice().observe(this, this::showTotalPrice);

        proceedButton.setOnClickListener(v -> finalizeOrder());
    }

    private void showTotalPrice(BigDecimal price) {
        CurrencyFormatter formatter = CurrencyFormatter.getInstance();
        priceView.setText(formatter.getFormattedCurrency(price));
    }

    private void onItemsReceived(List<CartItemDao.OrderedProduct> items) {
        proceedButton.setEnabled(items.size() != 0);
        mAdapter.setOrderedProducts(items);
    }

    private void finalizeOrder() {
        mViewModel.finalizeOrder();
        Snackbar.make(findViewById(android.R.id.content), getString(R.string.order_confirmed), Snackbar.LENGTH_SHORT).show();
    }

    private void setupRecyclerView() {
        RecyclerView mRecyclerView = findViewById(R.id.cart_list);
        mRecyclerView.setNestedScrollingEnabled(false);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new CartItemListAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setClickListener(this);
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
        getMenuInflater().inflate(R.menu.cart, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent = null;

        if (id == R.id.nav_main) {
            intent = new Intent(this, HomepageActivity.class);
        } else if (id == R.id.nav_categories) {
            intent = new Intent(this, CategoryProductActivity.class);
        } else if (id == R.id.nav_cart) {
            // we are here
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
        CartItemDao.OrderedProduct item = mAdapter.getItem(position);
        mViewModel.deleteItem(item);
    }
}
