package com.kaibacorp.barangayprojectportal;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class NotificationHelper {

    public static void createNotification(
            String title,
            String message,
            String imageUrl,
            String recipientRole
    ) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> notification = new HashMap<>();

        notification.put("title", title);
        notification.put("message", message);
        notification.put("imageUrl", imageUrl);
        notification.put("recipientRole", recipientRole);
        notification.put("timestamp", System.currentTimeMillis());
        notification.put("isRead", false);

        db.collection("notifications")
                .add(notification);
    }
}