package com.kaibacorp.barangayprojectportal;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class UpdateProjectActivity extends AppCompatActivity {

    EditText etStatus;
    Button btnUpdate;

    FirebaseFirestore db;
    String projectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_project);

        etStatus = findViewById(R.id.etStatus);
        btnUpdate = findViewById(R.id.btnUpdate);

        db = FirebaseFirestore.getInstance();

        projectId = getIntent().getStringExtra("PROJECT_ID");

        btnUpdate.setOnClickListener(v -> {

            String status = etStatus.getText().toString().trim();

            if(status.isEmpty()) {

                Toast.makeText(
                        this,
                        "Enter a status",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            db.collection("projects")
                    .document(projectId)
                    .update("status", status)
                    .addOnSuccessListener(unused -> {

                        Toast.makeText(
                                this,
                                "Project Updated!",
                                Toast.LENGTH_LONG
                        ).show();

                        finish();

                    })
                    .addOnFailureListener(e -> {

                        Toast.makeText(
                                this,
                                e.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();

                    });

        });
    }
}