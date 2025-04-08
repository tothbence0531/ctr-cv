package com.example.ctrl_cv;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
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

public class LoginActivity extends AppCompatActivity {

    private static final String TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJDdHJsK0NWIiwiaWF0IjoxNzQzOTY2ODU5LCJleHAiOjE3NzU1MDI4NTksImF1ZCI6Ind3dy5leGFtcGxlLmNvbSIsInN1YiI6IiIsIkdpdmVuTmFtZSI6IkpvaG5ueSIsIlN1cm5hbWUiOiJSb2NrZXQiLCJFbWFpbCI6Impyb2NrZXRAZXhhbXBsZS5jb20iLCJSb2xlIjpbIk1hbmFnZXIiLCJQcm9qZWN0IEFkbWluaXN0cmF0b3IiXX0.zVaOYr-rJFYI8vMKhec7ku6YpjIfwYzqc1-ePN3y5Qo";
    private static final String TAG = LoginActivity.class.getName();
    private TextInputEditText emailET;
    private TextInputEditText passwordET;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
        mAuth = FirebaseAuth.getInstance();

        emailET = findViewById(R.id.emailEditText);
        passwordET = findViewById(R.id.passwordEditText);

    }

    public void toMainPage() {
        Intent intent = new Intent(LoginActivity.this, JobsActivity.class);
        startActivity(intent);
    }

    public void onSignupClick(View view) {
        Intent intent = new Intent(this, SignupActivity.class);
        intent.putExtra("TOKEN", TOKEN);

        String email = Objects.requireNonNull(this.emailET.getText()).toString();
        if (!email.isEmpty()) {
            intent.putExtra("USER.EMAIL", email);
        }

        startActivity(intent);
    }

    public void login(View view) {
        String email = Objects.requireNonNull(this.emailET.getText()).toString();
        String password = Objects.requireNonNull(this.passwordET.getText()).toString();
        
        if(email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Fill in every field", Toast.LENGTH_SHORT).show();
        } else {
            Log.i(TAG, "email: " + email + '\n' + "password: " + password);

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "onComplete: User logged in successfully");
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        toMainPage();
                        finish();
                    } else {
                        Log.d(TAG, "onComplete: Failed to login user");
                        emailET.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.shake));
                        passwordET.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.shake));
                        Toast.makeText(LoginActivity.this, "Login failed: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    }
}