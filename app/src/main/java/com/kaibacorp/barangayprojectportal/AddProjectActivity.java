package com.kaibacorp.barangayprojectportal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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

import java.util.HashMap;
import java.util.Map;



public class AddProjectActivity extends AppCompatActivity {

    EditText etTitle;
    EditText etDescription;
    EditText etBudget;
    EditText etStatus;
    EditText etRaoRefCode;

    Button btnSaveProject;
    Button btnChooseImage;

    ImageView imgProjectPreview;

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
                                &&
                                result.getData() != null) {

                            selectedImageUri =
                                    result.getData().getData();

                            imgProjectPreview.setImageURI(
                                    selectedImageUri
                            );
                        }
                    }
            );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);

        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        etBudget = findViewById(R.id.etBudget);
        etStatus = findViewById(R.id.etStatus);
        etRaoRefCode = findViewById(R.id.etRaoRefCode);

        btnSaveProject = findViewById(R.id.btnSaveProject);

        btnChooseImage =
                findViewById(R.id.btnChooseImage);

        imgProjectPreview =
                findViewById(R.id.imgProjectPreview);

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

        btnSaveProject.setOnClickListener(v -> {

            String title =
                    etTitle.getText()
                            .toString()
                            .trim();

            String description =
                    etDescription.getText()
                            .toString()
                            .trim();

            String budget =
                    etBudget.getText()
                            .toString()
                            .trim();

            String status =
                    etStatus.getText()
                            .toString()
                            .trim();

            String raoRefCode =
                    etRaoRefCode.getText()
                            .toString()
                            .trim();

            if (title.isEmpty()
                    || description.isEmpty()
                    || budget.isEmpty()
                    || status.isEmpty()
                    || raoRefCode.isEmpty()) {

                Toast.makeText(
                        this,
                        "Fill all fields",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            Map<String, Object> project =
                    new HashMap<>();

            project.put("title", title);
            project.put("description", description);
            project.put("budget", budget);
            project.put("status", status);
            project.put("raoRefCode", raoRefCode);

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

                                project.put(
                                        "imageUrl",
                                        imageUrl
                                );

                                db.collection("projects")
                                        .add(project)
                                        .addOnSuccessListener(
                                                documentReference ->

                                                        Toast.makeText(
                                                                AddProjectActivity.this,
                                                                "Project Saved!",
                                                                Toast.LENGTH_LONG
                                                        ).show()
                                        )

                                        .addOnFailureListener(
                                                e ->

                                                        Toast.makeText(
                                                                AddProjectActivity.this,
                                                                e.getMessage(),
                                                                Toast.LENGTH_LONG
                                                        ).show()
                                        );
                            });
                        }

                        @Override
                        public void onError(
                                String error
                        ) {

                            runOnUiThread(() ->

                                    Toast.makeText(
                                            AddProjectActivity.this,
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
                    "project_"
                            + System.currentTimeMillis()
                            + ".jpg";

            String uploadUrl =
                    SUPABASE_URL
                            + "/storage/v1/object/project-images/"
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
                                                + "/storage/v1/object/public/project-images/"
                                                + fileName;

                                callback.onSuccess(
                                        publicUrl
                                );

                            } else {

                                String errorMessage = "";

                                try {
                                    errorMessage = response.body().string();
                                } catch (Exception ignored) {
                                }

                                callback.onError(
                                        "Upload Failed: "
                                                + response.code()
                                                + "\n"
                                                + errorMessage
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