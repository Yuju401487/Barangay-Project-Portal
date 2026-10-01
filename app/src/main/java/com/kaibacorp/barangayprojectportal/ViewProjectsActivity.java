package com.kaibacorp.barangayprojectportal;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class ViewProjectsActivity extends AppCompatActivity {

    LinearLayout projectContainer;

    EditText etSearch;

    Button btnAll;
    Button btnOngoing;
    Button btnArchived;

    FirebaseFirestore db;

    String currentFilter = "ALL";
    String currentSearch = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_projects);

        projectContainer = findViewById(R.id.projectContainer);

        etSearch = findViewById(R.id.etSearch);

        btnAll = findViewById(R.id.btnAll);
        btnOngoing = findViewById(R.id.btnOngoing);
        btnArchived = findViewById(R.id.btnArchived);

        db = FirebaseFirestore.getInstance();

        loadProjects();

        btnAll.setOnClickListener(v -> {

            currentFilter = "ALL";
            loadProjects();

        });

        btnOngoing.setOnClickListener(v -> {

            currentFilter = "Ongoing";
            loadProjects();

        });

        btnArchived.setOnClickListener(v -> {

            currentFilter = "Archived";
            loadProjects();

        });

        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(
                    CharSequence s,
                    int start,
                    int count,
                    int after) {

            }

            @Override
            public void onTextChanged(
                    CharSequence s,
                    int start,
                    int before,
                    int count) {

                currentSearch =
                        s.toString().toLowerCase();

                loadProjects();

            }

            @Override
            public void afterTextChanged(
                    Editable s) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProjects();
    }

    private void loadProjects() {

        projectContainer.removeAllViews();

        db.collection("projects")
                .get()
                .addOnSuccessListener(querySnapshot -> {

                    querySnapshot.forEach(document -> {

                        String documentId =
                                document.getId();

                        String title =
                                document.getString("title");

                        String description =
                                document.getString("description");

                        String budget =
                                document.getString("budget");

                        String status =
                                document.getString("status");

                        if(title == null)
                            return;

                        if(!title.toLowerCase()
                                .contains(currentSearch))
                            return;

                        if(!currentFilter.equals("ALL")
                                && !currentFilter.equals(status))
                            return;

                        TextView tv =
                                new TextView(this);

                        tv.setText(

                                "PROJECT TITLE\n"
                                        + title +

                                        "\n\nDESCRIPTION\n"
                                        + description +

                                        "\n\nBUDGET\n₱"
                                        + budget +

                                        "\n\nSTATUS\n"
                                        + status +

                                        "\n\n--------------------------------"

                        );

                        tv.setTextSize(18);

                        Button btnUpdate =
                                new Button(this);

                        btnUpdate.setText("UPDATE");

                        Button btnArchive =
                                new Button(this);

                        btnArchive.setText("ARCHIVE");

                        btnUpdate.setOnClickListener(v -> {

                            Intent intent =
                                    new Intent(
                                            ViewProjectsActivity.this,
                                            UpdateProjectActivity.class
                                    );

                            intent.putExtra(
                                    "PROJECT_ID",
                                    documentId
                            );

                            startActivity(intent);

                        });

                        btnArchive.setOnClickListener(v -> {

                            db.collection("projects")
                                    .document(documentId)
                                    .update(
                                            "status",
                                            "Archived"
                                    )
                                    .addOnSuccessListener(unused -> {

                                        Toast.makeText(
                                                this,
                                                "Project Archived",
                                                Toast.LENGTH_SHORT
                                        ).show();

                                        loadProjects();

                                    });

                        });

                        projectContainer.addView(tv);
                        projectContainer.addView(btnUpdate);
                        projectContainer.addView(btnArchive);

                    });

                });
    }
}