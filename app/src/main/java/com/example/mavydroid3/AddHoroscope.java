package com.example.mavydroid3;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.elevation.SurfaceColors;
import com.google.android.material.snackbar.Snackbar;

import java.security.Permission;

public class AddHoroscope extends AppCompatActivity {

    LinearLayout layout;
    ImageView imageView;
    Button cancelBtn;
    Button addBtn;
    EditText horoscopeName;

    ActivityResultLauncher<Intent> startActivityIntent;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_horoscope);

        getWindow().setStatusBarColor(SurfaceColors.SURFACE_2.getColor(this));
        getWindow().setNavigationBarColor(SurfaceColors.SURFACE_2.getColor(this));

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle("Add Horoscope");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        layout = findViewById(R.id.addlayout);
        horoscopeName = findViewById(R.id.name);
        imageView = findViewById(R.id.imageView3);
        cancelBtn = findViewById(R.id.cancel);
        addBtn = findViewById(R.id.add);

        imageView.setClipToOutline(true);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (startActivityIntent != null) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityIntent.launch(intent);
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = horoscopeName.getText().toString();

                if (imageUri == null) {
                    Snackbar.make(layout, "Pag add sag image before ka mo add.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (input.isEmpty()) {
                    Snackbar.make(layout, "Tidert, butngi sag horoscape name uyy.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (ActivityCompat.checkSelfPermission(AddHoroscope.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    new MaterialAlertDialogBuilder(AddHoroscope.this)
                        .setTitle("Request Permission")
                        .setMessage("Accept para makita ang images sa list.")
                        .setNegativeButton("Decline", (dialogInterface, i) -> {

                        })
                        .setPositiveButton("Accept", (dialogInterface, i) -> {
                            requestPermissions(new String[] { Manifest.permission.READ_EXTERNAL_STORAGE }, 500);
                        })
                        .show();

                    return;
                }

                // Create intent
                Intent intent = new Intent();
                // Put horoscope data
                intent.putExtra("horoscope_image", imageUri.toString());
                intent.putExtra("horoscope_name", input);
                // Set result
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        startActivityIntent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Intent intent = result.getData();

                    if (intent == null) return;

                    imageUri = intent.getData();
                    imageView.setImageURI(imageUri);
                }
            }
        );
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 500) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Snackbar.make(layout, "Permission Granted! Balik click ang add button.", Toast.LENGTH_SHORT).show();
            } else {
                Snackbar.make(layout, "Permission Denied! Tidert lang sa.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }
}