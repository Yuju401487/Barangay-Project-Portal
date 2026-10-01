package com.kaibacorp.barangayprojectportal;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class AnnouncementDetailsActivity extends AppCompatActivity {

    private TextView tvTitle;
    private TextView tvContent;
    private TextView tvDate;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_announcement_details
        );

        tvTitle =
                findViewById(R.id.tvTitle);

        tvContent =
                findViewById(R.id.tvContent);

        tvDate =
                findViewById(R.id.tvDate);

        db = FirebaseFirestore.getInstance();

        String announcementId =
                getIntent().getStringExtra(
                        "ANNOUNCEMENT_ID"
                );

        db.collection("announcements")
                .document(announcementId)
                .get()
                .addOnSuccessListener(document -> {

                    if (document.exists()) {

                        tvTitle.setText(
                                document.getString(
                                        "title"
                                )
                        );

                        tvContent.setText(
                                document.getString(
                                        "content"
                                )
                        );

                        tvDate.setText(
                                "Date: "
                                        + document.getString(
                                        "date"
                                )
                        );

                    }

                });

    }
}