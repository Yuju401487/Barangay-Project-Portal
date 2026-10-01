package com.kaibacorp.barangayprojectportal;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class AddAnnouncementActivity extends AppCompatActivity {

    private EditText etTitle;
    private EditText etContent;

    private Button btnSaveAnnouncement;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_announcement);

        etTitle =
                findViewById(R.id.etTitle);

        etContent =
                findViewById(R.id.etContent);

        btnSaveAnnouncement =
                findViewById(
                        R.id.btnSaveAnnouncement
                );

        db = FirebaseFirestore.getInstance();

        btnSaveAnnouncement.setOnClickListener(v -> {

            String title =
                    etTitle.getText()
                            .toString()
                            .trim();

            String content =
                    etContent.getText()
                            .toString()
                            .trim();

            HashMap<String, Object> announcement =
                    new HashMap<>();

            announcement.put(
                    "title",
                    title
            );

            announcement.put(
                    "content",
                    content
            );

            announcement.put(
                    "date",
                    "September 30, 2026"
            );

            db.collection("announcements")
                    .add(announcement)
                    .addOnSuccessListener(
                            documentReference -> {

                                Toast.makeText(
                                        this,
                                        "Announcement Added",
                                        Toast.LENGTH_SHORT
                                ).show();

                                finish();

                            });

        });
    }
}