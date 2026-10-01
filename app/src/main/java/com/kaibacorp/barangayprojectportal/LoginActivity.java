package com.kaibacorp.barangayprojectportal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnLogin;
    Button btnRegister;

    FirebaseAuth auth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        btnLogin.setOnClickListener(v -> {

            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                        LoginActivity.this,
                        "Fill all fields",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener(authResult -> {

                        String uid =
                                auth.getCurrentUser().getUid();

                        db.collection("users")
                                .document(uid)
                                .get()
                                .addOnSuccessListener(documentSnapshot -> {

                                    if (!documentSnapshot.exists()) {

                                        Toast.makeText(
                                                LoginActivity.this,
                                                "User profile not found",
                                                Toast.LENGTH_LONG
                                        ).show();

                                        return;
                                    }

                                    String role =
                                            documentSnapshot.getString("role");

                                    if (role == null) {

                                        Toast.makeText(
                                                LoginActivity.this,
                                                "Role not set",
                                                Toast.LENGTH_LONG
                                        ).show();

                                        return;
                                    }

                                    getSharedPreferences(
                                            "USER_DATA",
                                            MODE_PRIVATE
                                    )
                                            .edit()
                                            .putString(
                                                    "ROLE",
                                                    role
                                            )
                                            .apply();

                                    if (role.equals("admin")) {

                                        startActivity(
                                                new Intent(
                                                        LoginActivity.this,
                                                        AdminDashboardActivity.class
                                                )
                                        );

                                    } else {

                                        startActivity(
                                                new Intent(
                                                        LoginActivity.this,
                                                        ResidentDashboardActivity.class
                                                )
                                        );

                                    }

                                    finish();

                                })
                                .addOnFailureListener(e ->
                                        Toast.makeText(
                                                LoginActivity.this,
                                                e.getMessage(),
                                                Toast.LENGTH_LONG
                                        ).show());

                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(
                                    LoginActivity.this,
                                    e.getMessage(),
                                    Toast.LENGTH_LONG
                            ).show());

        });

        btnRegister.setOnClickListener(v -> {

            startActivity(
                    new Intent(
                            LoginActivity.this,
                            RegisterActivity.class
                    )
            );

        });
    }
}