package com.rmit.multiverseshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import static com.rmit.multiverseshop.helpers.Validator.isValidEmail;

public class LoginActivity extends AppCompatActivity {
    TextView signUpText;
    EditText emailText;
    EditText passwordText;
    Button loginButton;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        signUpText = findViewById(R.id.signup_text);
        emailText = findViewById(R.id.email_text);
        passwordText = findViewById(R.id.password_text);
        loginButton = findViewById(R.id.signin_button);

        signUpText.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });

        loginButton.setOnClickListener(view -> signIn());
    }

    private void signIn() {
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if (email.isEmpty()) {
            emailText.setError("Enter your email");
            return;
        } else if (!isValidEmail(email)) {
            emailText.setError("Invalid email address");
            return;
        } else if (password.isEmpty()) {
            passwordText.setError("Enter your password");
            return;
        }

        emailText.setError(null);
        passwordText.setError(null);

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Signed in successfully",
                            Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                } else {
                    Toast.makeText(LoginActivity.this, "Sign in failed",
                            Toast.LENGTH_SHORT).show();
                }
            });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
