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

public class ResidentViewAnnouncementsActivity extends AppCompatActivity {

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

                                        "\n\nDate: " +
                                        date
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
                                            ResidentViewAnnouncementsActivity.this,
                                            AnnouncementDetailsActivity.class
                                    );

                            intent.putExtra(
                                    "ANNOUNCEMENT_ID",
                                    document.getId()
                            );

                            startActivity(intent);

                        });

                        card.addView(tv);
                        card.addView(btnDetails);

                        announcementContainer.addView(card);

                    });

                });
    }
}