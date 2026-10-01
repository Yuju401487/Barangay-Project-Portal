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
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class AdminViewProjectsActivity extends AppCompatActivity {

    private LinearLayout projectContainer;
    private EditText etSearch;
    private Button btnAll, btnOngoing, btnArchived;

    private FirebaseFirestore db;

    private String currentFilter = "ALL";
    private String currentSearch = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_projects);

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

    private void loadProjects() {

        projectContainer.removeAllViews();

        db.collection("projects")
                .get()
                .addOnSuccessListener(querySnapshot -> {

                    projectContainer.removeAllViews();

                    for (QueryDocumentSnapshot document : querySnapshot) {

                        String documentId = document.getId();

                        String title = document.getString("title");
                        String description = document.getString("description");
                        String budget = document.getString("budget");
                        String status = document.getString("status");

                        String raoRefCode =
                                document.getString("raoRefCode");

                        if (raoRefCode == null) {
                            raoRefCode = "Not Available";
                        }

                        if (title == null)
                            continue;

                        if (!title.toLowerCase().contains(currentSearch))
                            continue;

                        if (!currentFilter.equals("ALL")
                                && !currentFilter.equalsIgnoreCase(status))
                            continue;

                        LinearLayout card = new LinearLayout(this);

                        card.setOrientation(
                                LinearLayout.VERTICAL
                        );

                        card.setPadding(
                                40,
                                40,
                                40,
                                40
                        );

                        LinearLayout.LayoutParams cardParams =
                                new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                );

                        cardParams.setMargins(
                                0,
                                0,
                                0,
                                30
                        );

                        card.setLayoutParams(cardParams);

                        card.setBackgroundColor(
                                android.graphics.Color.parseColor(
                                        "#FCE4EC"
                                )
                        );

                        TextView tv = new TextView(this);

                        tv.setText(
                                title +

                                        "\n\nDescription: "
                                        + description +

                                        "\n\nBudget: ₱"
                                        + budget +

                                        "\n\nRAO Ref. Code: "
                                        + raoRefCode +

                                        "\n\nStatus: "
                                        + status
                        );

                        tv.setTextSize(18);

                        tv.setTextColor(
                                android.graphics.Color.parseColor(
                                        "#4A4A4A"
                                )
                        );


                        Button btnDetails = new Button(this);
                        btnDetails.setText("VIEW DETAILS");

                        Button btnUpdate = new Button(this);
                        btnUpdate.setText("UPDATE");

                        Button btnArchive = new Button(this);
                        btnArchive.setText("ARCHIVE");

                        btnDetails.setTextColor(
                                android.graphics.Color.WHITE
                        );

                        btnUpdate.setTextColor(
                                android.graphics.Color.WHITE
                        );

                        btnArchive.setTextColor(
                                android.graphics.Color.WHITE
                        );


                        Button btnAddUpdate = new Button(this);
                        btnAddUpdate.setText("ADD UPDATE");

                        Button btnViewTimeline = new Button(this);
                        btnViewTimeline.setText("VIEW TIMELINE");

                        Button btnAddExpense = new Button(this);
                        btnAddExpense.setText("ADD EXPENSE");

                        Button btnFundSummary = new Button(this);
                        btnFundSummary.setText("FUND SUMMARY");

                        Button btnViewComments = new Button(this);
                        btnViewComments.setText("VIEW COMMENTS");

                        btnDetails.setBackgroundTintList(
                                android.content.res.ColorStateList.valueOf(
                                        android.graphics.Color.parseColor("#EC407A")
                                )
                        );

                        btnUpdate.setBackgroundTintList(
                                android.content.res.ColorStateList.valueOf(
                                        android.graphics.Color.parseColor("#EC407A")
                                )
                        );

                        btnArchive.setBackgroundTintList(
                                android.content.res.ColorStateList.valueOf(
                                        android.graphics.Color.parseColor("#EC407A")
                                )
                        );

                        btnAddUpdate.setBackgroundTintList(
                                android.content.res.ColorStateList.valueOf(
                                        android.graphics.Color.parseColor("#EC407A")
                                )
                        );

                        btnViewTimeline.setBackgroundTintList(
                                android.content.res.ColorStateList.valueOf(
                                        android.graphics.Color.parseColor("#EC407A")
                                )
                        );

                        btnAddExpense.setBackgroundTintList(
                                android.content.res.ColorStateList.valueOf(
                                        android.graphics.Color.parseColor("#EC407A")
                                )
                        );

                        btnFundSummary.setBackgroundTintList(
                                android.content.res.ColorStateList.valueOf(
                                        android.graphics.Color.parseColor("#EC407A")
                                )
                        );

                        btnViewComments.setBackgroundTintList(
                                android.content.res.ColorStateList.valueOf(
                                        android.graphics.Color.parseColor("#EC407A")
                                )
                        );

                        btnAddUpdate.setTextColor(
                                android.graphics.Color.WHITE
                        );

                        btnViewTimeline.setTextColor(
                                android.graphics.Color.WHITE
                        );

                        btnAddExpense.setTextColor(
                                android.graphics.Color.WHITE
                        );

                        btnFundSummary.setTextColor(
                                android.graphics.Color.WHITE
                        );

                        btnViewComments.setTextColor(
                                android.graphics.Color.WHITE
                        );

                        btnDetails.setOnClickListener(v -> {

                            Intent intent =
                                    new Intent(
                                            AdminViewProjectsActivity.this,
                                            ProjectDetailsActivity.class
                                    );

                            intent.putExtra(
                                    "PROJECT_ID",
                                    documentId
                            );

                            startActivity(intent);

                        });

                        btnUpdate.setOnClickListener(v -> {

                            Intent intent =
                                    new Intent(
                                            AdminViewProjectsActivity.this,
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
                                    .update("status", "Archived")
                                    .addOnSuccessListener(unused -> {

                                        Toast.makeText(
                                                this,
                                                "Project Archived",
                                                Toast.LENGTH_SHORT
                                        ).show();

                                        loadProjects();

                                    });

                        });

                        btnAddUpdate.setOnClickListener(v -> {

                            Intent intent =
                                    new Intent(
                                            AdminViewProjectsActivity.this,
                                            AddProjectUpdateActivity.class
                                    );

                            intent.putExtra(
                                    "PROJECT_ID",
                                    documentId
                            );

                            startActivity(intent);

                        });

                        btnViewTimeline.setOnClickListener(v -> {

                            Intent intent =
                                    new Intent(
                                            AdminViewProjectsActivity.this,
                                            ViewProjectUpdatesActivity.class
                                    );

                            intent.putExtra(
                                    "PROJECT_ID",
                                    documentId
                            );

                            startActivity(intent);

                        });

                        btnAddExpense.setOnClickListener(v -> {

                            Intent intent =
                                    new Intent(
                                            AdminViewProjectsActivity.this,
                                            AddProjectExpenseActivity.class
                                    );

                            intent.putExtra(
                                    "PROJECT_ID",
                                    documentId
                            );

                            startActivity(intent);

                        });

                        btnFundSummary.setOnClickListener(v -> {

                            Intent intent =
                                    new Intent(
                                            AdminViewProjectsActivity.this,
                                            ViewBudgetSummaryActivity.class
                                    );

                            intent.putExtra(
                                    "PROJECT_ID",
                                    documentId
                            );

                            startActivity(intent);

                        });

                        btnViewComments.setOnClickListener(v -> {

                            Intent intent =
                                    new Intent(
                                            AdminViewProjectsActivity.this,
                                            ViewCommentsActivity.class
                                    );

                            intent.putExtra(
                                    "PROJECT_ID",
                                    documentId
                            );

                            startActivity(intent);

                        });

                        card.addView(tv);
                        card.addView(btnDetails);
                        card.addView(btnUpdate);
                        card.addView(btnArchive);
                        card.addView(btnAddUpdate);
                        card.addView(btnViewTimeline);
                        card.addView(btnAddExpense);
                        card.addView(btnFundSummary);
                        card.addView(btnViewComments);

                        projectContainer.addView(card);
                    }

                });
    }
}

