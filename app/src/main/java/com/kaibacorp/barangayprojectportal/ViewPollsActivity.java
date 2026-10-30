package com.kaibacorp.barangayprojectportal;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

                        Button btnDelete =
                                new Button(this);

                        btnDelete.setText(
                                "DELETE POLL"
                        );

                        btnDelete.setTextColor(
                                Color.WHITE
                        );

                        btnDelete.setBackgroundTintList(
                                ColorStateList.valueOf(
                                        Color.parseColor(
                                                "#EC407A"
                                        )
                                )
                        );

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

                        btnDelete.setOnClickListener(v -> {

                            new androidx.appcompat.app.AlertDialog.Builder(
                                    ViewPollsActivity.this
                            )
                                    .setTitle("Delete Poll")
                                    .setMessage(
                                            "Are you sure you want to delete this poll?"
                                    )
                                    .setPositiveButton(
                                            "Delete",
                                            (dialog, which) -> {

                                                db.collection("polls")
                                                        .document(
                                                                document.getId()
                                                        )
                                                        .delete()
                                                        .addOnSuccessListener(unused -> {

                                                            Toast.makeText(
                                                                    ViewPollsActivity.this,
                                                                    "Poll Deleted",
                                                                    Toast.LENGTH_SHORT
                                                            ).show();

                                                            loadPolls();

                                                        });

                                            }
                                    )
                                    .setNegativeButton(
                                            "Cancel",
                                            null
                                    )
                                    .show();

                        });

                        String role =
                                getSharedPreferences(
                                        "USER_DATA",
                                        MODE_PRIVATE
                                ).getString(
                                        "ROLE",
                                        ""
                                );

                        card.addView(tv);
                        card.addView(btnResults);

                        if ("admin".equalsIgnoreCase(role)) {

                            card.addView(btnDelete);

                        }

                        pollContainer.addView(card);

                    });

                });

    }
}