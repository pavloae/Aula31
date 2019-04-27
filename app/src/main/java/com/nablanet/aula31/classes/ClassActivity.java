package com.nablanet.aula31.classes;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.nablanet.aula31.R;
import com.nablanet.aula31.courses.Course;
import com.nablanet.aula31.tracking.MemberTrackActivity;
import com.nablanet.aula31.utils.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class ClassActivity extends AppCompatActivity implements View.OnClickListener, OnMemberListener {

    public static final String COURSE_ID_KEY = "course_id_key";
    public static final String MEMBER_ID_KEY = "member_id_key";
    public static final String SUBJECT_KEY = "subject_key";
    public static final String INSTITUTE_KEY = "institute_key";

    public static final String LAYOUT_TYPE_KEY = "layout_type_key";

    int layoutType;
    Toolbar toolbar;
    TextView dateTV, lastnameTV, namesTV;
    ClassViewModel classViewModel;
    RelativeLayout memberBar;
    ClassRecyclerView recyclerView;
    List<ClassDay.Member> memberList;
    ClassDay.Member currentMember;
    ImageButton absentIB;
    Course course;
    ClassDay classDay;

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

        layoutType = (savedInstanceState == null) ?
                ClassRecyclerView.LIST : savedInstanceState.getInt(LAYOUT_TYPE_KEY);

        setLayoutType();

        setViewModel();

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
                layoutType = (recyclerView.getLayoutType() == ClassRecyclerView.LIST) ?
                        ClassRecyclerView.GRID : ClassRecyclerView.LIST;
                setLayoutType();
                setMenuIcon(item);
                break;
            case R.id.new_class:
                launchCalendar();
                break;
            case R.id.new_member:
                new AddMemberDialog().show(getSupportFragmentManager(), null);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(LAYOUT_TYPE_KEY, recyclerView.getLayoutType());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_day_button:
                classViewModel.setPrevClass();
                break;
            case R.id.forward_day_button:
                classViewModel.setNextClass();
                break;
            case R.id.date_tv:
                launchCalendar();
                break;
            case R.id.absent_member_bar_ib:
                currentMember.present = currentMember.present == null || !currentMember.present;
                currentMember.position = null;
                classViewModel.updateMemberClass(currentMember);
                break;
        }
    }

    @Override
    public void onItemClick(ClassDay.Member member) {
        if (recyclerView.getLayoutType() == ClassRecyclerView.LIST) {
            classViewModel.updateMemberClass(member);
            onNextMember(null);
        } else if (member.isNew()) {
            currentMember.position = member.position;
            currentMember.present = true;
            classViewModel.updateMemberClass(currentMember);
            onNextMember(null);
        } else{
            member.position = null;
            currentMember = member;
            currentMember.present = false;
            setCurrentMemberToView();
            classViewModel.updateMemberClass(currentMember);
        }
    }

    @Override
    public boolean onItemLongClick(final ClassDay.Member member) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(member.lastname);
        builder.setItems(
                new String[]{"Borrar de la clase", "Borrar del curso"},
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                classViewModel.deleteMemberClass(member);
                                break;
                            case 1:
                                classViewModel.deleteMemberFromCourse(course.id, member.id);
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
    public void onMemberTrack(ClassDay.Member member) {

        if (course == null || TextUtils.isEmpty(course.id) || TextUtils.isEmpty(member.id)
                || classDay == null || TextUtils.isEmpty(classDay.id))
            return;

        this.currentMember = member;
        setCurrentMemberToView();

        Bundle bundle = new Bundle();
        bundle.putString(MemberTrackActivity.COURSE_ID_KEY, course.id);
        bundle.putString(MemberTrackActivity.MEMBER_ID_KEY, member.id);
        bundle.putString(MemberTrackActivity.LASTNAME_KEY, member.lastname);
        bundle.putString(MemberTrackActivity.NAMES_KEY, member.names);
        bundle.putString(MemberTrackActivity.CLASS_ID_KEY, classDay.id);

        startActivity(new Intent(this, MemberTrackActivity.class).putExtras(bundle));

    }

    private void setViewModel() {
        classViewModel = ViewModelProviders.of(this).get(ClassViewModel.class);
        classViewModel.getCourseLiveData(getIntent().getStringExtra(COURSE_ID_KEY)).observe(
                this, new Observer<Course>() {
                    @Override
                    public void onChanged(@Nullable Course course) {
                        if (course != null) setCourse(course);
                    }
                }
        );
        classViewModel.getClassLiveData().observe(this, new Observer<ClassDay>() {
            @Override
            public void onChanged(@Nullable ClassDay classDay) {
                if (classDay != null) setClassDay(classDay);
            }
        });
        classViewModel.getDatabaseError().observe(this, new Observer<DatabaseError>() {
            @Override
            public void onChanged(@Nullable DatabaseError databaseError) {
                if (databaseError != null)
                    Snackbar.make(toolbar, databaseError.getMessage(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
            }
        });
        classViewModel.getSuccessMutableLiveData().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                if (integer == null) return;
                String message;
                switch (integer) {
                    case ClassViewModel.MEMBER_CREATED:
                        message = "Miembro creado";
                        break;
                    case ClassViewModel.MEMBER_EDITED:
                        message = "Miembro cambiado";
                        break;
                        default:
                            message = "";

                }
                Toast.makeText(ClassActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
        classViewModel.getDbResultLiveData().observe(this, new Observer<DBResult>() {
            @Override
            public void onChanged(@Nullable DBResult dbResult) {
                if (dbResult != null)
                    Toast.makeText(
                            ClassActivity.this, dbResult.message, Toast.LENGTH_SHORT
                    ).show();
            }
        });
    }

    private void setCourse(@NonNull Course course) {
        this.course = course;
        toolbar.setTitle(course.profile.getCourseName());
        toolbar.setSubtitle(course.profile.getInstitutionName());
    }

    private void setClassDay(@NonNull ClassDay classDay) {
        this.classDay = classDay;
        dateTV.setText(Util.getStringDate(classDay.date));

        if (course == null) return;

        classDay.prepareMembers(course.members);
        memberList = new ArrayList<>(classDay.members.values());
        if (memberList.size() == 0) {
            currentMember = new ClassDay.Member();
            setCurrentMemberToView();
            return;
        } else {
            // Ordenamos la lista por "Apellido, Nombres" de A-Z
            Collections.sort(
                    memberList, Collections.reverseOrder(new Comparator<ClassDay.Member>() {
                        @Override
                        public int compare(ClassDay.Member o1, ClassDay.Member o2) {
                            return o2.getFullName().compareTo(o1.getFullName());
                        }
                    })
            );
        }

        recyclerView.updateList(memberList);

        if (currentMember == null){
            currentMember = memberList.get(0);
            setCurrentMemberToView();
            return;
        }

        for (ClassDay.Member member : memberList)
            if (member.id.equals(currentMember.id)){
                currentMember = member;
                break;
            }

        if (currentMember.isNew())
            currentMember = memberList.get(0);

        setCurrentMemberToView();
    }

    private void setLayoutType() {
        recyclerView.setLayoutType(layoutType);
        setMemberBarVisibility();
    }

    private void setMemberBarVisibility() {
        memberBar.setVisibility(
                (recyclerView.getLayoutType() == ClassRecyclerView.LIST) ? View.GONE : View.VISIBLE
        );
    }

    private void setMenuIcon(MenuItem item) {
        if (recyclerView != null && recyclerView.getLayoutType() == ClassRecyclerView.GRID)
            item.setIcon(getResources().getDrawable(R.drawable.ic_list_white_24dp));
        else
            item.setIcon(getResources().getDrawable(R.drawable.ic_grid_white_24dp));
    }

    private void launchCalendar() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        ClassDay classDay = new ClassDay();
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, dayOfMonth);
                        classDay.date = calendar.getTimeInMillis();
                        classViewModel.saveNewClassDay(classDay);
                    }
                },
                Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR),
                Calendar.getInstance(Locale.getDefault()).get(Calendar.MONTH),
                Calendar.getInstance(Locale.getDefault()).get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.setTitle("Selecciona el día");
        datePickerDialog.show();
    }

    public void onPrevMember(@Nullable View view) {
        if (memberList == null || memberList.size() == 0)
            return;
        if (currentMember == null)
            currentMember = memberList.get(0);

        currentMember = memberList.get(
                (memberList.indexOf(currentMember) == 0) ?
                        memberList.size() - 1 : memberList.indexOf(currentMember) - 1
        );
        setCurrentMemberToView();
    }

    public void onNextMember(@Nullable View view) {
        if (memberList == null || memberList.size() == 0)
            return;
        if (currentMember == null)
            currentMember = memberList.get(0);
        currentMember = memberList.get(
                (memberList.indexOf(currentMember) == memberList.size() - 1) ?
                        0 : memberList.indexOf(currentMember) + 1
        );
        setCurrentMemberToView();
    }

    private void setCurrentMemberToView() {
        if (currentMember == null) return;
        lastnameTV.setText(currentMember.lastname);
        namesTV.setText(currentMember.names);
        if (currentMember.present == null)
            absentIB.setImageDrawable(getResources().getDrawable(R.drawable.ic_absent_gray));
        else if (!currentMember.present)
            absentIB.setImageDrawable(getResources().getDrawable(R.drawable.ic_absent_red));
        else
            absentIB.setImageDrawable(getResources().getDrawable(R.drawable.ic_present_green));
    }

}
