package com.kaibacorp.barangayprojectportal;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText etFullName, etEmail, etPassword;
    Button btnRegister;

    FirebaseAuth auth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        btnRegister.setOnClickListener(v -> {

            String fullname = etFullName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if(fullname.isEmpty() || email.isEmpty() || password.isEmpty()) {

                Toast.makeText(
                        RegisterActivity.this,
                        "Fill all fields",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {

                        if(task.isSuccessful()) {

                            FirebaseUser firebaseUser =
                                    auth.getCurrentUser();

                            String uid =
                                    firebaseUser.getUid();

                            Map<String, Object> user =
                                    new HashMap<>();

                            user.put("fullname", fullname);
                            user.put("email", email);
                            user.put("role", "resident");

                            db.collection("users")
                                    .document(uid)
                                    .set(user)
                                    .addOnSuccessListener(unused ->

                                            Toast.makeText(
                                                    RegisterActivity.this,
                                                    "Registration Successful!",
                                                    Toast.LENGTH_LONG
                                            ).show()

                                    );

                        } else {

                            Toast.makeText(
                                    RegisterActivity.this,
                                    task.getException().getMessage(),
                                    Toast.LENGTH_LONG
                            ).show();

                        }

                    });

        });
    }
}