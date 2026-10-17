package com.kaibacorp.barangayprojectportal;

import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class VotePollActivity extends AppCompatActivity {

    TextView tvQuestion;
    RadioGroup radioGroupOptions;
    Button btnVote;

    FirebaseFirestore db;

    String pollId;

    int selectedOptionNumber = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_poll);

        tvQuestion =
                findViewById(R.id.tvQuestion);

        radioGroupOptions =
                findViewById(R.id.radioGroupOptions);

        btnVote =
                findViewById(R.id.btnVote);

        db = FirebaseFirestore.getInstance();

        pollId =
                getIntent().getStringExtra(
                        "POLL_ID"
                );

        loadPoll();

        btnVote.setOnClickListener(v -> {

            String userEmail =
                    FirebaseAuth.getInstance()
                            .getCurrentUser()
                            .getEmail();

            int selectedId =
                    radioGroupOptions.getCheckedRadioButtonId();

            if (selectedId == -1) {

                Toast.makeText(
                        this,
                        "Select an option",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            RadioButton selectedRadio =
                    findViewById(selectedId);

            selectedOptionNumber =
                    (int) selectedRadio.getTag();

            db.collection("poll_votes")
                    .whereEqualTo(
                            "pollId",
                            pollId
                    )
                    .whereEqualTo(
                            "userEmail",
                            userEmail
                    )
                    .get()
                    .addOnSuccessListener(snapshot -> {

                        if (!snapshot.isEmpty()) {

                            Toast.makeText(
                                    this,
                                    "You already voted",
                                    Toast.LENGTH_SHORT
                            ).show();

                            return;
                        }

                        HashMap<String, Object> vote =
                                new HashMap<>();

                        vote.put(
                                "pollId",
                                pollId
                        );

                        vote.put(
                                "userEmail",
                                userEmail
                        );

                        db.collection("poll_votes")
                                .add(vote);

                        db.collection("polls")
                                .document(pollId)
                                .get()
                                .addOnSuccessListener(document -> {

                                    Long currentVotes =
                                            document.getLong(
                                                    "votes" + selectedOptionNumber
                                            );

                                    if (currentVotes == null) {
                                        currentVotes = 0L;
                                    }

                                    db.collection("polls")
                                            .document(pollId)
                                            .update(
                                                    "votes" + selectedOptionNumber,
                                                    currentVotes + 1
                                            );

                                    Toast.makeText(
                                            VotePollActivity.this,
                                            "Vote Submitted",
                                            Toast.LENGTH_SHORT
                                    ).show();

                                    finish();

                                });

                    });

        });
    }

    private void loadPoll() {

        db.collection("polls")
                .document(pollId)
                .get()
                .addOnSuccessListener(document -> {

                    tvQuestion.setText(
                            document.getString("question")
                    );

                    radioGroupOptions.removeAllViews();

                    int optionNumber = 1;

                    while (true) {

                        String option =
                                document.getString(
                                        "option" + optionNumber
                                );

                        if (option == null ||
                                option.trim().isEmpty()) {
                            break;
                        }

                        RadioButton rb =
                                new RadioButton(this);

                        rb.setText(option);

                        rb.setTextColor(
                                0xFF000000
                        );

                        rb.setTag(optionNumber);

                        radioGroupOptions.addView(rb);

                        optionNumber++;
                    }

                });

    }
}