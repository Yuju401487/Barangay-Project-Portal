package com.kaibacorp.barangayprojectportal;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.widget.ImageView;
import com.bumptech.glide.Glide;


import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class ResidentViewProjectsActivity extends AppCompatActivity {

    private LinearLayout projectContainer;
    private EditText etSearch;
    private Button btnAll;
    private Button btnOngoing;
    private Button btnArchived;

    private FirebaseFirestore db;

    private String currentFilter = "ALL";
    private String currentSearch = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resident_view_projects);

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

                    querySnapshot.forEach(document -> {

                        String title =
                                document.getString("title");

                        String description =
                                document.getString("description");

                        String budget =
                                document.getString("budget");

                        String status =
                                document.getString("status");

                        String imageUrl =
                                document.getString("imageUrl");

                        String raoRefCode =
                                document.getString("raoRefCode");

                        if (raoRefCode == null) {
                            raoRefCode = "Not Available";
                        }

                        if (title == null)
                            return;

                        if (!title.toLowerCase()
                                .contains(currentSearch))
                            return;

                        if (!currentFilter.equals("ALL")
                                && !currentFilter.equalsIgnoreCase(status))
                            return;

                        LinearLayout card =
                                new LinearLayout(this);

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
                                Color.parseColor(
                                        "#FCE4EC"
                                )
                        );

                        ImageView projectImage =
                                new ImageView(this);

                        projectImage.setLayoutParams(
                                new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        500
                                )
                        );

                        projectImage.setScaleType(
                                ImageView.ScaleType.CENTER_CROP
                        );

                        if (imageUrl != null
                                && !imageUrl.isEmpty()) {

                            Glide.with(this)
                                    .load(imageUrl)
                                    .into(projectImage);
                        }

                        card.setElevation(12f);

                        TextView tv =
                                new TextView(this);

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
                                Color.parseColor(
                                        "#4A4A4A"
                                )
                        );

                        Button btnDetails =
                                new Button(this);

                        btnDetails.setText(
                                "VIEW DETAILS"
                        );

                        Button btnViewTimeline =
                                new Button(this);

                        btnViewTimeline.setText(
                                "VIEW TIMELINE"
                        );

                        btnViewTimeline.setTextColor(
                                Color.WHITE
                        );

                        btnViewTimeline.setBackgroundTintList(
                                ColorStateList.valueOf(
                                        Color.parseColor("#EC407A")
                                )
                        );

                        Button btnFundSummary =
                                new Button(this);

                        btnFundSummary.setText(
                                "FUND SUMMARY"
                        );

                        btnFundSummary.setTextColor(
                                Color.WHITE
                        );

                        btnFundSummary.setBackgroundTintList(
                                ColorStateList.valueOf(
                                        Color.parseColor("#EC407A")
                                )
                        );

                        Button btnAddComment =
                                new Button(this);

                        btnAddComment.setText(
                                "ADD COMMENT"
                        );

                        btnAddComment.setTextColor(
                                Color.WHITE
                        );

                        btnAddComment.setBackgroundTintList(
                                ColorStateList.valueOf(
                                        Color.parseColor("#EC407A")
                                )
                        );

                        Button btnViewComments =
                                new Button(this);

                        btnViewComments.setText(
                                "VIEW COMMENTS"
                        );

                        btnViewComments.setTextColor(
                                Color.WHITE
                        );

                        btnViewComments.setBackgroundTintList(
                                ColorStateList.valueOf(
                                        Color.parseColor("#EC407A")
                                )
                        );


                        btnViewTimeline.setText(
                                "VIEW TIMELINE"
                        );

                        btnViewTimeline.setTextColor(
                                Color.WHITE
                        );

                        btnViewTimeline.setBackgroundTintList(
                                ColorStateList.valueOf(
                                        Color.parseColor("#EC407A")
                                )
                        );

                        btnDetails.setTextColor(
                                Color.WHITE
                        );

                        btnDetails.setBackgroundTintList(
                                ColorStateList.valueOf(
                                        Color.parseColor(
                                                "#EC407A"
                                        )
                                )
                        );

                        btnDetails.setOnClickListener(v -> {

                            Intent intent =
                                    new Intent(
                                            ResidentViewProjectsActivity.this,
                                            ProjectDetailsActivity.class
                                    );

                            intent.putExtra(
                                    "PROJECT_ID",
                                    document.getId()
                            );

                            startActivity(intent);

                        });

                        btnViewTimeline.setOnClickListener(v -> {

                            Intent intent =
                                    new Intent(
                                            ResidentViewProjectsActivity.this,
                                            ViewProjectUpdatesActivity.class
                                    );

                            intent.putExtra(
                                    "PROJECT_ID",
                                    document.getId()
                            );

                            startActivity(intent);

                        });

                        btnFundSummary.setOnClickListener(v -> {

                            Intent intent =
                                    new Intent(
                                            ResidentViewProjectsActivity.this,
                                            ViewBudgetSummaryActivity.class
                                    );

                            intent.putExtra(
                                    "PROJECT_ID",
                                    document.getId()
                            );

                            startActivity(intent);

                        });

                        btnAddComment.setOnClickListener(v -> {

                            Intent intent =
                                    new Intent(
                                            ResidentViewProjectsActivity.this,
                                            AddCommentActivity.class
                                    );

                            intent.putExtra(
                                    "PROJECT_ID",
                                    document.getId()
                            );

                            startActivity(intent);

                        });

                        btnViewComments.setOnClickListener(v -> {

                            Intent intent =
                                    new Intent(
                                            ResidentViewProjectsActivity.this,
                                            ViewCommentsActivity.class
                                    );

                            intent.putExtra(
                                    "PROJECT_ID",
                                    document.getId()
                            );

                            startActivity(intent);

                        });


                        btnViewTimeline.setOnClickListener(v -> {

                            Intent intent =
                                    new Intent(
                                            ResidentViewProjectsActivity.this,
                                            ViewProjectUpdatesActivity.class
                                    );

                            intent.putExtra(
                                    "PROJECT_ID",
                                    document.getId()
                            );

                            startActivity(intent);

                        });


                        card.addView(projectImage);
                        card.addView(tv);
                        card.addView(btnDetails);
                        card.addView(btnViewTimeline);
                        card.addView(btnFundSummary);
                        card.addView(btnAddComment);
                        card.addView(btnViewComments);

                        projectContainer.addView(card);

                    });

                });
    }
}