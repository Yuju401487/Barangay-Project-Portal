package com.kaibacorp.barangayprojectportal;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class ViewRatingsActivity extends AppCompatActivity {

    private TextView tvProjectTitle;
    private TextView tvAverageRating;
    private LinearLayout ratingsContainer;

    private FirebaseFirestore db;

    private String projectId;
    private String projectTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ratings);

        tvProjectTitle = findViewById(R.id.tvProjectTitle);
        tvAverageRating = findViewById(R.id.tvAverageRating);
        ratingsContainer = findViewById(R.id.ratingsContainer);

        db = FirebaseFirestore.getInstance();

        projectId =
                getIntent().getStringExtra("PROJECT_ID");

        projectTitle =
                getIntent().getStringExtra("PROJECT_TITLE");

        if (projectTitle != null) {
            tvProjectTitle.setText(
                    projectTitle + " Ratings"
            );
        }

        loadRatings();
    }

    private void loadRatings() {

        ratingsContainer.removeAllViews();

        db.collection("project_ratings")
                .whereEqualTo(
                        "projectId",
                        projectId
                )
                .get()
                .addOnSuccessListener(querySnapshot -> {

                    float totalRating = 0;
                    int count = 0;

                    for (var document : querySnapshot.getDocuments()) {

                        Double ratingValue =
                                document.getDouble("rating");

                        String feedback =
                                document.getString("feedback");

                        if (ratingValue == null) {
                            ratingValue = 0.0;
                        }

                        totalRating += ratingValue;
                        count++;

                        LinearLayout card =
                                new LinearLayout(this);

                        card.setOrientation(
                                LinearLayout.VERTICAL
                        );

                        card.setPadding(
                                30,
                                30,
                                30,
                                30
                        );

                        LinearLayout.LayoutParams params =
                                new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                );

                        params.setMargins(
                                0,
                                0,
                                0,
                                25
                        );

                        card.setLayoutParams(params);

                        card.setBackgroundColor(
                                Color.parseColor(
                                        "#FCE4EC"
                                )
                        );

                        TextView tvRating =
                                new TextView(this);

                        tvRating.setText(
                                "⭐ Rating: "
                                        + ratingValue
                                        + "/5"
                        );

                        tvRating.setTextSize(18);

                        tvRating.setTextColor(
                                Color.parseColor(
                                        "#EC407A"
                                )
                        );

                        TextView tvFeedback =
                                new TextView(this);

                        tvFeedback.setText(
                                "\nFeedback:\n"
                                        + feedback
                        );

                        tvFeedback.setTextSize(16);

                        tvFeedback.setTextColor(
                                Color.parseColor(
                                        "#4A4A4A"
                                )
                        );

                        card.addView(tvRating);
                        card.addView(tvFeedback);

                        ratingsContainer.addView(card);
                    }

                    if (count > 0) {

                        float average =
                                totalRating / count;

                        tvAverageRating.setText(
                                "Average Rating: "
                                        + String.format(
                                        "%.1f",
                                        average
                                )
                                        + " / 5 (" + count + " ratings)"
                        );

                    } else {

                        tvAverageRating.setText(
                                "No ratings yet."
                        );
                    }
                });
    }
}