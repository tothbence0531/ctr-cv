package com.example.ctrl_cv;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import kotlinx.coroutines.Job;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            // If user is not logged in, redirect to login
            finish();
            return;
        }

        // Set up back button
        findViewById(R.id.btn_back).setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });

        // Modern back press handling
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                finish();
                overridePendingTransition(0, 0);
            }
        });

        // Set up UI elements
        TextView tvEmail = findViewById(R.id.tv_email);
        TextView tvName = findViewById(R.id.tv_name);
        MaterialButton btnLogout = findViewById(R.id.btn_logout);

        // Display user info
        tvEmail.setText(user.getEmail());
        // Firebase doesn't store name by default, you might need to implement it separately
        tvName.setText("No name set");

        // Logout button
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(ProfileActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();

                // Redirect to login or main activity
                Intent intent = new Intent(ProfileActivity.this, JobsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
}