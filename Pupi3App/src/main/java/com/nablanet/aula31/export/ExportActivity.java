package com.nablanet.aula31.export;

import android.app.DatePickerDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nablanet.aula31.R;
import com.nablanet.aula31.export.data.DataParams;
import com.nablanet.aula31.export.data.DataRepo;
import com.nablanet.aula31.export.entity.CourseExt;
import com.nablanet.aula31.export.entity.MemberExt;
import com.nablanet.aula31.export.view.MemberAdapter;
import com.nablanet.aula31.export.viewmodel.CourseViewModel;
import com.nablanet.aula31.export.viewmodel.RepoViewModel;
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

    public static final String COURSE_ID_KEY = "course_id_key";

    ProgressBar progressBar;

    TextView institutionTV, subjectTV;
    RecyclerView recyclerView;

    MemberAdapter adapter;

    DataParams dataParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dataParams = new DataParams();
        if (getIntent().getExtras() != null)
            dataParams.setCourseId(getIntent().getExtras().getString(COURSE_ID_KEY));

        progressBar = findViewById(R.id.progressBar);

        institutionTV = findViewById(R.id.institution_tv);
        subjectTV = findViewById(R.id.subject_tv);

        recyclerView = findViewById(R.id.members_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MemberAdapter(new ArrayList<MemberExt>(), R.layout.item_list_export);
        recyclerView.setAdapter(adapter);

        if (dataParams.getCourseId() != null)
            bindViewModel();
        else
            finish();

    }

    private void bindViewModel() {

        progressBar.setVisibility(View.VISIBLE);

        ViewModelProviders.of(this).get(CourseViewModel.class)
                .getCourseExtLiveData(dataParams.getCourseId())
                .observe(this, new Observer<CourseExt>() {
                    @Override
                    public void onChanged(@Nullable CourseExt courseExt) {
                        progressBar.setVisibility(View.INVISIBLE);
                        dataParams.setCourseExt(courseExt);
                        if (courseExt != null) {
                            institutionTV.setText(courseExt.getInstitutionName());
                            subjectTV.setText(courseExt.getSubjectFullName());
                            adapter.updateMembers(courseExt.getMemberExtList());
                        } else {
                            institutionTV.setText("");
                            subjectTV.setText("");
                            adapter.updateMembers(new ArrayList<MemberExt>());
                        }
                    }
                });

        ViewModelProviders.of(this).get(RepoViewModel.class)
                .getResponseLive()
                .observe(this, new Observer<Response>() {
                    @Override
                    public void onChanged(@Nullable Response response) {
                        progressBar.setVisibility(View.INVISIBLE);
                        if (response != null)
                            Toast.makeText(
                                    ExportActivity.this,
                                    response.getMessage(),
                                    Toast.LENGTH_SHORT
                            ).show();
                    }
                });

    }

    public void queryDataReport(View view) {

        if (dataParams.getCourseExt() == null) {
            Toast.makeText(
                    this, "No hay datos del curso...", Toast.LENGTH_SHORT
            ).show();
            return;
        }

        dataParams.setMemberIdList(adapter.getMemberKeyList(true));

        progressBar.setVisibility(View.VISIBLE);

        ViewModelProviders.of(this).get(RepoViewModel.class).getDataRepoLive(dataParams)
                .observe(this, new Observer<DataRepo>() {
            @Override
            public void onChanged(@Nullable DataRepo dataRepo) {
                progressBar.setVisibility(View.INVISIBLE);
                if (dataRepo != null)
                    setDataRepo(dataRepo);
                else
                    Toast.makeText(
                            ExportActivity.this,
                            "No hay datos para la planilla...",
                            Toast.LENGTH_SHORT
                    ).show();
            }
        });
    }

    private void setDataRepo(@NonNull DataRepo dataRepo) {
        File file = createFile(dataRepo);
        if (file != null)
            sendByEmail(file);
        else
            Toast.makeText(
                    this, "No hay archivo", Toast.LENGTH_SHORT
            ).show();
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
            Toast.makeText(
                    this, "Error al crear el directorio...", Toast.LENGTH_SHORT
            ).show();
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
        if (dataParams.getCourseExt() == null) return null;
        return String.format(
                Locale.getDefault(),
                "%s-%s%s.ods",
                dataParams.getCourseExt().getSubjectName(),
                dataParams.getCourseExt().getSubjectGrade(),
                dataParams.getCourseExt().getClassroom()
        );
    }

    public void onDateButton(final View view) {

        final DatePickerDialog.OnDateSetListener dateSetListener;
        String title;

        switch (view.getId()) {
            case R.id.from_bt:

                title = getResources().getString(R.string.title_from);

                dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker v, int year, int month, int dayOfMonth) {
                        Calendar calendar = DateFormat.getDateInstance().getCalendar();
                        calendar.set(year, month, dayOfMonth,0, 0, 0);
                        dataParams.setFrom(calendar.getTimeInMillis());
                        ((Button) view).setText(
                                String.format(
                                        Locale.getDefault(),
                                        "%d/%d/%d", dayOfMonth, month + 1, year
                                )
                        );
                    }
                };

                break;

            case R.id.to_bt:

                title = getResources().getString(R.string.title_to);

                dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker v, int year, int month, int dayOfMonth) {
                        Calendar calendar = DateFormat.getDateInstance().getCalendar();
                        calendar.set(year, month, dayOfMonth, 23, 59, 59);
                        dataParams.setTo(calendar.getTimeInMillis());
                        ((Button) view).setText(
                                String.format(
                                        Locale.getDefault(),
                                        "%d/%d/%d", dayOfMonth, month + 1, year
                                )
                        );
                    }
                };

                break;

                default:
                return;
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                dateSetListener,
                Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR),
                Calendar.getInstance(Locale.getDefault()).get(Calendar.MONTH),
                Calendar.getInstance(Locale.getDefault()).get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.setTitle(title);
        datePickerDialog.show();
    }

    public void onClickCheckbox(View view) {
        for (MemberExt memberExt : adapter.getMemberList())
            memberExt.checked = ((CheckBox) view).isChecked();
        adapter.notifyDataSetChanged();
    }

}
