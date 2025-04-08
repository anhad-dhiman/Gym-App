    package com.example.gymbuddy;

    import android.content.Intent;
    import android.os.Bundle;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.Toast;
    import androidx.appcompat.app.AppCompatActivity;

    import com.google.firebase.auth.FirebaseAuth;

    public class LoginActivity extends AppCompatActivity {

        private EditText emailEditText, passwordEditText;
        private Button loginButton, goToSignupButton;
        private FirebaseAuth mAuth;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);

            mAuth = FirebaseAuth.getInstance();

            emailEditText = findViewById(R.id.emailEditText);
            passwordEditText = findViewById(R.id.passwordEditText);
            loginButton = findViewById(R.id.loginButton);
            goToSignupButton = findViewById(R.id.goToSignupButton);

            loginButton.setOnClickListener(v -> {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this, "Email and Password can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            });

            goToSignupButton.setOnClickListener(v -> {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                finish();
            });
        }
    }
