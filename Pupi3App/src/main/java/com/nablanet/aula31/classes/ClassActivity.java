package com.nablanet.aula31.classes;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nablanet.aula31.R;
import com.nablanet.aula31.classes.entity.MemberItem;
import com.nablanet.aula31.classes.view.AddMemberDialog;
import com.nablanet.aula31.classes.view.ClassRecyclerView;
import com.nablanet.aula31.classes.viewmodel.ClassViewModel;
import com.nablanet.aula31.export.ExportActivity;
import com.nablanet.aula31.repo.Response;
import com.nablanet.aula31.repo.Utils;
import com.nablanet.aula31.repo.entity.Attendance;
import com.nablanet.aula31.repo.entity.ClassDay;
import com.nablanet.aula31.repo.entity.CourseProfile;
import com.nablanet.aula31.tracking.MemberTrackActivity;
import com.nablanet.aula31.utils.Util;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ClassActivity extends AppCompatActivity implements View.OnClickListener, OnMemberListener {

    public static final String TAG = "ClassActivity";

    public static final String COURSE_ID_KEY = "course_id_key";
    public static final String LAYOUT_TYPE_KEY = "layout_type_key";

    Toolbar toolbar;

    // Top Member View
    RelativeLayout memberBar;
    TextView lastnameTV, namesTV;
    ImageButton absentIB;

    // Main Class View
    ClassRecyclerView recyclerView;

    // Bottom Day View
    TextView dateTV;

    ClassViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);

        toolbar = findViewById(R.id.toolbar_class);
        setSupportActionBar(toolbar);

        memberBar = findViewById(R.id.dinamic_member_bar);
        lastnameTV = findViewById(R.id.lastname_tv);
        namesTV = findViewById(R.id.names_tv);
        absentIB = findViewById(R.id.absent_member_bar_ib);
        recyclerView = findViewById(R.id.members_class_rv);
        dateTV = findViewById(R.id.date_tv);

        setLayoutType(
                (savedInstanceState == null) ?
                ClassRecyclerView.LIST : savedInstanceState.getInt(LAYOUT_TYPE_KEY)
        );

        init();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.class_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.view_button:
                setLayoutType(
                        (recyclerView.getLayoutType() == ClassRecyclerView.LIST) ?
                                ClassRecyclerView.GRID : ClassRecyclerView.LIST
                );
                setMenuIcon(item);
                break;
            case R.id.new_member:
                new AddMemberDialog().show(getSupportFragmentManager(), null);
                break;
            case R.id.export:
                Intent intent = new Intent(this, ExportActivity.class);
                intent.putExtra(ExportActivity.COURSE_ID_KEY, viewModel.getCourseId().getValue());
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(LAYOUT_TYPE_KEY, recyclerView.getLayoutType());
        super.onSaveInstanceState(outState);
    }

    private void setLayoutType(int layoutType) {
        recyclerView.setLayoutType(layoutType);
        // Si estamos en modo Lista no necesitamos el View memberBar superior.
        memberBar.setVisibility(
                (recyclerView.getLayoutType() == ClassRecyclerView.LIST) ?
                        View.GONE : View.VISIBLE
        );
    }

    private void init() {

        String courseId = getIntent().getStringExtra(COURSE_ID_KEY);

        if (courseId == null) {
            finish();
            return;
        }

        viewModel = ViewModelProviders.of(this).get(ClassViewModel.class);

        viewModel.getResponse().observe(this, new Observer<Response>() {
            @Override
            public void onChanged(@Nullable Response response) {
                if (response != null && !response.success)
                    Toast.makeText(
                            ClassActivity.this, response.getMessage(), Toast.LENGTH_SHORT
                    ).show();

            }
        });

        // Configuramos el curso
        viewModel.setCourseId(courseId);

        // Mantenemos actualizados los datos del curso
        viewModel.getCourseProfileLive().observe(this, new Observer<CourseProfile>() {
            @Override
            public void onChanged(@Nullable CourseProfile courseProfile) {
                Log.d(TAG, "init() - CourseProfileLive(): " +
                        ((courseProfile == null) ? "null" : courseProfile.getOwner()));
                toolbar.setTitle(
                        courseProfile == null ? null : Utils.getCourseName(courseProfile)
                );
                toolbar.setSubtitle(
                        courseProfile == null || courseProfile.getInstitution() == null ?
                                null : courseProfile.getInstitution().getName()
                );
            }
        });

        // Mantenemos actualizada la lista de Miembros de la clase activa
        viewModel.getMergedMemberList().observe(this, new Observer<List<MemberItem>>() {
            @Override
            public void onChanged(@Nullable List<MemberItem> memberItems) {

                recyclerView.updateList(memberItems);

            }
        });

        // Mantenemos actualizado el Miembro activo
        viewModel.getCurrentMember().observe(this, new Observer<MemberItem>() {

            @Override
            public void onChanged(@Nullable MemberItem memberItem) {

                recyclerView.setCurrentMember(memberItem);

                if (memberItem == null) {

                    lastnameTV.setText("");
                    namesTV.setText("");
                    absentIB.setImageDrawable(getResources().getDrawable(R.drawable.ic_absent_gray));

                } else {

                    lastnameTV.setText(memberItem.getLastname());
                    namesTV.setText(memberItem.getNames());

                    Attendance attendance = memberItem.getAttendance();

                    if (attendance == null || attendance.getPresent() == null)
                        absentIB.setImageDrawable(
                                getResources().getDrawable(R.drawable.ic_absent_gray)
                        );

                    else if (attendance.getPresent())
                        absentIB.setImageDrawable(
                                getResources().getDrawable(R.drawable.ic_present_green)
                        );

                    else
                        absentIB.setImageDrawable(
                                getResources().getDrawable(R.drawable.ic_absent_red)
                        );
                }

            }
        });

        // Mantenemos actualizados los datos de la Clase activa
        viewModel.getCurrentClassDay().observe(this, new Observer<ClassDay>() {
            @Override
            public void onChanged(@Nullable ClassDay classDay) {

                dateTV.setText(
                        (classDay == null || classDay.getDate() == null) ?
                                "" : Util.getStringDate(classDay.getDate()));
            }
        });
    }

    @Override
    public void onClick(View v) {
        MemberItem currentMember = viewModel.getCurrentMember().getValue();
        switch (v.getId()) {

            // Boton de asistencia de la vista superior.
            // Al tomar asistencia desde acá eliminamos la posición en el aula
            case R.id.absent_member_bar_ib:
                if (currentMember == null)
                    return;
                currentMember.setPosition(null);
                currentMember.setPresent(
                        currentMember.getPresent() == null || !currentMember.getPresent()
                );
                viewModel.updateAttendance(currentMember);
                break;

            // Vista del perfil del usuario en la vista superior
            case R.id.member_rl:
                onMemberTrack(currentMember);
                break;

        }
    }

    @Override
    public void onItemClick(@NonNull MemberItem memberItem) {

        switch (recyclerView.getLayoutType()) {

            case ClassRecyclerView.LIST:

                viewModel.updateAttendance(memberItem);

                viewModel.setCurrentMember(memberItem);

                break;

            case ClassRecyclerView.GRID:

                MemberItem currentMember = viewModel.getCurrentMember().getValue();

                // Si fue seleccionado un pupítre vacío
                if (memberItem.isNew()) {

                    if (currentMember == null)
                        return;

                    currentMember.setPosition(memberItem.getPosition());
                    currentMember.setPresent(true);

                    viewModel.setNextMember();

                    viewModel.updateAttendance(currentMember);

                }

                // Si fue seleccionado un pupitre con un miembro, lo marcamos como actual
                else {

                    viewModel.setCurrentMember(memberItem);

                }

                break;

                default:

        }

    }

    @Override
    public boolean onItemLongClick(@NonNull final MemberItem memberItem) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(memberItem.getLastname());
        builder.setItems(
                new String[]{"Borrar de la clase", "Borrar del curso"},
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                viewModel.deleteFromClass(memberItem);
                                break;
                            case 1:
                                viewModel.deleteFromCourse(memberItem);
                                break;
                                default:
                        }

                    }
                }
        );
        builder.show();
        return true;
    }

    @Override
    public void onMemberTrack(@NonNull MemberItem memberItem) {

        viewModel.setCurrentMember(memberItem);

        Intent intent = new Intent(this, MemberTrackActivity.class);

        ClassDay classDay = viewModel.getCurrentClassDay().getValue();
        if (memberItem.getKey() != null && classDay != null && classDay.getKey() != null) {
            Bundle bundle = new Bundle();
            bundle.putString(MemberTrackActivity.MEMBER_ID_KEY, memberItem.getKey());
            bundle.putString(MemberTrackActivity.CLASS_ID_KEY, classDay.getKey());
            intent.putExtras(bundle);
            startActivity(intent);
        } else
            Toast.makeText(this, "Seleccione un día", Toast.LENGTH_SHORT).show();



    }

    private void setMenuIcon(MenuItem item) {
        if (recyclerView != null && recyclerView.getLayoutType() == ClassRecyclerView.GRID)
            item.setIcon(getResources().getDrawable(R.drawable.ic_list_white_24dp));
        else
            item.setIcon(getResources().getDrawable(R.drawable.ic_grid_white_24dp));
    }

    public void onPreviousMember(@Nullable View view) {
        viewModel.setPreviousMember();
    }

    public void onNextMember(@Nullable View view) {
        viewModel.setNextMember();
    }

    public void onPreviousClass(@Nullable View view) {
        viewModel.setPreviousClass();
    }

    public void onNextClass(@Nullable View view) {
        viewModel.setNextClass();
    }

    public void onCalendar(@Nullable View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        viewModel.onDateSelected(year, month, dayOfMonth);
                    }
                },
                Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR),
                Calendar.getInstance(Locale.getDefault()).get(Calendar.MONTH),
                Calendar.getInstance(Locale.getDefault()).get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.setTitle("Selecciona el día");
        datePickerDialog.show();
    }

}
