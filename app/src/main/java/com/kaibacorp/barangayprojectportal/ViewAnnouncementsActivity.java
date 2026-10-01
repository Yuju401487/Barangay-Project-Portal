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

public class ViewAnnouncementsActivity extends AppCompatActivity {

    private LinearLayout announcementContainer;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_announcements);

        announcementContainer =
                findViewById(R.id.announcementContainer);

        db = FirebaseFirestore.getInstance();

        loadAnnouncements();
    }

    private void loadAnnouncements() {

        announcementContainer.removeAllViews();

        db.collection("announcements")
                .get()
                .addOnSuccessListener(querySnapshot -> {

                    announcementContainer.removeAllViews();

                    querySnapshot.forEach(document -> {

                        String title =
                                document.getString("title");

                        String content =
                                document.getString("content");

                        String date =
                                document.getString("date");

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
                                title +

                                        "\n\n" +
                                        content +

                                        "\n\nDate: "
                                        + date
                        );

                        tv.setTextColor(
                                Color.BLACK
                        );

                        tv.setTextSize(18);

                        Button btnDetails =
                                new Button(this);

                        btnDetails.setText(
                                "VIEW DETAILS"
                        );

                        Button btnUpdate =
                                new Button(this);

                        btnUpdate.setText(
                                "UPDATE"
                        );

                        Button btnDelete =
                                new Button(this);

                        btnDelete.setText(
                                "DELETE"
                        );

                        btnDetails.setTextColor(
                                Color.WHITE
                        );

                        btnUpdate.setTextColor(
                                Color.WHITE
                        );

                        btnDelete.setTextColor(
                                Color.WHITE
                        );

                        btnDetails.setBackgroundTintList(
                                ColorStateList.valueOf(
                                        Color.parseColor(
                                                "#EC407A"
                                        )
                                )
                        );

                        btnUpdate.setBackgroundTintList(
                                ColorStateList.valueOf(
                                        Color.parseColor(
                                                "#EC407A"
                                        )
                                )
                        );

                        btnDelete.setBackgroundTintList(
                                ColorStateList.valueOf(
                                        Color.parseColor(
                                                "#EC407A"
                                        )
                                )
                        );

                        btnDetails.setOnClickListener(v -> {

                            Intent intent =
                                    new Intent(
                                            ViewAnnouncementsActivity.this,
                                            AnnouncementDetailsActivity.class
                                    );

                            intent.putExtra(
                                    "ANNOUNCEMENT_ID",
                                    document.getId()
                            );

                            startActivity(intent);

                        });

                        btnUpdate.setOnClickListener(v -> {

                            Intent intent =
                                    new Intent(
                                            ViewAnnouncementsActivity.this,
                                            UpdateAnnouncementActivity.class
                                    );

                            intent.putExtra(
                                    "ANNOUNCEMENT_ID",
                                    document.getId()
                            );

                            startActivity(intent);

                        });

                        btnDelete.setOnClickListener(v -> {

                            db.collection("announcements")
                                    .document(document.getId())
                                    .delete()
                                    .addOnSuccessListener(unused -> {

                                        Toast.makeText(
                                                ViewAnnouncementsActivity.this,
                                                "Announcement Deleted",
                                                Toast.LENGTH_SHORT
                                        ).show();

                                        loadAnnouncements();

                                    });

                        });

                        card.addView(tv);
                        card.addView(btnDetails);
                        card.addView(btnUpdate);
                        card.addView(btnDelete);

                        announcementContainer.addView(
                                card
                        );

                    });

                })
                .addOnFailureListener(e -> {

                    Toast.makeText(
                            this,
                            e.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();

                });
    }
}