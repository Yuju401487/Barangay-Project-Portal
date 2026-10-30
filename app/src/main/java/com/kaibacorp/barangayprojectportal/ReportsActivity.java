package com.kaibacorp.barangayprojectportal;

import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.Toast;

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
                                        "MonthlyReport.pdf",
                                        "MONTHLY REPORT",
                                        projectCount,
                                        expenseCount,
                                        totalExpense
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
                                        totalExpense
                                );

                            });

                });

    }

    private void createPdf(
            String fileName,
            String reportTitle,
            int projects,
            int expenses,
            double total
    ) {

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