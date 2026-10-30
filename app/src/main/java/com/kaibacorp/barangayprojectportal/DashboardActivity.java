package com.kaibacorp.barangayprojectportal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class DashboardActivity extends AppCompatActivity {

    Button btnLogout;
    Button btnNotifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        btnLogout = findViewById(R.id.btnLogout);
        btnNotifications = findViewById(R.id.btnNotifications);

        btnNotifications.setOnClickListener(v -> {

            startActivity(
                    new Intent(
                            DashboardActivity.this,
                            NotificationsActivity.class
                    )
            );

        });

        btnLogout.setOnClickListener(v -> {

            FirebaseAuth.getInstance().signOut();

            startActivity(
                    new Intent(
                            DashboardActivity.this,
                            LoginActivity.class
                    )
            );

            finish();

        });
    }
}