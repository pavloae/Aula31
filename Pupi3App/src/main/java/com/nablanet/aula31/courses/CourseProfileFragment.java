package com.nablanet.aula31.courses;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import com.nablanet.aula31.courses.viewmodel.CourseViewModel;
import com.nablanet.aula31.repo.entity.CourseProfile;
import com.nablanet.aula31.repo.entity.Institution;
import com.nablanet.aula31.repo.entity.Subject;
import com.reginald.editspinner.EditSpinner;

import java.util.Calendar;

public class CourseProfileFragment extends Fragment {

    EditSpinner institutionSP, subjectSP, classroomSP, shiftSP;
    NumberPicker gradeNP;

    CourseViewModel viewModel;
    CourseProfile courseProfile;

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
        gradeNP = view.findViewById(R.id.grade_np);
        classroomSP = view.findViewById(R.id.classroom_sp);
        shiftSP = view.findViewById(R.id.shift_sp);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        gradeNP.setMinValue(0);
        gradeNP.setMaxValue(9);
        gradeNP.setValue(0);

        assert getActivity() != null;

        classroomSP.setAdapter(
                new ArrayAdapter<>(
                        getActivity(),
                        android.R.layout.simple_dropdown_item_1line,
                        getResources().getStringArray(R.array.classroom_array)
                )
        );

        shiftSP.setAdapter(
                new ArrayAdapter<>(
                        getActivity(),
                        android.R.layout.simple_dropdown_item_1line,
                        getResources().getStringArray(R.array.shifts_array)
                )
        );

        viewModel = ViewModelProviders.of(getActivity()).get(CourseViewModel.class);
        viewModel.getCourseProfile().observe(getActivity(), new Observer<CourseProfile>() {
            @Override
            public void onChanged(@Nullable CourseProfile courseProfile) {
                CourseProfileFragment.this.courseProfile = courseProfile;
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
        if (TextUtils.isEmpty(viewModel.getCourseId().getValue()))
            menu.findItem(R.id.btn_save).setIcon(android.R.drawable.ic_menu_save);
        else
            menu.findItem(R.id.btn_save).setIcon(android.R.drawable.stat_notify_sync_noanim);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.btn_save) {
            updateValues();
            viewModel.updateCourseProfile(courseProfile);
            if (getActivity() != null)
                getActivity().onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadFragment() {

        institutionSP.setText("");
        subjectSP.setText("");
        gradeNP.setValue(0);
        classroomSP.setText("");
        shiftSP.setText("");

        if (courseProfile == null)
            return;

        institutionSP.setText(
                courseProfile.getInstitution() == null ?
                        null : courseProfile.getInstitution().getName()
        );
        subjectSP.setText(
                courseProfile.getSubject() == null ?
                        null : courseProfile.getSubject().getName()
        );
        gradeNP.setValue(
                courseProfile.getSubject() == null ||
                        courseProfile.getSubject().getGrade() == null ?
                        0 : courseProfile.getSubject().getGrade()
        );
        classroomSP.setText(courseProfile.getClassroom());
        shiftSP.setText(courseProfile.getShift());
    }

    public void updateValues() {
        if (courseProfile == null)
            courseProfile = new CourseProfile();

        if (courseProfile.getInstitution() == null)
            courseProfile.setInstitution(new Institution());
        courseProfile.getInstitution().setName(getText(institutionSP.getText()));

        if (courseProfile.getSubject() == null)
            courseProfile.setSubject(new Subject());
        courseProfile.getSubject().setName(getText(subjectSP.getText()));
        courseProfile.getSubject().setGrade(gradeNP.getValue());

        courseProfile.setClassroom(getText(classroomSP.getText()));
        courseProfile.setYear(Calendar.getInstance().get(Calendar.YEAR));
        courseProfile.setOwner(FirebaseAuth.getInstance().getUid());
        courseProfile.setShift(getText(shiftSP.getText()));
    }

    private String getText(Editable editable) {
        return (TextUtils.isEmpty(editable.toString())) ? null : editable.toString();
    }

}
