package com.kaibacorp.barangayprojectportal;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class ViewPollResultsActivity extends AppCompatActivity {

    private LinearLayout resultsContainer;
    private FirebaseFirestore db;
    private String pollId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_poll_results);

        resultsContainer =
                findViewById(R.id.resultsContainer);

        db = FirebaseFirestore.getInstance();

        pollId =
                getIntent().getStringExtra("POLL_ID");

        loadResults();
    }

    private void loadResults() {

        db.collection("polls")
                .document(pollId)
                .get()
                .addOnSuccessListener(document -> {

                    String question =
                            document.getString("question");

                    String option1 =
                            document.getString("option1");

                    String option2 =
                            document.getString("option2");

                    String option3 =
                            document.getString("option3");

                    Long votes1 =
                            document.getLong("votes1");

                    Long votes2 =
                            document.getLong("votes2");

                    Long votes3 =
                            document.getLong("votes3");

                    if (votes1 == null) votes1 = 0L;
                    if (votes2 == null) votes2 = 0L;
                    if (votes3 == null) votes3 = 0L;

                    resultsContainer.removeAllViews();

                    TextView tvQuestion =
                            new TextView(this);

                    tvQuestion.setText(question);

                    tvQuestion.setTextSize(24);

                    tvQuestion.setTextColor(
                            Color.parseColor("#EC407A")
                    );

                    tvQuestion.setPadding(
                            20,
                            20,
                            20,
                            40
                    );

                    resultsContainer.addView(tvQuestion);

                    addResultCard(
                            option1,
                            votes1
                    );

                    addResultCard(
                            option2,
                            votes2
                    );

                    addResultCard(
                            option3,
                            votes3
                    );

                });

    }

    private void addResultCard(
            String option,
            Long votes
    ) {

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

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        params.setMargins(
                0,
                0,
                0,
                30
        );

        card.setLayoutParams(params);

        card.setBackgroundColor(
                Color.parseColor("#FCE4EC")
        );

        card.setElevation(12f);

        TextView tv =
                new TextView(this);

        tv.setText(
                option +
                        "\n\nVotes: " +
                        votes
        );

        tv.setTextSize(20);

        tv.setTextColor(
                Color.BLACK
        );

        card.addView(tv);

        resultsContainer.addView(card);

    }
}