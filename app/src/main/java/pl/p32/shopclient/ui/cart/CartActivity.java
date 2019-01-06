package pl.p32.shopclient.ui.cart;

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
import java.util.function.Consumer;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pl.p32.shopclient.R;
import pl.p32.shopclient.db.dao.CartItemDao;
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

        proceedButton.setOnClickListener(v -> finalizeOrder());
    }

    private void onItemsReceived(List<CartItemDao.OrderedProduct> items) {
        mAdapter.setOrderedProducts(items);
        BigDecimal sum = new BigDecimal(0);
        for (CartItemDao.OrderedProduct item : items) {
            BigDecimal quantity = BigDecimal.valueOf(item.quantity);
            BigDecimal productValue = quantity.multiply(item.price);
            sum = sum.add(productValue);
        }

        priceView.setText(sum.toString());
    }

    private void finalizeOrder() {
        mViewModel.finalizeOrder();
        Snackbar.make(findViewById(android.R.id.content), getString(R.string.order_confirmed), Snackbar.LENGTH_SHORT);
    }

    private void setupRecyclerView() {
        RecyclerView mRecyclerView = findViewById(R.id.cart_list);
        mRecyclerView.setNestedScrollingEnabled(false);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

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
