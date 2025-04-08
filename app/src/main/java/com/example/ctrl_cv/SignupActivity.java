package com.example.ctrl_cv;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = SignupActivity.class.getName();
    private TextInputEditText emailET;
    private TextInputEditText passwordET;
    private TextInputEditText rePasswordET;
    private TextInputEditText nameET;
    private TextInputEditText phoneET;
    private FirebaseAuth mAuth;


    private static final String TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJDdHJsK0NWIiwiaWF0IjoxNzQzOTY2ODU5LCJleHAiOjE3NzU1MDI4NTksImF1ZCI6Ind3dy5leGFtcGxlLmNvbSIsInN1YiI6IiIsIkdpdmVuTmFtZSI6IkpvaG5ueSIsIlN1cm5hbWUiOiJSb2NrZXQiLCJFbWFpbCI6Impyb2NrZXRAZXhhbXBsZS5jb20iLCJSb2xlIjpbIk1hbmFnZXIiLCJQcm9qZWN0IEFkbWluaXN0cmF0b3IiXX0.zVaOYr-rJFYI8vMKhec7ku6YpjIfwYzqc1-ePN3y5Qo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (!TOKEN.equals(getIntent().getStringExtra("TOKEN"))) {
            finish();
        }

        mAuth = FirebaseAuth.getInstance();

        // TODO: kiszedni a USER.NAME-et es bearkni az inputba

        this.emailET = findViewById(R.id.emailEditText);
        this.passwordET = findViewById(R.id.passwordEditText);
        this.rePasswordET = findViewById(R.id.confirmPasswordEditText);
        this.nameET = findViewById(R.id.nameEditText);
        this.phoneET = findViewById(R.id.phoneEditText);

    }

    public void onLoginClick(View view) {
        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void toMainPage() {
        Intent intent = new Intent(SignupActivity.this, JobsActivity.class);
        startActivity(intent);
    }

    public void signup(View view) {
        String email = Objects.requireNonNull(this.emailET.getText()).toString();
        String password = Objects.requireNonNull(this.passwordET.getText()).toString();
        String rePassword = Objects.requireNonNull(this.rePasswordET.getText()).toString();
        String name = Objects.requireNonNull(this.nameET.getText()).toString();
        String phone = Objects.requireNonNull(this.phoneET.getText()).toString();

        if(email.isEmpty() || password.isEmpty() || rePassword.isEmpty() || name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Fill in every field", Toast.LENGTH_SHORT).show();
        }else if (!password.equals(rePassword)) {
            Toast.makeText(this, "Passwords don't match", Toast.LENGTH_SHORT).show();
        } else {
            Log.i(TAG, "email: " + email + "\npassword: " + password + "\nrepassword: " + rePassword + "\nname: " + name + "\nphone: " + phone);

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        Log.d(TAG, "onComplete: User added successfully");
                        toMainPage();
                    } else {
                        Log.d(TAG, "onComplete: Failed to add user");
                        Toast.makeText(SignupActivity.this, "Failed to add user: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }


    }
}