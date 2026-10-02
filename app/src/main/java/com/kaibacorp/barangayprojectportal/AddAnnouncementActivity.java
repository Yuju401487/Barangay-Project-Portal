package com.kaibacorp.barangayprojectportal;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.InputStream;
import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class AddAnnouncementActivity extends AppCompatActivity {

    private EditText etTitle;
    private EditText etContent;

    private Button btnSaveAnnouncement;

    private Button btnChooseImage;

    private ImageView imgAnnouncementPreview;

    private Uri selectedImageUri;

    private FirebaseFirestore db;

    private static final String SUPABASE_URL =
            "https://vmswctwkkjtfdxnjtulj.supabase.co";

    private static final String SUPABASE_KEY =
            "sb_publishable_TpS0U_wBVBE_N2MguWrZIw_jeZqRKLF";

    private final androidx.activity.result.ActivityResultLauncher<Intent>
            imagePickerLauncher =
            registerForActivityResult(
                    new androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult(),
                    result -> {

                        if (result.getResultCode() == RESULT_OK
                                && result.getData() != null) {

                            selectedImageUri =
                                    result.getData().getData();

                            imgAnnouncementPreview
                                    .setImageURI(
                                            selectedImageUri
                                    );
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_announcement);

        etTitle =
                findViewById(R.id.etTitle);

        etContent =
                findViewById(R.id.etContent);

        btnSaveAnnouncement =
                findViewById(
                        R.id.btnSaveAnnouncement
                );

        btnChooseImage =
                findViewById(
                        R.id.btnChooseImage
                );

        imgAnnouncementPreview =
                findViewById(
                        R.id.imgAnnouncementPreview
                );


        db = FirebaseFirestore.getInstance();

        btnChooseImage.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            Intent.ACTION_PICK
                    );

            intent.setType("image/*");

            imagePickerLauncher.launch(
                    intent
            );

        });

        btnSaveAnnouncement.setOnClickListener(v -> {

            String title =
                    etTitle.getText()
                            .toString()
                            .trim();

            String content =
                    etContent.getText()
                            .toString()
                            .trim();

            HashMap<String, Object> announcement =
                    new HashMap<>();

            announcement.put(
                    "title",
                    title
            );

            announcement.put(
                    "content",
                    content
            );

            announcement.put(
                    "date",
                    new SimpleDateFormat(
                            "MMMM dd, yyyy",
                            Locale.getDefault()
                    ).format(
                            new Date()
                    )
            );
            if (selectedImageUri == null) {

                Toast.makeText(
                        this,
                        "Please select an image",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            uploadImageToSupabase(
                    selectedImageUri,
                    new UploadCallback() {

                        @Override
                        public void onSuccess(
                                String imageUrl
                        ) {

                            runOnUiThread(() -> {

                                announcement.put(
                                        "imageUrl",
                                        imageUrl
                                );

                                db.collection("announcements")
                                        .add(announcement)
                                        .addOnSuccessListener(
                                                documentReference -> {

                                                    Toast.makeText(
                                                            AddAnnouncementActivity.this,
                                                            "Announcement Added",
                                                            Toast.LENGTH_SHORT
                                                    ).show();

                                                    finish();
                                                }
                                        );
                            });
                        }

                        @Override
                        public void onError(
                                String error
                        ) {

                            runOnUiThread(() ->

                                    Toast.makeText(
                                            AddAnnouncementActivity.this,
                                            error,
                                            Toast.LENGTH_LONG
                                    ).show()
                            );
                        }
                    }
            );

        });
    }


    private void uploadImageToSupabase(
            Uri imageUri,
            UploadCallback callback
    ) {

        try {

            InputStream inputStream =
                    getContentResolver()
                            .openInputStream(imageUri);

            java.io.ByteArrayOutputStream buffer =
                    new java.io.ByteArrayOutputStream();

            int nRead;

            byte[] data =
                    new byte[16384];

            while ((nRead = inputStream.read(data, 0, data.length))
                    != -1) {

                buffer.write(
                        data,
                        0,
                        nRead
                );
            }

            buffer.flush();

            byte[] imageBytes =
                    buffer.toByteArray();

            String fileName =
                    "announcement_"
                            + System.currentTimeMillis()
                            + ".jpg";

            String uploadUrl =
                    SUPABASE_URL
                            + "/storage/v1/object/announcement-images/"
                            + fileName;

            RequestBody body =
                    RequestBody.create(
                            MediaType.parse("image/jpeg"),
                            imageBytes
                    );

            Request request =
                    new Request.Builder()
                            .url(uploadUrl)
                            .addHeader(
                                    "apikey",
                                    SUPABASE_KEY
                            )
                            .addHeader(
                                    "Authorization",
                                    "Bearer " + SUPABASE_KEY
                            )
                            .addHeader(
                                    "x-upsert",
                                    "true"
                            )
                            .addHeader(
                                    "Content-Type",
                                    "image/jpeg"
                            )
                            .put(body)
                            .build();

            OkHttpClient client =
                    new OkHttpClient();

            client.newCall(request)
                    .enqueue(new Callback() {

                        @Override
                        public void onFailure(
                                Call call,
                                IOException e
                        ) {

                            callback.onError(
                                    e.getMessage()
                            );
                        }

                        @Override
                        public void onResponse(
                                Call call,
                                Response response
                        ) {

                            if (response.isSuccessful()) {

                                String publicUrl =
                                        SUPABASE_URL
                                                + "/storage/v1/object/public/announcement-images/"
                                                + fileName;

                                callback.onSuccess(
                                        publicUrl
                                );

                            } else {

                                callback.onError(
                                        "Upload Failed"
                                );
                            }
                        }
                    });

        } catch (Exception e) {

            callback.onError(
                    e.getMessage()
            );
        }
    }
    interface UploadCallback {

        void onSuccess(
                String imageUrl
        );

        void onError(
                String error
        );
    }
}