package com.example.ctrl_cv;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private static final String TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJDdHJsK0NWIiwiaWF0IjoxNzQzOTY2ODU5LCJleHAiOjE3NzU1MDI4NTksImF1ZCI6Ind3dy5leGFtcGxlLmNvbSIsInN1YiI6IiIsIkdpdmVuTmFtZSI6IkpvaG5ueSIsIlN1cm5hbWUiOiJSb2NrZXQiLCJFbWFpbCI6Impyb2NrZXRAZXhhbXBsZS5jb20iLCJSb2xlIjpbIk1hbmFnZXIiLCJQcm9qZWN0IEFkbWluaXN0cmF0b3IiXX0.zVaOYr-rJFYI8vMKhec7ku6YpjIfwYzqc1-ePN3y5Qo";
    private static final String TAG = LoginActivity.class.getName();
    private TextInputEditText emailET;
    private TextInputEditText passwordET;


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

        emailET = findViewById(R.id.emailEditText);
        passwordET = findViewById(R.id.passwordEditText);

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

        Log.i(TAG, "email: " + email + '\n' + "password: " + password);

    }
}