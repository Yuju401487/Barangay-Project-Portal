package com.kaibacorp.barangayprojectportal;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RateProjectActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_project);

        TextView tvProjectTitle =
                findViewById(R.id.tvProjectTitle);

        RatingBar ratingBar =
                findViewById(R.id.ratingBar);

        EditText etFeedback =
                findViewById(R.id.etFeedback);

        Button btnSubmitRating =
                findViewById(R.id.btnSubmitRating);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        String projectId =
                getIntent().getStringExtra("PROJECT_ID");

        String projectTitle =
                getIntent().getStringExtra("PROJECT_TITLE");

        if (projectTitle != null) {
            tvProjectTitle.setText(projectTitle);
        }

        btnSubmitRating.setOnClickListener(v -> {

            float rating =
                    ratingBar.getRating();

            String feedback =
                    etFeedback.getText()
                            .toString()
                            .trim();

            if (rating == 0) {

                Toast.makeText(
                        RateProjectActivity.this,
                        "Please select a rating.",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            if (feedback.isEmpty()) {

                Toast.makeText(
                        RateProjectActivity.this,
                        "Please enter feedback.",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            if (auth.getCurrentUser() == null) {

                Toast.makeText(
                        RateProjectActivity.this,
                        "User not logged in.",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            String residentId =
                    auth.getCurrentUser().getUid();

            db.collection("project_ratings")
                    .whereEqualTo(
                            "projectId",
                            projectId
                    )
                    .whereEqualTo(
                            "residentId",
                            residentId
                    )
                    .get()
                    .addOnSuccessListener(querySnapshot -> {

                        if (!querySnapshot.isEmpty()) {

                            Toast.makeText(
                                    RateProjectActivity.this,
                                    "You have already rated this project.",
                                    Toast.LENGTH_LONG
                            ).show();

                            return;
                        }

                        Map<String, Object> data =
                                new HashMap<>();

                        data.put(
                                "projectId",
                                projectId
                        );

                        data.put(
                                "projectTitle",
                                projectTitle
                        );

                        data.put(
                                "residentId",
                                residentId
                        );

                        data.put(
                                "rating",
                                rating
                        );

                        data.put(
                                "feedback",
                                feedback
                        );

                        data.put(
                                "timestamp",
                                Timestamp.now()
                        );

                        db.collection("project_ratings")
                                .add(data)
                                .addOnSuccessListener(documentReference -> {

                                    Toast.makeText(
                                            RateProjectActivity.this,
                                            "Rating submitted successfully.",
                                            Toast.LENGTH_SHORT
                                    ).show();

                                    finish();

                                })
                                .addOnFailureListener(e -> {

                                    Toast.makeText(
                                            RateProjectActivity.this,
                                            "Failed to submit rating.",
                                            Toast.LENGTH_SHORT
                                    ).show();

                                });

                    })
                    .addOnFailureListener(e -> {

                        Toast.makeText(
                                RateProjectActivity.this,
                                e.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();

                    });

        });
    }
}