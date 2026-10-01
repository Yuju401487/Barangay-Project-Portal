package com.kaibacorp.barangayprojectportal;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class CreatePollActivity extends AppCompatActivity {

    EditText etQuestion;
    EditText etOption1;
    EditText etOption2;
    EditText etOption3;

    Button btnCreatePoll;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_poll);

        etQuestion = findViewById(R.id.etQuestion);
        etOption1 = findViewById(R.id.etOption1);
        etOption2 = findViewById(R.id.etOption2);
        etOption3 = findViewById(R.id.etOption3);

        btnCreatePoll =
                findViewById(R.id.btnCreatePoll);

        db = FirebaseFirestore.getInstance();

        btnCreatePoll.setOnClickListener(v -> {

            HashMap<String,Object> poll =
                    new HashMap<>();

            poll.put(
                    "question",
                    etQuestion.getText().toString().trim()
            );

            poll.put(
                    "option1",
                    etOption1.getText().toString().trim()
            );

            poll.put(
                    "option2",
                    etOption2.getText().toString().trim()
            );

            poll.put(
                    "option3",
                    etOption3.getText().toString().trim()
            );

            poll.put("votes1",0);
            poll.put("votes2",0);
            poll.put("votes3",0);

            db.collection("polls")
                    .add(poll)
                    .addOnSuccessListener(ref -> {

                        Toast.makeText(
                                this,
                                "Poll Created",
                                Toast.LENGTH_SHORT
                        ).show();

                        finish();

                    });

        });

    }
}