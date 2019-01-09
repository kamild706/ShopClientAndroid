package pl.p32.shopclient.ui.barcode;

import android.Manifest;
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
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import pl.p32.shopclient.BuildConfig;
import pl.p32.shopclient.R;
import pl.p32.shopclient.ui.cart.CartActivity;
import pl.p32.shopclient.ui.categoryproduct.CategoryProductActivity;
import pl.p32.shopclient.ui.homepage.HomepageActivity;
import pl.p32.shopclient.ui.productdetails.ProductDetailsActivity;

public class BarcodeActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    private static final int PHOTO_REQUEST = 10;
    public static final String ERROR_TEXT_VIEW = "ERROR_TEXT_VIEW";
    private TextView errorTextView;
    private BarcodeDetector detector;
    private Uri imageUri;
    private static final int REQUEST_WRITE_PERMISSION = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);

        errorTextView = findViewById(R.id.error_tv);
        if (savedInstanceState != null) {
            String previousError = savedInstanceState.getString(ERROR_TEXT_VIEW);
            errorTextView.setText(previousError);
        }

        setupToolbar();
        setupNavigation();
        setupBarcodeDetector();

        Button button = findViewById(R.id.button);
        button.setOnClickListener(view -> handleButtonClick());
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ERROR_TEXT_VIEW, errorTextView.getText().toString());
    }

    private void setupBarcodeDetector() {
        detector = new BarcodeDetector.Builder(getApplicationContext())
                .setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.QR_CODE)
                .build();
        if (!detector.isOperational()) {
            errorTextView.setText(getString(R.string.barcode_scanner_unavailable));
        }
    }

    private void handleButtonClick() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            takePicture();
        } else {
            ActivityCompat.requestPermissions(this, new
                    String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
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
        if (requestCode == REQUEST_WRITE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePicture();
            } else {
                errorTextView.setText(getString(R.string.qr_code_storage_permission_denied));
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
                    errorTextView.setText(getString(R.string.qr_code_not_found));
                    return;
                }

                Barcode code = barcodes.valueAt(0);
                Intent intent = new Intent(this, ProductDetailsActivity.class);
                intent.putExtra(ProductDetailsActivity.PRODUCT_ID, code.displayValue);
                startActivity(intent);
            } else {
                errorTextView.setText(getString(R.string.barcode_scanner_unavailable));
            }
        } catch (FileNotFoundException e) {
            errorTextView.setText(getString(R.string.captured_picture_not_found));
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
