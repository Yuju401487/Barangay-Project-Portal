package com.kaibacorp.barangayprojectportal;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class ProjectDetailsActivity extends AppCompatActivity {

    TextView tvTitle;
    TextView tvDescription;
    TextView tvBudget;
    TextView tvStatus;
    TextView tvProjectId;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details);

        tvTitle = findViewById(R.id.tvTitle);
        tvDescription = findViewById(R.id.tvDescription);
        tvBudget = findViewById(R.id.tvBudget);
        tvStatus = findViewById(R.id.tvStatus);
        tvProjectId = findViewById(R.id.tvProjectId);

        db = FirebaseFirestore.getInstance();

        String projectId =
                getIntent().getStringExtra("PROJECT_ID");

        db.collection("projects")
                .document(projectId)
                .get()
                .addOnSuccessListener(document -> {

                    tvTitle.setText(
                            document.getString("title"));

                    tvDescription.setText(
                            "Description: "
                                    + document.getString("description"));

                    tvBudget.setText(
                            "Budget: ₱"
                                    + document.getString("budget"));

                    tvStatus.setText(
                            "Status: "
                                    + document.getString("status"));

                    tvProjectId.setText(
                            "Project ID: "
                                    + document.getId());

                });

    }
}
