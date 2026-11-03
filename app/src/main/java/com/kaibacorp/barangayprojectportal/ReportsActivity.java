package com.kaibacorp.barangayprojectportal;

import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;

public class ReportsActivity extends AppCompatActivity {

    Button btnMonthlyReport;
    Button btnAnnualReport;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        db = FirebaseFirestore.getInstance();

        btnMonthlyReport =
                findViewById(R.id.btnMonthlyReport);

        btnAnnualReport =
                findViewById(R.id.btnAnnualReport);

        btnMonthlyReport.setOnClickListener(v -> {
            generateMonthlyReport();
        });

        btnAnnualReport.setOnClickListener(v -> {
            generateAnnualReport();
        });
    }

    private void generateMonthlyReport() {

        db.collection("projects")
                .get()
                .addOnSuccessListener(projectSnapshot -> {

                    db.collection("project_expenses")
                            .get()
                            .addOnSuccessListener(expenseSnapshot -> {

                                int projectCount = 0;
                                int expenseCount = 0;
                                double totalExpense = 0;

                                ArrayList<String> projectDetails =
                                        new ArrayList<>();

                                ArrayList<String> expenseDetails =
                                        new ArrayList<>();

                                Calendar now =
                                        Calendar.getInstance();

                                int currentMonth =
                                        now.get(Calendar.MONTH);

                                int currentYear =
                                        now.get(Calendar.YEAR);

                                for (DocumentSnapshot doc : projectSnapshot) {

                                    Long timestamp =
                                            doc.getLong(
                                                    "timestamp"
                                            );

                                    if (timestamp == null)
                                        continue;

                                    Calendar cal =
                                            Calendar.getInstance();

                                    cal.setTimeInMillis(
                                            timestamp
                                    );

                                    if (cal.get(Calendar.MONTH)
                                            == currentMonth
                                            &&
                                            cal.get(Calendar.YEAR)
                                                    == currentYear) {

                                        projectCount++;

                                        String title =
                                                doc.getString("title");

                                        String budget =
                                                doc.getString("budget");

                                        String status =
                                                doc.getString("status");

                                        String dateStarted =
                                                new SimpleDateFormat(
                                                        "MMMM dd, yyyy",
                                                        Locale.getDefault()
                                                ).format(
                                                        new Date(timestamp)
                                                );

                                        projectDetails.add(

                                                "Project: "
                                                        + title

                                                        + "\nStarted: "
                                                        + dateStarted

                                                        + "\nBudget: PHP "
                                                        + budget

                                                        + "\nStatus: "
                                                        + status
                                        );
                                    }
                                }

                                for (DocumentSnapshot doc : expenseSnapshot) {

                                    Long timestamp =
                                            doc.getLong(
                                                    "timestamp"
                                            );

                                    if (timestamp == null)
                                        continue;

                                    Calendar cal =
                                            Calendar.getInstance();

                                    cal.setTimeInMillis(
                                            timestamp
                                    );

                                    if (cal.get(Calendar.MONTH)
                                            == currentMonth
                                            &&
                                            cal.get(Calendar.YEAR)
                                                    == currentYear) {

                                        expenseCount++;

                                        String expenseName =
                                                doc.getString(
                                                        "expenseName"
                                                );

                                        String amount =
                                                doc.getString(
                                                        "amount"
                                                );

                                        String expenseDate =
                                                new SimpleDateFormat(
                                                        "MMMM dd, yyyy",
                                                        Locale.getDefault()
                                                ).format(
                                                        new Date(timestamp)
                                                );

                                        expenseDetails.add(

                                                expenseName
                                                        + " | PHP "
                                                        + amount
                                                        + " | "
                                                        + expenseDate
                                        );

                                        try {

                                            totalExpense +=
                                                    Double.parseDouble(
                                                            amount
                                                    );

                                        } catch (Exception ignored) {
                                        }
                                    }
                                }

                                createPdf(
                                        "MonthlyReport.pdf",
                                        "MONTHLY REPORT",
                                        projectCount,
                                        expenseCount,
                                        totalExpense,
                                        projectDetails,
                                        expenseDetails
                                );

                            });

                });

    }

    private void generateAnnualReport() {

        db.collection("projects")
                .get()
                .addOnSuccessListener(projectSnapshot -> {

                    db.collection("project_expenses")
                            .get()
                            .addOnSuccessListener(expenseSnapshot -> {

                                int projectCount = 0;
                                int expenseCount = 0;
                                double totalExpense = 0;

                                ArrayList<String> projectDetails =
                                        new ArrayList<>();

                                ArrayList<String> expenseDetails =
                                        new ArrayList<>();

                                int currentYear =
                                        Calendar.getInstance()
                                                .get(
                                                        Calendar.YEAR
                                                );

                                for (DocumentSnapshot doc : projectSnapshot) {

                                    Long timestamp =
                                            doc.getLong(
                                                    "timestamp"
                                            );

                                    if (timestamp == null)
                                        continue;

                                    Calendar cal =
                                            Calendar.getInstance();

                                    cal.setTimeInMillis(
                                            timestamp
                                    );

                                    if (cal.get(Calendar.YEAR)
                                            == currentYear) {

                                        projectCount++;

                                        String title =
                                                doc.getString("title");

                                        String budget =
                                                doc.getString("budget");

                                        String status =
                                                doc.getString("status");

                                        String dateStarted =
                                                new java.text.SimpleDateFormat(
                                                        "MMMM dd, yyyy",
                                                        java.util.Locale.getDefault()
                                                ).format(
                                                        new java.util.Date(timestamp)
                                                );

                                        projectDetails.add(
                                                "Project: "
                                                        + title
                                                        + "\nStarted: "
                                                        + dateStarted
                                                        + "\nBudget: PHP "
                                                        + budget
                                                        + "\nStatus: "
                                                        + status
                                        );
                                    }
                                }

                                for (DocumentSnapshot doc : expenseSnapshot) {

                                    Long timestamp =
                                            doc.getLong(
                                                    "timestamp"
                                            );

                                    if (timestamp == null)
                                        continue;

                                    Calendar cal =
                                            Calendar.getInstance();

                                    cal.setTimeInMillis(
                                            timestamp
                                    );

                                    if (cal.get(Calendar.YEAR)
                                            == currentYear) {

                                        expenseCount++;

                                        try {

                                            totalExpense +=
                                                    Double.parseDouble(
                                                            doc.getString(
                                                                    "amount"
                                                            )
                                                    );

                                        } catch (Exception ignored) {
                                        }
                                    }
                                }

                                createPdf(
                                        "AnnualReport.pdf",
                                        "ANNUAL REPORT",
                                        projectCount,
                                        expenseCount,
                                        totalExpense,
                                        projectDetails,
                                        expenseDetails
                                );

                            });

                });

    }

    private void createPdf(
            String fileName,
            String reportTitle,
            int projects,
            int expenses,
            double total,
            ArrayList<String> projectDetails,
            ArrayList<String> expenseDetails
    )
    {

        PdfDocument pdfDocument =
                new PdfDocument();

        PdfDocument.PageInfo pageInfo =
                new PdfDocument.PageInfo.Builder(
                        595,
                        842,
                        1
                ).create();

        PdfDocument.Page page =
                pdfDocument.startPage(pageInfo);

        Paint paint =
                new Paint();

        int x = 40;
        int y = 60;

        page.getCanvas().drawText(
                "BARANGAY PROJECT PORTAL",
                x,
                y,
                paint
        );

        y += 40;

        page.getCanvas().drawText(
                reportTitle,
                x,
                y,
                paint
        );

        y += 60;

        page.getCanvas().drawText(
                "Projects Created: "
                        + projects,
                x,
                y,
                paint
        );

        y += 40;

        page.getCanvas().drawText(
                "Expenses Recorded: "
                        + expenses,
                x,
                y,
                paint
        );

        y += 40;

        page.getCanvas().drawText(
                "Total Expenses: PHP "
                        + total,
                x,
                y,
                paint
        );

        y += 70;

        page.getCanvas().drawText(
                "PROJECT DETAILS",
                x,
                y,
                paint
        );

        for (String project : projectDetails) {

            y += 35;

            String[] lines =
                    project.split("\n");

            for (String line : lines) {

                page.getCanvas().drawText(
                        line,
                        x,
                        y,
                        paint
                );

                y += 25;
            }

            y += 15;
        }

        y += 20;

        page.getCanvas().drawText(
                "EXPENSE DETAILS",
                x,
                y,
                paint
        );

        for (String expense : expenseDetails) {

            y += 25;

            page.getCanvas().drawText(
                    expense,
                    x,
                    y,
                    paint
            );
        }

        pdfDocument.finishPage(page);

        try {

            File file =
                    new File(
                            Environment
                                    .getExternalStoragePublicDirectory(
                                            Environment.DIRECTORY_DOWNLOADS
                                    ),
                            fileName
                    );

            pdfDocument.writeTo(
                    new FileOutputStream(
                            file
                    )
            );

            Toast.makeText(
                    this,
                    "Saved: "
                            + file.getAbsolutePath(),
                    Toast.LENGTH_LONG
            ).show();

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    e.getMessage(),
                    Toast.LENGTH_LONG
            ).show();
        }

        pdfDocument.close();
    }
}