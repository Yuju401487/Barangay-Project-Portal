package com.kaibacorp.barangayprojectportal;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class AddProjectExpenseActivity extends AppCompatActivity {

    EditText etExpenseName;
    EditText etAmount;
    Button btnSaveExpense;

    FirebaseFirestore db;

    String projectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project_expense);

        etExpenseName =
                findViewById(R.id.etExpenseName);

        etAmount =
                findViewById(R.id.etAmount);

        btnSaveExpense =
                findViewById(R.id.btnSaveExpense);

        db = FirebaseFirestore.getInstance();

        projectId =
                getIntent().getStringExtra(
                        "PROJECT_ID"
                );

        btnSaveExpense.setOnClickListener(v -> {

            HashMap<String,Object> expense =
                    new HashMap<>();

            expense.put(
                    "projectId",
                    projectId
            );

            expense.put(
                    "expenseName",
                    etExpenseName.getText()
                            .toString()
                            .trim()
            );

            expense.put(
                    "amount",
                    etAmount.getText()
                            .toString()
                            .trim()
            );

            expense.put(
                    "date",
                    new SimpleDateFormat(
                            "MMMM dd, yyyy",
                            Locale.getDefault()
                    ).format(new Date())
            );

            db.collection("project_expenses")
                    .add(expense)
                    .addOnSuccessListener(ref -> {

                        Toast.makeText(
                                this,
                                "Expense Added",
                                Toast.LENGTH_SHORT
                        ).show();

                        finish();

                    });

        });

    }
}