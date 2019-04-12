package com.nablanet.aula31.courses;

import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nablanet.aula31.OnItemListener;
import com.nablanet.aula31.classes.ClassActivity;
import com.nablanet.aula31.R;

import java.util.ArrayList;
import java.util.List;

public class CoursesListFragment extends Fragment implements OnItemListener<Membership> {

    CourseViewModel courseViewModel;

    RecyclerView recyclerView;

    public CoursesListFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_course, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.courses_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new CoursesAdapter(new ArrayList<Membership>(), this));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        courseViewModel = ViewModelProviders.of(getActivity()).get(CourseViewModel.class);
        courseViewModel.getMemberships().observe(this, new Observer<List<Membership>>() {
            @Override
            public void onChanged(@Nullable List<Membership> memberships) {
                if (memberships == null) memberships = new ArrayList<>();
                ((CoursesAdapter) recyclerView.getAdapter()).updateList(memberships);
            }
        });
    }

    @Override
    public void onItemClick(Membership membership) {
        Intent intent = new Intent(getActivity(), ClassActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ClassActivity.COURSE_ID_KEY, membership.course_id);
        bundle.putString(ClassActivity.MEMBER_ID_KEY, membership.id);
        bundle.putString(ClassActivity.SUBJECT_KEY, membership.course_name);
        bundle.putString(ClassActivity.INSTITUTE_KEY, membership.institution_name);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(final Membership membership) {
        String[] options = (membership.role == Membership.TEACHER) ?
                new String[]{
                        "Abandonar",
                        "Editar"
        }
                : new String[] {
                        "Abandonar"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Curso");
        builder.setItems(
                options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                courseViewModel.leaveCourse(membership);
                                break;
                            case 1:
                                courseViewModel.loadCourseProfile(membership.course_id);
                                break;
                        }
                    }
                }
        ).show();
        return true;
    }
}
