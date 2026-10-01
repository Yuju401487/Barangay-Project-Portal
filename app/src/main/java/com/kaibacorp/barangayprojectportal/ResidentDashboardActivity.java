package com.kaibacorp.barangayprojectportal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ResidentDashboardActivity extends AppCompatActivity {

    Button btnVotePoll;
    Button btnPollResults;
    private Button btnViewProjects;
    private Button btnViewAnnouncements;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resident_dashboard);

        btnViewProjects =
                findViewById(R.id.btnViewProjects);

        btnViewAnnouncements =
                findViewById(R.id.btnViewAnnouncements);

        btnLogout =
                findViewById(R.id.btnLogout);

        btnVotePoll =
                findViewById(R.id.btnVotePoll);

        btnPollResults =
                findViewById(R.id.btnPollResults);

        btnViewProjects.setOnClickListener(v -> {

            startActivity(
                    new Intent(
                            ResidentDashboardActivity.this,
                            ResidentViewProjectsActivity.class
                    )
            );

        });

        btnViewAnnouncements.setOnClickListener(v -> {

            startActivity(
                    new Intent(
                            ResidentDashboardActivity.this,
                            ResidentViewAnnouncementsActivity.class
                    )
            );

        });

        btnVotePoll.setOnClickListener(v -> {

            startActivity(
                    new Intent(
                            ResidentDashboardActivity.this,
                            ResidentViewPollsActivity.class
                    )
            );

        });

        btnPollResults.setOnClickListener(v -> {

            startActivity(
                    new Intent(
                            ResidentDashboardActivity.this,
                            ViewPollsActivity.class
                    )
            );

        });

        btnLogout.setOnClickListener(v -> {

            FirebaseAuth.getInstance().signOut();

            getSharedPreferences(
                    "USER_DATA",
                    MODE_PRIVATE
            )
                    .edit()
                    .clear()
                    .apply();

            startActivity(
                    new Intent(
                            ResidentDashboardActivity.this,
                            LoginActivity.class
                    )
            );

            finish();

        });
    }
}