package com.example.ctrl_cv;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

public class MyListingsActivity extends AppCompatActivity {

    private LinearLayout myListingsContainer;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private static final String TAG = "MyListingsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_my_listings);

        myListingsContainer = findViewById(R.id.cardContainer);
        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

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

        findViewById(R.id.btn_add_listing).setOnClickListener(v -> {
            startActivity(new Intent(MyListingsActivity.this, AddListingActivity.class));
        });

        loadMyListings();
    }

    private void loadMyListings() {
        db.collection("Listings")
                .whereEqualTo("userId", currentUser.getUid())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Listing listing = doc.toObject(Listing.class);
                        if (listing != null) {
                            String listingId = doc.getId();
                            View listingCard = LayoutInflater.from(this).inflate(R.layout.my_listing_card, myListingsContainer, false);

                            ((TextView) listingCard.findViewById(R.id.titleTextView)).setText(listing.getTitle());
                            ((TextView) listingCard.findViewById(R.id.companyTextView)).setText(listing.getCompanyName());
                            String salaryRange = "€" + listing.getMinSalary() + " - €" + listing.getMaxSalary();
                            ((TextView) listingCard.findViewById(R.id.locationSalaryTextView)).setText(listing.getLocation() + " · " + salaryRange);
                            ((TextView) listingCard.findViewById(R.id.descriptionTextView)).setText(listing.getDescription());

                            Button deleteButton = listingCard.findViewById(R.id.deleteButton);
                            Button editButton = listingCard.findViewById(R.id.editButton);

                            deleteButton.setVisibility(View.VISIBLE);
                            editButton.setVisibility(View.VISIBLE);

                            deleteButton.setOnClickListener(v -> confirmAndDeleteListing(listingId));

                            editButton.setOnClickListener(v -> {
                                Intent intent = new Intent(MyListingsActivity.this, EditListingActivity.class);
                                intent.putExtra("listingId", listingId);
                                startActivity(intent);
                            });

                            myListingsContainer.addView(listingCard);


                            LinearLayout applicationsContainer = new LinearLayout(this);
                            applicationsContainer.setOrientation(LinearLayout.VERTICAL);
                            ((LinearLayout) listingCard.findViewById(R.id.cardContent)).addView(applicationsContainer);

                            loadApplicationsForListing(listingId, applicationsContainer);

                        }
                    }
                })
                .addOnFailureListener(e -> Log.e(TAG, "Error loading listings", e));
    }

    private void updateApplicationStatus(String applicationId, String newStatus, View applicationView) {
        db.collection("Applications").document(applicationId)
                .update("status", newStatus)
                .addOnSuccessListener(aVoid -> {
                    ((TextView) applicationView.findViewById(R.id.statusText)).setText("Status: " + newStatus);
                    Toast.makeText(this, "Application " + newStatus, Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to update status", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error updating status", e);
                });
    }

    private void loadApplicationsForListing(String listingId, LinearLayout container) {
        db.collection("Applications")
                .whereEqualTo("listingId", listingId)
                .whereEqualTo("status", "pending")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        String applicationId = doc.getId();
                        String applicantId = doc.getString("userId");

                        db.collection("Users").document(applicantId)
                                .get()
                                .addOnSuccessListener(userDoc -> {
                                    String name = userDoc.getString("name");
                                    String email = userDoc.getString("email");
                                    String phone = userDoc.getString("phoneNumber");

                                    View applicationView = LayoutInflater.from(this).inflate(R.layout.application_card, container, false);

                                    ((TextView) applicationView.findViewById(R.id.applicantName)).setText("Name: " + name);
                                    ((TextView) applicationView.findViewById(R.id.applicantEmail)).setText("Email: " + email);
                                    ((TextView) applicationView.findViewById(R.id.statusText)).setText("Phone: " + phone);

                                    Button acceptBtn = applicationView.findViewById(R.id.acceptButton);
                                    Button rejectBtn = applicationView.findViewById(R.id.rejectButton);

                                    acceptBtn.setOnClickListener(v -> updateApplicationStatus(applicationId, "accepted", applicationView));
                                    rejectBtn.setOnClickListener(v -> updateApplicationStatus(applicationId, "rejected", applicationView));

                                    container.addView(applicationView);
                                });
                    }
                });
    }



    private void confirmAndDeleteListing(String listingId) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Listing")
                .setMessage("Are you sure you want to delete this listing?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    deleteListingAndApplications(listingId);
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteListingAndApplications(String listingId) {
        db.collection("Applications")
                .whereEqualTo("listingId", listingId)
                .get()
                .addOnSuccessListener(snapshot -> {
                    for (DocumentSnapshot doc : snapshot.getDocuments()) {
                        String applicationId = doc.getId();
                        String userId = doc.getString("userId");

                        db.collection("Users").document(userId)
                                .update("applications", com.google.firebase.firestore.FieldValue.arrayRemove(applicationId));

                        db.collection("Applications").document(applicationId).delete();
                    }

                    db.collection("Listings").document(listingId).delete();


                    db.collection("Users").document(currentUser.getUid())
                            .update("myListings", com.google.firebase.firestore.FieldValue.arrayRemove(listingId));

                    Toast.makeText(this, "Listing deleted successfully", Toast.LENGTH_SHORT).show();

                    myListingsContainer.removeAllViews();
                    loadMyListings();
                })
                .addOnFailureListener(e -> Log.e(TAG, "Error deleting applications", e));
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }
}
