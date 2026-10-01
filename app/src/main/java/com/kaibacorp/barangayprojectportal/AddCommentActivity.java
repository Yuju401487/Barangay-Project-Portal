package com.kaibacorp.barangayprojectportal;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class AddCommentActivity extends AppCompatActivity {

    EditText etComment;
    Button btnSubmitComment;

    FirebaseFirestore db;

    String projectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);

        etComment =
                findViewById(R.id.etComment);

        btnSubmitComment =
                findViewById(R.id.btnSubmitComment);

        db = FirebaseFirestore.getInstance();

        projectId =
                getIntent().getStringExtra(
                        "PROJECT_ID"
                );

        btnSubmitComment.setOnClickListener(v -> {

            String comment =
                    etComment.getText()
                            .toString()
                            .trim();

            if(comment.isEmpty()){

                Toast.makeText(
                        this,
                        "Enter a comment",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            HashMap<String,Object> data =
                    new HashMap<>();

            data.put(
                    "projectId",
                    projectId
            );

            data.put(
                    "comment",
                    comment
            );

            data.put(
                    "author",
                    "Resident"
            );

            data.put(
                    "likes",
                    0
            );

            data.put(
                    "date",
                    new SimpleDateFormat(
                            "MMMM dd, yyyy",
                            Locale.getDefault()
                    ).format(new Date())
            );

            db.collection("comments")
                    .add(data)
                    .addOnSuccessListener(ref -> {

                        Toast.makeText(
                                this,
                                "Comment Submitted",
                                Toast.LENGTH_SHORT
                        ).show();

                        finish();

                    });

        });

    }
}