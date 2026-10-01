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

public class AddProjectUpdateActivity extends AppCompatActivity {

    EditText etUpdate;
    Button btnSaveUpdate;

    FirebaseFirestore db;

    String projectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project_update);

        etUpdate = findViewById(R.id.etUpdate);
        btnSaveUpdate = findViewById(R.id.btnSaveUpdate);

        db = FirebaseFirestore.getInstance();

        projectId =
                getIntent().getStringExtra(
                        "PROJECT_ID"
                );

        btnSaveUpdate.setOnClickListener(v -> {

            String updateText =
                    etUpdate.getText()
                            .toString()
                            .trim();

            if(updateText.isEmpty()){

                Toast.makeText(
                        this,
                        "Enter update",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            HashMap<String,Object> update =
                    new HashMap<>();

            update.put(
                    "projectId",
                    projectId
            );

            update.put(
                    "updateText",
                    updateText
            );

            update.put(
                    "date",
                    new SimpleDateFormat(
                            "MMMM dd, yyyy",
                            Locale.getDefault()
                    ).format(
                            new Date()
                    )
            );

            db.collection("project_updates")
                    .add(update)
                    .addOnSuccessListener(ref -> {

                        Toast.makeText(
                                this,
                                "Update Added",
                                Toast.LENGTH_SHORT
                        ).show();

                        finish();

                    });

        });

    }
}