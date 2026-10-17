package com.kaibacorp.barangayprojectportal;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class CreatePollActivity extends AppCompatActivity {

    EditText etQuestion;
    EditText etOption1;
    EditText etOption2;

    Button btnAddOption;
    Button btnCreatePoll;

    LinearLayout optionsContainer;

    FirebaseFirestore db;

    ArrayList<EditText> optionFields = new ArrayList<>();

    int optionCount = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_poll);

        etQuestion = findViewById(R.id.etQuestion);
        etOption1 = findViewById(R.id.etOption1);
        etOption2 = findViewById(R.id.etOption2);

        btnAddOption = findViewById(R.id.btnAddOption);
        btnCreatePoll = findViewById(R.id.btnCreatePoll);

        optionsContainer = findViewById(R.id.optionsContainer);

        db = FirebaseFirestore.getInstance();

        optionFields.add(etOption1);
        optionFields.add(etOption2);

        btnAddOption.setOnClickListener(v -> {

            optionCount++;

            EditText newOption = new EditText(this);

            newOption.setHint("Option " + optionCount);
            newOption.setTextColor(0xFF000000);
            newOption.setHintTextColor(0xFF808080);

            optionFields.add(newOption);

            optionsContainer.addView(newOption);

        });

        btnCreatePoll.setOnClickListener(v -> {

            String question =
                    etQuestion.getText()
                            .toString()
                            .trim();

            if (question.isEmpty()) {

                Toast.makeText(
                        this,
                        "Please enter a question",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            HashMap<String, Object> poll =
                    new HashMap<>();

            poll.put("question", question);

            int validOptions = 0;

            for (int i = 0; i < optionFields.size(); i++) {

                String option =
                        optionFields.get(i)
                                .getText()
                                .toString()
                                .trim();

                if (!option.isEmpty()) {

                    validOptions++;

                    poll.put(
                            "option" + validOptions,
                            option
                    );

                    poll.put(
                            "votes" + validOptions,
                            0
                    );
                }
            }

            if (validOptions < 2) {

                Toast.makeText(
                        this,
                        "Enter at least 2 options",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            db.collection("polls")
                    .add(poll)
                    .addOnSuccessListener(ref -> {

                        Toast.makeText(
                                this,
                                "Poll Created",
                                Toast.LENGTH_SHORT
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