package com.example.ctrl_cv;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class JobsActivity extends AppCompatActivity {

    private static final String TAG = JobsActivity.class.getName();
    private FirebaseUser user;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Először ellenőrizd a bejelentkezési állapotot
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            // Ha nincs bejelentkezve felhasználó, irányítsd a bejelentkező oldalra
            startActivity(new Intent(this, LoginActivity.class));
            finish(); // Ne hagyd meg ezt az Activity-t a back stack-ben
            return;
        }

        // Ha be van jelentkezve, folytasd a normál működéssel
        setContentView(R.layout.activity_jobs);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            finish();
            return;
        }

        // Modern back press handling for MainActivity
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Exit the app when back is pressed from MainActivity
                finishAffinity();
            }
        });

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_jobs) {
                    // Already on jobs page
                    return true;
                } else if (itemId == R.id.nav_my_listings) {
                    startActivity(new Intent(JobsActivity.this, MyListingsActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    startActivity(new Intent(JobsActivity.this, ProfileActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Always show Jobs as selected when returning to MainActivity
        bottomNavigationView.setSelectedItemId(R.id.nav_jobs);
    }
}