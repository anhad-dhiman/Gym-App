package com.example.gymbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignupActivity extends AppCompatActivity {

    private EditText usernameEditText, emailEditText, passwordEditText;
    private Button signupButton;
    private TextView loginTextView;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Find views
        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signupButton = findViewById(R.id.signupButton);
        loginTextView = findViewById(R.id.loginTextView);

        // Set up signup button click listener
        signupButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            // Validate inputs
            if (username.isEmpty()) {
                usernameEditText.setError("Username is required");
                usernameEditText.requestFocus();
                return;
            }

            if (email.isEmpty()) {
                emailEditText.setError("Email is required");
                emailEditText.requestFocus();
                return;
            }

            if (password.isEmpty()) {
                passwordEditText.setError("Password is required");
                passwordEditText.requestFocus();
                return;
            }

            if (password.length() < 6) {
                passwordEditText.setError("Password must be at least 6 characters");
                passwordEditText.requestFocus();
                return;
            }

            // Show progress indicator (you might want to add a ProgressBar in your layout)
            // progressBar.setVisibility(View.VISIBLE);

            // Create the user in Firebase Auth
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        // Hide progress indicator
                        // progressBar.setVisibility(View.GONE);

                        if (task.isSuccessful()) {
                            // Save the username to user profile
                            saveUserProfile(username);

                            Toast.makeText(this, "Sign up successful!", Toast.LENGTH_SHORT).show();

                            // Redirect to main activity
                            startActivity(new Intent(SignupActivity.this, MainActivity.class));
                            finish();
                        } else {
                            // If sign up fails, display a message to the user
                            Toast.makeText(this, "Signup failed: " +
                                            (task.getException() != null ? task.getException().getMessage() : "Unknown error"),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        });

        // Set up login text view click listener
        loginTextView.setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            finish();
        });
    }

    /**
     * Save the username to the Firebase user profile
     * @param username The username to save
     */
    private void saveUserProfile(String username) {
        // Update the user profile with the username as display name
        if (mAuth.getCurrentUser() != null) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(username)
                    .build();

            mAuth.getCurrentUser().updateProfile(profileUpdates)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // You can add additional handling here if needed
                        }
                    });
        }
    }
}