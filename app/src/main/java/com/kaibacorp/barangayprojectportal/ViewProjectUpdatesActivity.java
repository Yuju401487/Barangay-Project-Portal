package com.kaibacorp.barangayprojectportal;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class ViewProjectUpdatesActivity extends AppCompatActivity {

    private LinearLayout updateContainer;

    private FirebaseFirestore db;

    private String projectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(
                R.layout.activity_view_project_updates
        );

        updateContainer =
                findViewById(
                        R.id.updateContainer
                );

        db = FirebaseFirestore.getInstance();

        projectId =
                getIntent().getStringExtra(
                        "PROJECT_ID"
                );

        loadUpdates();
    }

    private void loadUpdates() {

        updateContainer.removeAllViews();

        db.collection("project_updates")
                .get()
                .addOnSuccessListener(querySnapshot -> {

                    querySnapshot.forEach(document -> {

                        String currentProjectId =
                                document.getString(
                                        "projectId"
                                );

                        if(currentProjectId == null)
                            return;

                        if(!currentProjectId.equals(projectId))
                            return;

                        String updateText =
                                document.getString(
                                        "updateText"
                                );

                        String date =
                                document.getString(
                                        "date"
                                );

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
                                Color.parseColor(
                                        "#FCE4EC"
                                )
                        );

                        card.setElevation(12f);

                        TextView tv =
                                new TextView(this);

                        tv.setText(

                                "Date: "
                                        + date

                                        +

                                        "\n\n"

                                        +

                                        updateText

                        );

                        tv.setTextSize(18);

                        tv.setTextColor(
                                Color.BLACK
                        );

                        card.addView(tv);

                        updateContainer.addView(card);

                    });

                });

    }
}