package com.kaibacorp.barangayprojectportal;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText etResetEmail;
    Button btnSendReset;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        etResetEmail = findViewById(R.id.etResetEmail);
        btnSendReset = findViewById(R.id.btnSendReset);

        auth = FirebaseAuth.getInstance();

        btnSendReset.setOnClickListener(v -> {

            String email = etResetEmail.getText()
                    .toString()
                    .trim();

            if (email.isEmpty()) {

                Toast.makeText(
                        ForgotPasswordActivity.this,
                        "Please enter your email",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            auth.sendPasswordResetEmail(email)
                    .addOnSuccessListener(unused -> {

                        Toast.makeText(
                                ForgotPasswordActivity.this,
                                "Password reset email sent",
                                Toast.LENGTH_LONG
                        ).show();

                        finish();

                    })
                    .addOnFailureListener(e -> {

                        Toast.makeText(
                                ForgotPasswordActivity.this,
                                e.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();

                    });
        });
    }
}