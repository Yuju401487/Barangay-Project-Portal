package com.kaibacorp.barangayprojectportal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class AdminDashboardActivity extends AppCompatActivity {

    Button btnCreatePoll;
    Button btnViewPolls;
    Button btnAddProject;
    Button btnViewProjects;
    Button btnAddAnnouncement;
    Button btnViewAnnouncements;
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        btnAddProject =
                findViewById(R.id.btnAddProject);

        btnViewProjects =
                findViewById(R.id.btnViewProjects);

        btnAddAnnouncement =
                findViewById(R.id.btnAddAnnouncement);

        btnViewAnnouncements =
                findViewById(R.id.btnViewAnnouncements);

        btnLogout =
                findViewById(R.id.btnLogout);

        btnCreatePoll =
                findViewById(R.id.btnCreatePoll);

        btnViewPolls =
                findViewById(R.id.btnViewPolls);

        btnAddProject.setOnClickListener(v -> {

            startActivity(
                    new Intent(
                            AdminDashboardActivity.this,
                            AddProjectActivity.class
                    )
            );

        });

        btnViewProjects.setOnClickListener(v -> {

            startActivity(
                    new Intent(
                            AdminDashboardActivity.this,
                            AdminViewProjectsActivity.class
                    )
            );

        });

        btnAddAnnouncement.setOnClickListener(v -> {

            startActivity(
                    new Intent(
                            AdminDashboardActivity.this,
                            AddAnnouncementActivity.class
                    )
            );

        });

        btnViewAnnouncements.setOnClickListener(v -> {

            startActivity(
                    new Intent(
                            AdminDashboardActivity.this,
                            ViewAnnouncementsActivity.class
                    )
            );

        });

        btnCreatePoll.setOnClickListener(v -> {

            startActivity(
                    new Intent(
                            AdminDashboardActivity.this,
                            CreatePollActivity.class
                    )
            );

        });

        btnViewPolls.setOnClickListener(v -> {

            startActivity(
                    new Intent(
                            AdminDashboardActivity.this,
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
                            AdminDashboardActivity.this,
                            LoginActivity.class
                    )
            );

            finish();

        });

    }
}