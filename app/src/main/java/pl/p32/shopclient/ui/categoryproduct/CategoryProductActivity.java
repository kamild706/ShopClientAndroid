package pl.p32.shopclient.ui.categoryproduct;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import pl.p32.shopclient.R;
import pl.p32.shopclient.model.Category;

public class CategoryProductActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, CategoryListFragment.OnCategoryChosenListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_product);

        setupToolbar();
        setupNavigation();
        setupNavigation();

        setupFragments();
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
        navigationView.getMenu().getItem(1).setChecked(true);
    }

    private void setupFragments() {
        CategoryListFragment fragment = CategoryListFragment.newInstance();
        fragment.setOnCategoryChosenListener(this);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.categories_container, fragment)
                .commitNow();

        if (!isPortrait()) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.products_container, ProductListFragment.newInstance())
                    .commitNow();
        }
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
        getMenuInflater().inflate(R.menu.category_product, menu);
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

    public boolean isPortrait() {
        return getResources().getBoolean(R.bool.is_portrait);
    }

    @Override
    public void onCategoryChosen(Category category) {
        if (isPortrait()) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.categories_container, ProductListFragment.newInstance());
            transaction.addToBackStack(null);
            transaction.commit();
        }

        setCategoryTitle(category);
    }

    @Override
    public void setCategoryTitle(Category category) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(category.getName());
    }
}
