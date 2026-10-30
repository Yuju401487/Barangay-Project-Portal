package com.kaibacorp.barangayprojectportal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class NotificationAdapter
        extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private final ArrayList<NotificationModel> notificationList;

    public NotificationAdapter(
            ArrayList<NotificationModel> notificationList
    ) {
        this.notificationList = notificationList;
    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder {

        ImageView imgNotification;
        TextView txtTitle;
        TextView txtMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgNotification =
                    itemView.findViewById(
                            R.id.imgNotification
                    );

            txtTitle =
                    itemView.findViewById(
                            R.id.txtTitle
                    );

            txtMessage =
                    itemView.findViewById(
                            R.id.txtMessage
                    );
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view =
                LayoutInflater.from(
                        parent.getContext()
                ).inflate(
                        R.layout.notification_item,
                        parent,
                        false
                );

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position
    ) {

        NotificationModel notification =
                notificationList.get(position);

        holder.txtTitle.setText(
                notification.getTitle()
        );

        holder.txtMessage.setText(
                notification.getMessage()
        );

        Glide.with(
                        holder.itemView.getContext()
                )
                .load(
                        notification.getImageUrl()
                )
                .into(
                        holder.imgNotification
                );

        holder.itemView.setOnClickListener(v -> {

            FirebaseFirestore.getInstance()
                    .collection("notifications")
                    .document(
                            notification.getDocumentId()
                    )
                    .delete();

            int currentPosition =
                    holder.getAdapterPosition();

            if (currentPosition
                    != RecyclerView.NO_POSITION) {

                notificationList.remove(
                        currentPosition
                );

                notifyItemRemoved(
                        currentPosition
                );
            }

        });

    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }
}