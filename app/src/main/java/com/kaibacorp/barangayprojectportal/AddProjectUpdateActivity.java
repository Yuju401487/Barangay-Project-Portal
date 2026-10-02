package com.kaibacorp.barangayprojectportal;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.io.InputStream;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class AddProjectUpdateActivity extends AppCompatActivity {

    EditText etUpdate;
    Button btnSaveUpdate;

    Button btnChooseImage;

    ImageView imgUpdatePreview;

    Uri selectedImageUri;

    FirebaseFirestore db;

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

                            imgUpdatePreview.setImageURI(
                                    selectedImageUri
                            );
                        }
                    }
            );


    String projectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project_update);

        etUpdate = findViewById(R.id.etUpdate);
        btnSaveUpdate = findViewById(R.id.btnSaveUpdate);

        btnChooseImage =
                findViewById(R.id.btnChooseImage);

        imgUpdatePreview =
                findViewById(R.id.imgUpdatePreview);

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

        projectId =
                getIntent().getStringExtra(
                        "PROJECT_ID"
                );


        btnSaveUpdate.setOnClickListener(v -> {

            String updateText =
                    etUpdate.getText()
                            .toString()
                            .trim();

            if(updateText.isEmpty()){

                Toast.makeText(
                        this,
                        "Enter update",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            HashMap<String,Object> update =
                    new HashMap<>();

            update.put(
                    "projectId",
                    projectId
            );

            update.put(
                    "updateText",
                    updateText
            );

            update.put(
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

                                update.put(
                                        "imageUrl",
                                        imageUrl
                                );

                                db.collection("project_updates")
                                        .add(update)
                                        .addOnSuccessListener(ref -> {

                                            Toast.makeText(
                                                    AddProjectUpdateActivity.this,
                                                    "Update Added",
                                                    Toast.LENGTH_SHORT
                                            ).show();

                                            finish();

                                        });
                            });
                        }

                        @Override
                        public void onError(
                                String error
                        ) {

                            runOnUiThread(() ->

                                    Toast.makeText(
                                            AddProjectUpdateActivity.this,
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

            while ((nRead = inputStream.read(data))
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
                    "update_"
                            + System.currentTimeMillis()
                            + ".jpg";

            RequestBody body =
                    RequestBody.create(
                            MediaType.parse("image/jpeg"),
                            imageBytes
                    );

            Request request =
                    new Request.Builder()
                            .url(
                                    "https://vmswctwkkjtfdxnjtulj.supabase.co/storage/v1/object/project-update-images/"
                                            + fileName
                            )
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

            new OkHttpClient()
                    .newCall(request)
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

                                callback.onSuccess(
                                        "https://vmswctwkkjtfdxnjtulj.supabase.co/storage/v1/object/public/project-update-images/"
                                                + fileName
                                );

                            } else {

                                callback.onError(
                                        "Upload Failed: "
                                                + response.code()
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