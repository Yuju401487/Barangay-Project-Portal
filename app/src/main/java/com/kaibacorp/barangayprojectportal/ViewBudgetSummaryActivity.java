package com.kaibacorp.barangayprojectportal;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class ViewBudgetSummaryActivity extends AppCompatActivity {

    private TextView tvProjectBudget;
    private TextView tvTotalSpent;
    private TextView tvRemaining;

    private LinearLayout expenseContainer;

    private FirebaseFirestore db;

    private String projectId;

    private double projectBudget = 0;
    private double totalSpent = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_budget_summary);

        tvProjectBudget =
                findViewById(R.id.tvProjectBudget);

        tvTotalSpent =
                findViewById(R.id.tvTotalSpent);

        tvRemaining =
                findViewById(R.id.tvRemaining);

        expenseContainer =
                findViewById(R.id.expenseContainer);

        db = FirebaseFirestore.getInstance();

        projectId =
                getIntent().getStringExtra(
                        "PROJECT_ID"
                );

        loadBudget();
    }

    private void loadBudget() {

        db.collection("projects")
                .document(projectId)
                .get()
                .addOnSuccessListener(document -> {

                    String budget =
                            document.getString("budget");

                    if (budget != null) {
                        projectBudget =
                                Double.parseDouble(budget);
                    }

                    tvProjectBudget.setText(
                            "Project Budget: ₱"
                                    + projectBudget
                    );

                    loadExpenses();

                });

    }

    private void loadExpenses() {

        db.collection("project_expenses")
                .get()
                .addOnSuccessListener(querySnapshot -> {

                    totalSpent = 0;

                    querySnapshot.forEach(document -> {

                        String currentProjectId =
                                document.getString(
                                        "projectId"
                                );

                        if(currentProjectId == null)
                            return;

                        if(!currentProjectId.equals(projectId))
                            return;

                        String expenseName =
                                document.getString(
                                        "expenseName"
                                );

                        String amount =
                                document.getString(
                                        "amount"
                                );

                        double expenseAmount =
                                Double.parseDouble(
                                        amount
                                );

                        totalSpent += expenseAmount;

                        TextView tv =
                                new TextView(this);

                        tv.setText(

                                expenseName

                                        +

                                        "\n₱"

                                        +

                                        amount

                        );

                        tv.setTextSize(18);

                        tv.setTextColor(
                                Color.BLACK
                        );

                        expenseContainer.addView(tv);

                    });

                    double remaining =
                            projectBudget
                                    - totalSpent;

                    tvTotalSpent.setText(
                            "Total Spent: ₱"
                                    + totalSpent
                    );

                    tvRemaining.setText(
                            "Remaining Budget: ₱"
                                    + remaining
                    );

                });

    }
}