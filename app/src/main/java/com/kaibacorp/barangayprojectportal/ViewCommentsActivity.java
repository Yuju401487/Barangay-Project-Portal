package com.kaibacorp.barangayprojectportal;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class ViewCommentsActivity extends AppCompatActivity {

    private LinearLayout commentContainer;

    private FirebaseFirestore db;

    private String projectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(
                R.layout.activity_view_comments
        );

        commentContainer =
                findViewById(
                        R.id.commentContainer
                );

        db = FirebaseFirestore.getInstance();

        projectId =
                getIntent().getStringExtra(
                        "PROJECT_ID"
                );

        loadComments();
    }

    private void loadComments() {

        commentContainer.removeAllViews();

        db.collection("comments")
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

                        String author =
                                document.getString(
                                        "author"
                                );

                        String comment =
                                document.getString(
                                        "comment"
                                );

                        String date =
                                document.getString(
                                        "date"
                                );

                        Long likes =
                                document.getLong(
                                        "likes"
                                );

                        if(likes == null)
                            likes = 0L;

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
                                Color.parseColor("#FCE4EC")
                        );

                        card.setElevation(12f);

                        TextView tv =
                                new TextView(this);

                        tv.setText(

                                "Author: "
                                        + author

                                        +

                                        "\n\nDate: "
                                        + date

                                        +

                                        "\n\nComment:\n"
                                        + comment

                                        +

                                        "\n\n❤️ Likes: "
                                        + likes

                        );

                        tv.setTextColor(
                                Color.BLACK
                        );

                        tv.setTextSize(18);

                        Button btnLike =
                                new Button(this);

                        btnLike.setText(
                                "LIKE"
                        );

                        btnLike.setTextColor(
                                Color.WHITE
                        );

                        btnLike.setBackgroundTintList(
                                ColorStateList.valueOf(
                                        Color.parseColor(
                                                "#EC407A"
                                        )
                                )
                        );

                        Long finalLikes = likes;

                        btnLike.setOnClickListener(v -> {

                            String userId =
                                    FirebaseAuth.getInstance()
                                            .getCurrentUser()
                                            .getUid();

                            db.collection("comment_likes")
                                    .whereEqualTo(
                                            "commentId",
                                            document.getId()
                                    )
                                    .whereEqualTo(
                                            "userId",
                                            userId
                                    )
                                    .get()
                                    .addOnSuccessListener(likeQuery -> {

                                        if (!likeQuery.isEmpty()) {

                                            android.widget.Toast.makeText(
                                                    ViewCommentsActivity.this,
                                                    "You already liked this comment",
                                                    android.widget.Toast.LENGTH_SHORT
                                            ).show();

                                            return;
                                        }

                                        db.collection("comments")
                                                .document(document.getId())
                                                .update(
                                                        "likes",
                                                        finalLikes + 1
                                                )
                                                .addOnSuccessListener(unused -> {

                                                    HashMap<String,Object> like =
                                                            new HashMap<>();

                                                    like.put(
                                                            "commentId",
                                                            document.getId()
                                                    );

                                                    like.put(
                                                            "userId",
                                                            userId
                                                    );

                                                    db.collection("comment_likes")
                                                            .add(like)
                                                            .addOnSuccessListener(
                                                                    ref -> loadComments()
                                                            );

                                                });

                                    });

                        });

                        card.addView(tv);
                        card.addView(btnLike);

                        commentContainer.addView(
                                card
                        );

                    });

                });

    }
}