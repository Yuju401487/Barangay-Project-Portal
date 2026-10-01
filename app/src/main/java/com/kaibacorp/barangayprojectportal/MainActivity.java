package com.kaibacorp.barangayprojectportal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDelegate;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO
        );

        SharedPreferences prefs =
                getSharedPreferences(
                        "USER_DATA",
                        MODE_PRIVATE
                );

        String role =
                prefs.getString(
                        "ROLE",
                        ""
                );

        if (FirebaseAuth.getInstance()
                .getCurrentUser() != null) {

            if (role.equals("admin")) {

                startActivity(
                        new Intent(
                                MainActivity.this,
                                AdminDashboardActivity.class
                        )
                );

            } else {

                startActivity(
                        new Intent(
                                MainActivity.this,
                                ResidentDashboardActivity.class
                        )
                );

            }

        } else {

            startActivity(
                    new Intent(
                            MainActivity.this,
                            LoginActivity.class
                    )
            );

        }

        finish();

    }
}