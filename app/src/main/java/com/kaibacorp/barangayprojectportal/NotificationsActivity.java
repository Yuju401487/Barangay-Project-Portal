package com.kaibacorp.barangayprojectportal;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class NotificationsActivity extends AppCompatActivity {

    RecyclerView recyclerNotifications;
    ArrayList<NotificationModel> notificationList;
    NotificationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        recyclerNotifications =
                findViewById(R.id.recyclerNotifications);

        recyclerNotifications.setLayoutManager(
                new LinearLayoutManager(this)
        );

        notificationList = new ArrayList<>();

        adapter = new NotificationAdapter(
                notificationList
        );

        recyclerNotifications.setAdapter(adapter);

        String role =
                getSharedPreferences(
                        "USER_DATA",
                        MODE_PRIVATE
                ).getString(
                        "ROLE",
                        ""
                );

        FirebaseFirestore.getInstance()
                .collection("notifications")
                .whereEqualTo(
                        "recipientRole",
                        role
                )
                .get()
                .addOnSuccessListener(snapshot -> {

                    notificationList.clear();

                    for (DocumentSnapshot doc : snapshot) {

                        NotificationModel notification =
                                doc.toObject(
                                        NotificationModel.class
                                );

                        if (notification != null) {

                            notification.setDocumentId(
                                    doc.getId()
                            );

                            notificationList.add(
                                    notification
                            );
                        }
                    }

                    adapter.notifyDataSetChanged();

                });

    }
}
