package com.nablanet.aula31.courses;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;

import com.google.firebase.auth.FirebaseAuth;
import com.nablanet.aula31.R;
import com.nablanet.aula31.courses.entity.CourseProfileExt;
import com.reginald.editspinner.EditSpinner;

import java.util.Calendar;

public class CourseProfileFragment extends Fragment {

    EditSpinner institutionSP, subjectSP, classroomSP, shiftSP;
    NumberPicker gradeNP;

    CourseViewModel viewModel;
    CourseProfileExt courseProfileExt;

    public CourseProfileFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_course, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        institutionSP = view.findViewById(R.id.institution_spinner);
        subjectSP = view.findViewById(R.id.subject_spinner);
        classroomSP = view.findViewById(R.id.classroom_sp);
        shiftSP = view.findViewById(R.id.shift_sp);
        gradeNP = view.findViewById(R.id.grade_np);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        gradeNP.setMinValue(0);
        gradeNP.setMaxValue(9);
        gradeNP.setValue(0);

        classroomSP.setAdapter(
                new ArrayAdapter<>(
                        getContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        getResources().getStringArray(R.array.classroom_array)
                )
        );

        shiftSP.setAdapter(
                new ArrayAdapter<>(
                        getContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        getResources().getStringArray(R.array.shifts_array)
                )
        );

        if (getActivity() == null)
            return;

        viewModel = ViewModelProviders.of(getActivity()).get(CourseViewModel.class);
        viewModel.getSelectedCourseProfile().observe(getActivity(), new Observer<CourseProfileExt>() {
            @Override
            public void onChanged(@Nullable CourseProfileExt courseProfile) {
                CourseProfileFragment.this.courseProfileExt = courseProfile;
                loadFragment();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.course_profile_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (TextUtils.isEmpty(courseProfileExt.getKey()))
            menu.findItem(R.id.btn_save).setIcon(android.R.drawable.ic_menu_save);
        else
            menu.findItem(R.id.btn_save).setIcon(android.R.drawable.stat_notify_sync_noanim);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btn_save:
                updateValues();
                if (TextUtils.isEmpty(courseProfileExt.getKey()))
                    viewModel.saveCourse(courseProfileExt);
                else
                    viewModel.updateCourseProfile(courseProfileExt.getKey(), courseProfileExt);
                break;
                // TODO: futere options
        }
        return super.onOptionsItemSelected(item);
    }

    private void cleanFragment() {
        institutionSP.setText("");
        subjectSP.setText("");
        gradeNP.setValue(0);
        classroomSP.setText("");
        shiftSP.setText("");
    }

    private void loadFragment() {
        cleanFragment();
        if (courseProfileExt == null) return;
        institutionSP.setText(courseProfileExt.getInstitutionName());
        subjectSP.setText(courseProfileExt.getSubjectName());
        gradeNP.setValue(courseProfileExt.getSubjectGrade());
        classroomSP.setText(courseProfileExt.getClassroom());
        shiftSP.setText(courseProfileExt.getShift());
    }

    public void updateValues() {
        if (courseProfileExt == null) courseProfileExt = new CourseProfileExt();
        courseProfileExt.setInstitutionName(getText(institutionSP.getText()));
        courseProfileExt.setSubjectName(getText(subjectSP.getText()));
        courseProfileExt.setSubjectGrade(gradeNP.getValue());
        courseProfileExt.setClassroom(getText(classroomSP.getText()));
        courseProfileExt.setYear(Calendar.getInstance().get(Calendar.YEAR));
        courseProfileExt.setOwner(FirebaseAuth.getInstance().getUid());
        courseProfileExt.setShift(getText(shiftSP.getText()));
    }

    private String getText(Editable editable) {
        return (TextUtils.isEmpty(editable.toString())) ? null : editable.toString();
    }

}
