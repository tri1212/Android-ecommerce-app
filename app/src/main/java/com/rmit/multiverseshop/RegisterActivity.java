package com.rmit.multiverseshop;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;

import static com.rmit.multiverseshop.utils.Validator.isValidEmail;

public class RegisterActivity extends AppCompatActivity {

    TextView signInText;
    EditText emailText;
    EditText passwordText;
    EditText repeatPasswordText;

    Button signupButton;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();

        signInText = findViewById(R.id.signin_text);
        emailText = findViewById(R.id.email_text);
        passwordText = findViewById(R.id.password_text);
        repeatPasswordText = findViewById(R.id.repeat_password_text);
        signupButton = findViewById(R.id.signup_button);

        signInText.setOnClickListener(view -> finish());
        signupButton.setOnClickListener(view -> register());
    }

    private void register() {
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        String repeatPassword = repeatPasswordText.getText().toString();

        if (email.isEmpty()) {
            emailText.setError("Email cannot be empty");
            return;
        } else if (!isValidEmail(email)) {
            emailText.setError("Invalid email address");
            return;
        } else if (password.isEmpty()) {
            passwordText.setError("Password cannot be empty");
            return;
        } else if (password.length() < 6) {
            passwordText.setError("Password must be at least 6 characters long");
            return;
        } else if (repeatPassword.isEmpty()) {
            repeatPasswordText.setError("Repeat your password");
            return;
        } else if (!password.equals(repeatPassword)) {
            repeatPasswordText.setError("Your passwords do not match");
            return;
        }

        emailText.setError(null);
        passwordText.setError(null);
        repeatPasswordText.setError(null);

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Registered successfully",
                            Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Exception e = task.getException();
                    Toast.makeText(RegisterActivity.this, 
                            e != null ? e.getMessage() : "Registration failed", 
                            Toast.LENGTH_SHORT).show();
                }
            });
    }
}
