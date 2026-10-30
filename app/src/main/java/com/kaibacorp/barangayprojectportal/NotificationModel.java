package com.kaibacorp.barangayprojectportal;

public class NotificationModel {

    private String documentId;
    private String title;
    private String message;
    private String imageUrl;

    public NotificationModel() {
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}