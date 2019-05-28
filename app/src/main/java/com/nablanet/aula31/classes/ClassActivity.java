package com.nablanet.aula31.classes;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nablanet.aula31.R;
import com.nablanet.aula31.classes.entity.MemberItem;
import com.nablanet.aula31.classes.view.ClassRecyclerView;
import com.nablanet.aula31.export.ExportActivity;
import com.nablanet.aula31.repo.Response;
import com.nablanet.aula31.repo.entity.ClassDay;
import com.nablanet.aula31.tracking.MemberTrackActivity;
import com.nablanet.aula31.utils.Util;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ClassActivity extends AppCompatActivity implements View.OnClickListener, OnMemberListener {

    public static final String COURSE_ID_KEY = "course_id_key";
    //public static final String MEMBER_ID_KEY = "member_id_key";
    public static final String SUBJECT_KEY = "subject_key";
    public static final String INSTITUTE_KEY = "institute_key";

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

    ClassViewModel classViewModel;
    @Nullable MemberItem currentMember;

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

        toolbar.setTitle(getIntent().getStringExtra(SUBJECT_KEY));
        toolbar.setSubtitle(getIntent().getStringExtra(INSTITUTE_KEY));

        setLayoutType(
                (savedInstanceState == null) ?
                ClassRecyclerView.LIST : savedInstanceState.getInt(LAYOUT_TYPE_KEY)
        );

        bindViewModel();

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
            case R.id.new_class:
                onCalendar(null);
                break;
            case R.id.new_member:
                new AddMemberDialog().show(getSupportFragmentManager(), null);
                break;
            case R.id.export:
                Intent intent = new Intent(this, ExportActivity.class);
                intent.putExtra(COURSE_ID_KEY, classViewModel.getCourseIdLiveData().getValue());
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

    private void bindViewModel() {
        classViewModel = ViewModelProviders.of(this).get(ClassViewModel.class);

        //Escuchamos las respuestas
        classViewModel.getResponseLiveData().observe(this, new Observer<Response>() {
            @Override
            public void onChanged(@Nullable Response response) {
                if (response != null)
                    Snackbar.make(toolbar, response.getMessage(), Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
            }
        });

        // Configuramos el curso
        classViewModel.setCourseId(getIntent().getStringExtra(COURSE_ID_KEY));

        // Mantenemos actualizada la lista de Miembros de la clase activa
        classViewModel.getMemberItemList().observe(this, new Observer<List<MemberItem>>() {
            @Override
            public void onChanged(@Nullable List<MemberItem> memberItems) {
                recyclerView.updateList(memberItems);
            }
        });

        // Mantenemos actualizado el Miembro activo
        classViewModel.getMemberItem().observe(this, new Observer<MemberItem>() {
            @Override
            public void onChanged(@Nullable MemberItem memberItem) {
                setCurrentMember(memberItem);
            }
        });

        classViewModel.getClassDayList().observe(this, new Observer<List<ClassDay>>() {
            @Override
            public void onChanged(@Nullable List<ClassDay> classDays) {
                classViewModel.onClassDayListUpdated(classDays);
            }
        });

        // Mantenemos actualizados los datos de la Clase activa
        classViewModel.getClassDay().observe(this, new Observer<ClassDay>() {
            @Override
            public void onChanged(@Nullable ClassDay classDay) {
                dateTV.setText(
                        (classDay == null) ? "" : Util.getStringDate(classDay.getDate()));
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            // Boton de asistencia de la vista superior.
            // Al tomar asistencia desde acá eliminamos la posición en el aula
            case R.id.absent_member_bar_ib:
                if (currentMember == null) return;
                currentMember.setPosition(null);
                currentMember.setPresent(
                        currentMember.getPresent() == null || !currentMember.getPresent()
                );
                classViewModel.updateAttendance(currentMember);
                break;

            // Vista del perfil del usuario en la vista superior
            case R.id.member_rl:
                onMemberTrack(currentMember);
                break;

        }
    }

    @Override
    public void onItemClick(MemberItem memberItem) {

        // Si estamos en el modo lista: fue cambiada la asistencia del miembro a la clase.
        // Lo actualizamos en la base de datos y lo configuramos como miembro actual
        if (recyclerView.getLayoutType() == ClassRecyclerView.LIST) {
            classViewModel.updateAttendance(memberItem);
            classViewModel.setMemberItem(memberItem);
        }

        // Si estamos en el modo Pupitres y fue seleccionado un pupitre vacío
        else if (memberItem.isNew()) {
            if (currentMember == null) return;
            currentMember.setPosition(memberItem.getPosition());
            currentMember.setPresent(true);
            classViewModel.updateAttendance(memberItem);
            onNextMember(null);
        }

        // Si fue seleccionado un pupitre con un miembro, lo marcamos como actual
        else {
            classViewModel.setMemberItem(memberItem);
        }
    }

    @Override
    public boolean onItemLongClick(final MemberItem memberItem) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(memberItem.lastname);
        builder.setItems(
                new String[]{"Borrar de la clase", "Borrar del curso"},
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                classViewModel.deleteFromClass(memberItem);
                                break;
                            case 1:
                                classViewModel.deleteFromCourse(memberItem);
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
    public void onMemberTrack(MemberItem memberItem) {

        classViewModel.setMemberItem(memberItem);

        Bundle bundle = new Bundle();
        bundle.putString(MemberTrackActivity.COURSE_ID_KEY, classViewModel.getCourseIdLiveData().getValue());
        bundle.putString(MemberTrackActivity.MEMBER_ID_KEY, memberItem.memberId);
        bundle.putString(MemberTrackActivity.LASTNAME_KEY, memberItem.lastname);
        bundle.putString(MemberTrackActivity.NAMES_KEY, memberItem.names);
        ClassDay classDay = classViewModel.getClassDay().getValue();
        if (classDay != null)
            bundle.putString(MemberTrackActivity.CLASS_ID_KEY, classDay.class_id);

        startActivity(new Intent(this, MemberTrackActivity.class).putExtras(bundle));

    }

    private void setLayoutType(int layoutType) {
        recyclerView.setLayoutType(layoutType);
        // Si estamos en modo Lista no necesitamos el View memberBar superior.
        memberBar.setVisibility(
                (recyclerView.getLayoutType() == ClassRecyclerView.LIST) ?
                        View.GONE : View.VISIBLE
        );
    }

    private void setMenuIcon(MenuItem item) {
        if (recyclerView != null && recyclerView.getLayoutType() == ClassRecyclerView.GRID)
            item.setIcon(getResources().getDrawable(R.drawable.ic_list_white_24dp));
        else
            item.setIcon(getResources().getDrawable(R.drawable.ic_grid_white_24dp));
    }

    public void onPreviousMember(@Nullable View view) {
        classViewModel.setPreviousMember();
    }

    public void onNextMember(@Nullable View view) {
        classViewModel.setNextMember();
    }

    public void onPreviousClass(@Nullable View view) {
        classViewModel.setPreviousClass();
    }

    public void onNextClass(@Nullable View view) {
        classViewModel.setNextClass();
    }

    public void onCalendar(@Nullable View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        classViewModel.createClassDay(year, month, dayOfMonth);
                    }
                },
                Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR),
                Calendar.getInstance(Locale.getDefault()).get(Calendar.MONTH),
                Calendar.getInstance(Locale.getDefault()).get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.setTitle("Selecciona el día");
        datePickerDialog.show();
    }

    private void setCurrentMember(MemberItem memberItem) {

        this.currentMember = memberItem;

        if (memberItem == null) {
            lastnameTV.setText("");
            namesTV.setText("");
            absentIB.setImageDrawable(getResources().getDrawable(R.drawable.ic_absent_gray));
            return;
        }

        lastnameTV.setText(memberItem.lastname);
        namesTV.setText(memberItem.names);

        if (memberItem.getAttendance() == null || memberItem.getAttendance().getPresent() == null)
            absentIB.setImageDrawable(getResources().getDrawable(R.drawable.ic_absent_gray));
        else if (memberItem.getAttendance().getPresent())
            absentIB.setImageDrawable(getResources().getDrawable(R.drawable.ic_present_green));
        else
            absentIB.setImageDrawable(getResources().getDrawable(R.drawable.ic_absent_red));
    }

}
