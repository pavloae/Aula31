package com.nablanet.aula31.courses;

import android.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nablanet.aula31.OnItemListener;
import com.nablanet.aula31.classes.ClassActivity;
import com.nablanet.aula31.R;
import com.nablanet.aula31.courses.view.CoursesAdapter;
import com.nablanet.aula31.courses.viewmodel.CourseViewModel;
import com.nablanet.aula31.repo.entity.Membership;

import java.util.List;

public class CoursesListFragment extends Fragment implements OnItemListener<Membership> {

    CourseViewModel courseViewModel;

    RecyclerView recyclerView;
    CoursesAdapter coursesAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_course, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        coursesAdapter = new CoursesAdapter( this);

        recyclerView = view.findViewById(R.id.courses_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(coursesAdapter);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        assert getActivity() != null;

        courseViewModel = ViewModelProviders.of(getActivity()).get(CourseViewModel.class);

        courseViewModel.getMemberships().observe(this, new Observer<List<Membership>>() {
            @Override
            public void onChanged(@Nullable List<Membership> memberships) {

                coursesAdapter.updateList(memberships);

            }
        });

    }

    @Override
    public void onItemClick(@NonNull Membership membership) {

        Intent intent = new Intent(getActivity(), ClassActivity.class);

        Bundle bundle = new Bundle();
        bundle.putString(ClassActivity.COURSE_ID_KEY, membership.getCourse_id());

        intent.putExtras(bundle);

        startActivity(intent);

    }

    @Override
    public boolean onItemLongClick(@NonNull final Membership membership) {

        if (membership.getRole() == null)
            return true;

        String[] options = (membership.getRole() == Membership.TEACHER) ?
                new String[]{"Abandonar", "Editar"}
                : new String[] {"Abandonar"};

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
                                if (membership.getCourse_id() != null)
                                    courseViewModel.setCourseId(membership.getCourse_id());
                                else
                                    Toast.makeText(
                                            getActivity(), "Curso sin id", Toast.LENGTH_SHORT
                                    ).show();
                                break;

                                default:
                        }

                    }
                }
        );

        builder.show();

        return true;

    }
}
