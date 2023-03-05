package com.example.mavydroid3;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.material.elevation.SurfaceColors;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<Horoscope> list = new ArrayList<>();
    ArrayList<Horoscope> copy = new ArrayList<>();

    ListView listView;
    EditText searchInput;
    ArrayAdapter<Horoscope> adapter;

    ActivityResultLauncher<Intent> startActivityIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setStatusBarColor(SurfaceColors.SURFACE_2.getColor(this));
        getWindow().setNavigationBarColor(SurfaceColors.SURFACE_2.getColor(this));

        // Add horoscopes
        list.add(new Horoscope(R.drawable.aquarius, "Aquarius"));
        list.add(new Horoscope(R.drawable.aries, "Aries"));
        list.add(new Horoscope(R.drawable.cancer, "Cancer"));
        list.add(new Horoscope(R.drawable.capricorn, "Capricorn"));
        list.add(new Horoscope(R.drawable.gemini, "Gemini"));
        list.add(new Horoscope(R.drawable.leo, "Leo"));

        // Copy horoscope
        copy = new ArrayList<>(list);

        // Create adapter
        adapter = new HoroscopeAdapter(this, android.R.layout.simple_list_item_1, copy);

        // Get elements
        listView = findViewById(R.id.list);
        searchInput = findViewById(R.id.searchText);

        // Set adapter
        listView.setAdapter(adapter);
        listView.setEmptyView(findViewById(R.id.empty));

        // Set on change text listener for search feature
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence text, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence text, int i, int i1, int i2) {
                copy.clear();

                for (Horoscope horoscope : list) {
                    if (horoscope.getName().toLowerCase().contains(text.toString().toLowerCase())) {
                        copy.add(horoscope);
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // Set activity intent
        startActivityIntent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Intent intent = result.getData();

                    if (intent != null) {
                        String uriString = intent.getStringExtra("horoscope_image");
                        String input = intent.getStringExtra("horoscope_name");

                        // Create a new horoscope item
                        Horoscope horoscope = new Horoscope(Uri.parse(uriString), input);

                        // Add it to the original and copy list
                        list.add(horoscope);
                        copy.add(horoscope);

                        // Update adapter
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent(this, AddHoroscope.class);
        startActivityIntent.launch(intent);

        return super.onOptionsItemSelected(item);
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
