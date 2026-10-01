package com.kaibacorp.barangayprojectportal;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class ViewPollsActivity extends AppCompatActivity {

    private LinearLayout pollContainer;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_polls);

        pollContainer =
                findViewById(R.id.pollContainer);

        db = FirebaseFirestore.getInstance();

        loadPolls();
    }

    private void loadPolls() {

        pollContainer.removeAllViews();

        db.collection("polls")
                .get()
                .addOnSuccessListener(querySnapshot -> {

                    querySnapshot.forEach(document -> {

                        String question =
                                document.getString("question");

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

                        TextView tv =
                                new TextView(this);

                        tv.setText(question);

                        tv.setTextSize(20);

                        tv.setTextColor(
                                Color.BLACK
                        );


                        Button btnResults =
                                new Button(this);

                        btnResults.setText(
                                "VIEW RESULTS"
                        );

                        btnResults.setTextColor(
                                Color.WHITE
                        );

                        btnResults.setBackgroundTintList(
                                ColorStateList.valueOf(
                                        Color.parseColor(
                                                "#EC407A"
                                        )
                                )
                        );

                        btnResults.setOnClickListener(v -> {

                            Intent intent =
                                    new Intent(
                                            ViewPollsActivity.this,
                                            ViewPollResultsActivity.class
                                    );

                            intent.putExtra(
                                    "POLL_ID",
                                    document.getId()
                            );

                            startActivity(intent);

                        });

                        card.addView(tv);
                        card.addView(btnResults);

                        pollContainer.addView(card);

                    });

                });

    }
}