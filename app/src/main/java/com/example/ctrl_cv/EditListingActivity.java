package com.example.ctrl_cv;

import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditListingActivity extends AppCompatActivity {

    private TextInputEditText etTitle, etCompany, etLocation, etMinSalary, etMaxSalary, etDescription;
    private MaterialButton btnUpdate;

    private FirebaseFirestore db;
    private String listingId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_edit_listing);

        etTitle = findViewById(R.id.etTitle);
        etCompany = findViewById(R.id.etCompany);
        etLocation = findViewById(R.id.etLocation);
        etMinSalary = findViewById(R.id.etMinSalary);
        etMaxSalary = findViewById(R.id.etMaxSalary);
        etDescription = findViewById(R.id.etDescription);
        btnUpdate = findViewById(R.id.btnUpdate);

        db = FirebaseFirestore.getInstance();
        listingId = getIntent().getStringExtra("listingId");

        if (listingId == null) {
            Toast.makeText(this, "Invalid listing ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadListingData();

        btnUpdate.setOnClickListener(v -> updateListing());
    }

    private void loadListingData() {
        db.collection("Listings").document(listingId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    Listing listing = documentSnapshot.toObject(Listing.class);
                    if (listing != null) {
                        etTitle.setText(listing.getTitle());
                        etCompany.setText(listing.getCompanyName());
                        etLocation.setText(listing.getLocation());
                        etMinSalary.setText(String.valueOf(listing.getMinSalary()));
                        etMaxSalary.setText(String.valueOf(listing.getMaxSalary()));
                        etDescription.setText(listing.getDescription());
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load listing", Toast.LENGTH_SHORT).show();
                    finish();
                });
    }

    private void updateListing() {
        String title = etTitle.getText().toString().trim();
        String company = etCompany.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String minSalaryStr = etMinSalary.getText().toString().trim();
        String maxSalaryStr = etMaxSalary.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        if (title.isEmpty() || company.isEmpty() || location.isEmpty()
                || minSalaryStr.isEmpty() || maxSalaryStr.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int minSalary = Integer.parseInt(minSalaryStr);
        int maxSalary = Integer.parseInt(maxSalaryStr);

        Listing updatedListing = new Listing(
                listingId,
                title,
                company,
                location,
                minSalary,
                maxSalary,
                description,
                FirebaseAuth.getInstance().getCurrentUser().getUid()
        );

        db.collection("Listings").document(listingId)
                .set(updatedListing)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Listing updated", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show());
    }
}
