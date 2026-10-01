package com.kaibacorp.barangayprojectportal;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class UpdateAnnouncementActivity extends AppCompatActivity {

    EditText etTitle;
    EditText etContent;
    Button btnUpdateAnnouncement;

    FirebaseFirestore db;

    String announcementId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(
                R.layout.activity_update_announcement
        );

        etTitle =
                findViewById(R.id.etTitle);

        etContent =
                findViewById(R.id.etContent);

        btnUpdateAnnouncement =
                findViewById(
                        R.id.btnUpdateAnnouncement
                );

        db = FirebaseFirestore.getInstance();

        announcementId =
                getIntent().getStringExtra(
                        "ANNOUNCEMENT_ID"
                );

        db.collection("announcements")
                .document(announcementId)
                .get()
                .addOnSuccessListener(document -> {

                    etTitle.setText(
                            document.getString("title")
                    );

                    etContent.setText(
                            document.getString("content")
                    );

                });

        btnUpdateAnnouncement.setOnClickListener(v -> {

            HashMap<String, Object> update =
                    new HashMap<>();

            update.put(
                    "title",
                    etTitle.getText()
                            .toString()
                            .trim()
            );

            update.put(
                    "content",
                    etContent.getText()
                            .toString()
                            .trim()
            );

            db.collection("announcements")
                    .document(announcementId)
                    .update(update)
                    .addOnSuccessListener(unused -> {

                        Toast.makeText(
                                this,
                                "Announcement Updated",
                                Toast.LENGTH_SHORT
                        ).show();

                        finish();

                    });

        });

    }
}