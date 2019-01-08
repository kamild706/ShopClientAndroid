package pl.p32.shopclient.ui.barcode;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.FileNotFoundException;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import pl.p32.shopclient.BuildConfig;
import pl.p32.shopclient.R;
import pl.p32.shopclient.ui.cart.CartActivity;
import pl.p32.shopclient.ui.categoryproduct.CategoryProductActivity;
import pl.p32.shopclient.ui.currencypicker.CurrencyDialogFragment;
import pl.p32.shopclient.ui.homepage.HomepageActivity;
import pl.p32.shopclient.ui.productdetails.ProductDetailsActivity;

public class BarcodeActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    private static final int PHOTO_REQUEST = 10;
    private TextView scanResults;
    private BarcodeDetector detector;
    private Uri imageUri;
    private static final int REQUEST_WRITE_PERMISSION = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);

        setupToolbar();
        setupNavigation();
       /* Button button = (Button) findViewById(R.id.button);
        scanResults = (TextView) findViewById(R.id.scan_results);

        button.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                takePicture();
            } else {
                ActivityCompat.requestPermissions(this, new
                        String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
            }
        });*/

        detector = new BarcodeDetector.Builder(getApplicationContext())
                .setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.QR_CODE)
                .build();
        if (!detector.isOperational()) {
            scanResults.setText("Could not set up the detector!");
            return;
        }
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePicture();
                } else {
                    Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_REQUEST && resultCode == RESULT_OK) {
            launchMediaScanIntent();
            handleCapturedPicture();
        }
    }

    private void handleCapturedPicture() {
        try {
            Bitmap bitmap = decodeBitmapUri(this, imageUri);
            if (detector.isOperational() && bitmap != null) {
                Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                SparseArray<Barcode> barcodes = detector.detect(frame);

                if (barcodes.size() == 0) {
                    scanResults.setText("Scan Failed: Found nothing to scan");
                    return;
                }

                Barcode code = barcodes.valueAt(0);
                scanResults.setText(code.displayValue);

                Intent intent = new Intent(this, ProductDetailsActivity.class);
                intent.putExtra(ProductDetailsActivity.PRODUCT_ID, code.displayValue);
                startActivity(intent);
            } else {
                scanResults.setText("Could not set up the detector!");
            }
        } catch (FileNotFoundException e) {
            Toast.makeText(this, "Failed to load Image", Toast.LENGTH_SHORT).show();
        }
    }

    private void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photo = new File(Environment.getExternalStorageDirectory(), "picture.jpg");
        imageUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, PHOTO_REQUEST);
    }

    private void launchMediaScanIntent() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(imageUri);
        sendBroadcast(mediaScanIntent);
    }

    private Bitmap decodeBitmapUri(Context ctx, Uri uri) throws FileNotFoundException {
        int targetW = 600;
        int targetH = 600;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(ctx.getContentResolver().openInputStream(uri), null, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        return BitmapFactory.decodeStream(ctx.getContentResolver()
                .openInputStream(uri), null, bmOptions);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.homepage_menu, menu);
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
            intent = new Intent(this, CartActivity.class);
        } else if (id == R.id.nav_qr_scan) {
            // we are here
        }

        if (intent != null)
            startActivity(intent);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
