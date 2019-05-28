package com.nablanet.aula31.export;

import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nablanet.aula31.R;
import com.nablanet.aula31.classes.ClassActivity;
import com.nablanet.aula31.export.data.DataParams;
import com.nablanet.aula31.export.data.DataRepo;
import com.nablanet.aula31.export.entity.CourseExport;
import com.nablanet.aula31.export.entity.MemberCourseExport;
import com.nablanet.aula31.export.view.MemberAdapter;
import com.nablanet.aula31.repo.Response;
import com.nablanet.documents.odf.TemplateODS;
import com.nablanet.documents.odf.content.Body;
import com.nablanet.documents.odf.content.spreadsheet.Spreadsheet;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

public class ExportActivity extends AppCompatActivity {

    Toolbar toolbar;
    ProgressBar progressBar;

    ExportViewModel exportViewModel;

    TextView institutionTV, subjectTV;
    Button fromSpinner, toSpinner;
    CheckBox checkBox;
    RecyclerView recyclerView;
    MemberAdapter adapter;

    String courseId;
    CourseExport courseExport;
    Long from, to;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBar = findViewById(R.id.progressBar);

        institutionTV = findViewById(R.id.institution_tv);
        subjectTV = findViewById(R.id.subject_tv);

        fromSpinner = findViewById(R.id.from_bt);
        toSpinner = findViewById(R.id.to_bt);

        checkBox = findViewById(R.id.checkbox);

        recyclerView = findViewById(R.id.members_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MemberAdapter(new ArrayList<MemberCourseExport>(), R.layout.item_list_export);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                queryDataReport();
            }
        });

        bindViewModel();

    }

    private void bindViewModel() {

        if (getIntent().getExtras() == null) return;
        courseId = getIntent().getExtras().getString(ClassActivity.COURSE_ID_KEY);
        if (courseId == null) {
            Snackbar.make(toolbar, "No especificó curso", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        exportViewModel = ViewModelProviders.of(this).get(ExportViewModel.class);

        //Escuchamos las respuestas
        exportViewModel.getResponseLive().observe(this, new Observer<Response>() {
            @Override
            public void onChanged(@Nullable Response response) {
                progressBar.setVisibility(View.INVISIBLE);
                if (response != null)
                    Snackbar.make(toolbar, response.getMessage(), Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
            }
        });
        exportViewModel.getCourseExportLiveData(courseId).observe(
                this, new Observer<CourseExport>() {
                    @Override
                    public void onChanged(@Nullable CourseExport courseExport) {
                        progressBar.setVisibility(View.INVISIBLE);
                        ExportActivity.this.courseExport = courseExport;
                        if (courseExport != null) {
                            institutionTV.setText(courseExport.institutionName);
                            subjectTV.setText(courseExport.getSubjectFullName());
                            adapter.updateMembers(courseExport.memberCourseExportList);
                        } else {
                            institutionTV.setText("");
                            subjectTV.setText("");
                            adapter.updateMembers(new ArrayList<MemberCourseExport>());
                        }
                    }
                }
        );
    }

    private void queryDataReport() {

        DataParams dataParams = new DataParams(
            this.courseId, adapter.getMemberKeyList(true), from, to
        );

        progressBar.setVisibility(View.VISIBLE);

        exportViewModel.getDataRepoLive(dataParams).observe(
                ExportActivity.this, new Observer<DataRepo>() {
                    @Override
                    public void onChanged(@Nullable DataRepo dataRepo) {
                        progressBar.setVisibility(View.INVISIBLE);
                        if (dataRepo != null)
                            setDataRepo(dataRepo);
                    }
                });
    }

    private void setDataRepo(@NonNull DataRepo dataRepo) {
        File file = createFile(dataRepo);
        if (file != null)
            sendByEmail(file);
        else
            Snackbar.make(toolbar, "No hay archivo", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
    }

    private void sendByEmail(@NonNull File file) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("vnd.android.cursor.dir/email");
        emailIntent.putExtra(
                Intent.EXTRA_STREAM,
                FileProvider.getUriForFile(this, "com.nablanet.aula31.fileprovider", file)
        );
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Planilla de Seguimiento");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Envío de planilla de seguimiento...");
        startActivity(Intent.createChooser(emailIntent , "Enviar correo..."));
        finish();
    }

    @Nullable
    private File createFile(@NonNull DataRepo dataRepo) {

        String fileName = getFileName();
        if (fileName == null)
            return null;

        File templateDir = new File(getCacheDir(), "template");
        if (!templateDir.exists() && !templateDir.mkdirs()) {
            Snackbar.make(toolbar, "Error al crear el directorio...", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
            return null;
        }

        File file = new File(templateDir, fileName);

        TemplateODS templateODS = new TemplateODS(file);

        try {
            Body body = templateODS.getBody();
            Spreadsheet spreadsheet = body.getSpreadsheet();
            spreadsheet.setData(
                    dataRepo.getDataSummary(),
                    dataRepo.getDataTrack(),
                    dataRepo.getDataWorkList()
            );
            templateODS.save();
            return file;
        } catch (TransformerException | IOException | XPathExpressionException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Nullable
    private String getFileName() {
        if (courseExport == null) return null;
        return String.format(
                Locale.getDefault(),
                "%s-%s%s.ods",
                courseExport.subjectName, courseExport.subjectGrade, courseExport.classroom
        );
    }

    public void onDateButton(final View view) {

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker v, int year, int month, int dayOfMonth) {
                        Calendar calendar = DateFormat.getDateInstance().getCalendar();
                        calendar.set(
                                year, month, dayOfMonth,
                                (view.getId() == R.id.from_bt) ? 0 : 23,
                                (view.getId() == R.id.from_bt) ? 0 : 59,
                                (view.getId() == R.id.from_bt) ? 0 : 59
                        );
                        if (view.getId() == R.id.from_bt)
                            from = calendar.getTimeInMillis();
                        else
                            to = calendar.getTimeInMillis();

                        Log.d("TAG", "Time from: " + from);
                        Log.d("TAG", "Time to: " + to);
                        ((Button) view).setText(
                                String.format(
                                        Locale.getDefault(),
                                        "%d/%d/%d",
                                        dayOfMonth, month, year
                                )
                        );
                    }
                },
                Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR),
                Calendar.getInstance(Locale.getDefault()).get(Calendar.MONTH),
                Calendar.getInstance(Locale.getDefault()).get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.setTitle("Selecciona el día");
        datePickerDialog.show();
    }

    public void onClickCheckbox(View view) {
        for (MemberCourseExport memberCourseExport : adapter.getMemberList())
            memberCourseExport.checked = ((CheckBox) view).isChecked();
        adapter.notifyDataSetChanged();
    }

}
