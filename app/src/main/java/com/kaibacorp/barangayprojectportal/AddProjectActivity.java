package com.kaibacorp.barangayprojectportal;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddProjectActivity extends AppCompatActivity {

    EditText etTitle;
    EditText etDescription;
    EditText etBudget;
    EditText etStatus;
    EditText etRaoRefCode;

    Button btnSaveProject;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);

        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        etBudget = findViewById(R.id.etBudget);
        etStatus = findViewById(R.id.etStatus);
        etRaoRefCode = findViewById(R.id.etRaoRefCode);

        btnSaveProject = findViewById(R.id.btnSaveProject);

        db = FirebaseFirestore.getInstance();

        btnSaveProject.setOnClickListener(v -> {

            String title =
                    etTitle.getText()
                            .toString()
                            .trim();

            String description =
                    etDescription.getText()
                            .toString()
                            .trim();

            String budget =
                    etBudget.getText()
                            .toString()
                            .trim();

            String status =
                    etStatus.getText()
                            .toString()
                            .trim();

            String raoRefCode =
                    etRaoRefCode.getText()
                            .toString()
                            .trim();

            if (title.isEmpty()
                    || description.isEmpty()
                    || budget.isEmpty()
                    || status.isEmpty()
                    || raoRefCode.isEmpty()) {

                Toast.makeText(
                        this,
                        "Fill all fields",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            Map<String, Object> project =
                    new HashMap<>();

            project.put("title", title);
            project.put("description", description);
            project.put("budget", budget);
            project.put("status", status);
            project.put("raoRefCode", raoRefCode);

            db.collection("projects")
                    .add(project)
                    .addOnSuccessListener(documentReference ->

                            Toast.makeText(
                                    this,
                                    "Project Saved!",
                                    Toast.LENGTH_LONG
                            ).show()

                    )

                    .addOnFailureListener(e ->

                            Toast.makeText(
                                    this,
                                    e.getMessage(),
                                    Toast.LENGTH_LONG
                            ).show()

                    );

        });
    }
}