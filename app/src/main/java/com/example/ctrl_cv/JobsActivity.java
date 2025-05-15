package com.example.ctrl_cv;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class JobsActivity extends AppCompatActivity {

    private static final String TAG = JobsActivity.class.getName();
    private FirebaseUser user;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        LinearLayout mainLayout = findViewById(R.id.cardcontainer);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Listings").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                Listing listing = doc.toObject(Listing.class);
                if (listing != null) {
                    View jobCard = getLayoutInflater().inflate(R.layout.job_card, mainLayout, false);

                    TextView titleText = jobCard.findViewById(R.id.titleTextView);
                    TextView companyText = jobCard.findViewById(R.id.companyTextView);
                    TextView locationSalaryText = jobCard.findViewById(R.id.locationSalaryTextView);
                    TextView descriptionText = jobCard.findViewById(R.id.descriptionTextView);
                    Button applyButton = jobCard.findViewById(R.id.applyButton);

                    titleText.setText(listing.getTitle());
                    companyText.setText(listing.getCompanyName());
                    String salaryRange = "€" + listing.getMinSalary() + " - €" + listing.getMaxSalary();
                    locationSalaryText.setText(listing.getLocation() + " · " + salaryRange);
                    descriptionText.setText(listing.getDescription());

                    applyButton.setOnClickListener(v -> {
                        FirebaseUser checkuser = FirebaseAuth.getInstance().getCurrentUser();
                        if (checkuser != null) {
                            String userId = checkuser.getUid();
                            String listingId = doc.getId();

                            // 1. Saját hirdetésre ne tudjon jelentkezni
                            if (userId.equals(listing.getUserId())) {
                                Toast.makeText(JobsActivity.this, "You can't apply to your own listing.", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            FirebaseFirestore dbInstance = FirebaseFirestore.getInstance();

                            // 2. Ellenőrizni, hogy már jelentkezett-e
                            dbInstance.collection("Applications")
                                    .whereEqualTo("userId", userId)
                                    .whereEqualTo("listingId", listingId)
                                    .get()
                                    .addOnSuccessListener(querySnapshot -> {
                                        if (!querySnapshot.isEmpty()) {
                                            Toast.makeText(JobsActivity.this, "You have already applied for this job.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            // Ha még nem jelentkezett, létrehozzuk az új application-t
                                            Application application = new Application(userId, listingId, "pending");

                                            dbInstance.collection("Applications")
                                                    .add(application)
                                                    .addOnSuccessListener(documentReference -> {
                                                        String applicationId = documentReference.getId();
                                                        Log.d(TAG, "Application submitted: " + applicationId);
                                                        Toast.makeText(JobsActivity.this, "Application submitted successfully!", Toast.LENGTH_SHORT).show();

                                                        // Frissítjük a felhasználó applications mezőjét
                                                        dbInstance.collection("Users")
                                                                .document(userId)
                                                                .update("applications", com.google.firebase.firestore.FieldValue.arrayUnion(applicationId))
                                                                .addOnSuccessListener(unused -> Log.d(TAG, "Application ID added to user document"))
                                                                .addOnFailureListener(e -> Log.e(TAG, "Failed to update user document with application ID", e));
                                                    })

                                                    .addOnFailureListener(e -> {
                                                        Log.e(TAG, "Error submitting application", e);
                                                        Toast.makeText(JobsActivity.this, "Failed to submit application.", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e(TAG, "Error checking existing applications", e);
                                        Toast.makeText(JobsActivity.this, "Could not check previous applications.", Toast.LENGTH_SHORT).show();
                                    });
                        }
                    });


                    mainLayout.addView(jobCard);
                }
            }
        }).addOnFailureListener(e -> {
            Log.e("Firestore", "Error loading listings", e);
        });

        // Bottom nav setup...
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_jobs) {
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
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigationView.setSelectedItemId(R.id.nav_jobs);
    }
}