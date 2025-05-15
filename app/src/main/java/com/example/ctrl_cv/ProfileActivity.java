package com.example.ctrl_cv;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";

    TextView tvName, tvEmail, tvPhone, tvListingsCount, tvApplicationsCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            finish();
            return;
        }

        tvName = findViewById(R.id.tv_name);
        tvEmail = findViewById(R.id.tv_email);
        tvPhone = findViewById(R.id.tv_phone);
        tvListingsCount = findViewById(R.id.tv_listings_count);
        tvApplicationsCount = findViewById(R.id.tv_applications_count);

        tvEmail.setText(user.getEmail()); // Auth-ból

        // Firestore-ból adatlekérés
        FirebaseFirestore.getInstance()
                .collection("Users")
                .document(user.getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String name = documentSnapshot.getString("nev");
                        String phone = "Phone number: " + documentSnapshot.getString("phoneNumber");

                        tvName.setText(name != null ? name : "No name");
                        tvPhone.setText(phone != null ? phone : "No phone number");

                        List<String> myListings = (List<String>) documentSnapshot.get("myListings");
                        int listingsCount = myListings != null ? myListings.size() : 0;

                        List<String> myApplications = (List<String>) documentSnapshot.get("applications");
                        int applicationsCount = myApplications != null ? myApplications.size() : 0;

                        tvListingsCount.setText("Listings: " + listingsCount);
                        tvApplicationsCount.setText("Applications: " + applicationsCount);

                    } else {
                        Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to load user data", e);
                    Toast.makeText(this, "Failed to load data", Toast.LENGTH_SHORT).show();
                });

        findViewById(R.id.btn_back).setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
                overridePendingTransition(0, 0);
            }
        });

        MaterialButton btnLogout = findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(ProfileActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ProfileActivity.this, JobsActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        });
    }
}
