package com.kaibacorp.barangayprojectportal;

import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class VotePollActivity extends AppCompatActivity {

    TextView tvQuestion;

    RadioGroup radioGroupOptions;

    RadioButton rbOption1;
    RadioButton rbOption2;
    RadioButton rbOption3;

    Button btnVote;

    FirebaseFirestore db;

    String pollId;

    long votes1;
    long votes2;
    long votes3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_poll);

        tvQuestion =
                findViewById(R.id.tvQuestion);

        radioGroupOptions =
                findViewById(R.id.radioGroupOptions);

        rbOption1 =
                findViewById(R.id.rbOption1);

        rbOption2 =
                findViewById(R.id.rbOption2);

        rbOption3 =
                findViewById(R.id.rbOption3);

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
                    radioGroupOptions
                            .getCheckedRadioButtonId();

            if(selectedId == -1){

                Toast.makeText(
                        this,
                        "Select an option",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

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

                        if(selectedId == R.id.rbOption1){

                            db.collection("polls")
                                    .document(pollId)
                                    .update(
                                            "votes1",
                                            votes1 + 1
                                    );

                        }
                        else if(selectedId == R.id.rbOption2){

                            db.collection("polls")
                                    .document(pollId)
                                    .update(
                                            "votes2",
                                            votes2 + 1
                                    );

                        }
                        else{

                            db.collection("polls")
                                    .document(pollId)
                                    .update(
                                            "votes3",
                                            votes3 + 1
                                    );

                        }

                        Toast.makeText(
                                VotePollActivity.this,
                                "Vote Submitted",
                                Toast.LENGTH_SHORT
                        ).show();

                        finish();


        });

    });

}
    private void loadPoll() {

        db.collection("polls")
                .document(pollId)
                .get()
                .addOnSuccessListener(document -> {

                    tvQuestion.setText(
                            document.getString(
                                    "question"
                            )
                    );

                    rbOption1.setText(
                            document.getString(
                                    "option1"
                            )
                    );

                    rbOption2.setText(
                            document.getString(
                                    "option2"
                            )
                    );

                    rbOption3.setText(
                            document.getString(
                                    "option3"
                            )
                    );

                    Long v1 =
                            document.getLong(
                                    "votes1"
                            );

                    Long v2 =
                            document.getLong(
                                    "votes2"
                            );

                    Long v3 =
                            document.getLong(
                                    "votes3"
                            );

                    votes1 = v1 == null ? 0 : v1;
                    votes2 = v2 == null ? 0 : v2;
                    votes3 = v3 == null ? 0 : v3;

                });

    }
}