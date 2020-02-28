package com.nablanet.aula31.schedules.ui.schedule;

import android.app.TimePickerDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.nablanet.aula31.R;
import com.nablanet.aula31.repo.entity.CourseProfile;
import com.nablanet.aula31.schedules.Schedule;

import java.util.ArrayList;
import java.util.List;

public class ScheduleListFragment extends Fragment {

    private ScheduleViewModel mViewModel;
    TextView course, institute;
    RecyclerView recyclerView;

    Schedule schedule;
    Schedule.Hours hours;

    CourseProfile courseProfileExt;

    public static ScheduleListFragment newInstance() {
        return new ScheduleListFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.schedule_list_fragment, container, false);
        course = view.findViewById(R.id.course_name_tv);
        institute = view.findViewById(R.id.institute_name_tv);
        recyclerView = view.findViewById(R.id.schedule_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new ScheduleListAdapter(new ArrayList<Schedule.Hours>()));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ScheduleViewModel.class);

        courseProfileExt = mViewModel.getCurrentCourseMutableLiveData().getValue();

        mViewModel.getSchedulesLiveDataByUser().observe(
                this, new Observer<List<Schedule>>() {
                    @Override
                    public void onChanged(@Nullable List<Schedule> schedules) {
                        if (schedules == null || courseProfileExt == null || TextUtils.isEmpty(/*courseProfileExt.getKey()*/null)) return;
                        for ( Schedule schedule : schedules)
                            if (schedule.course_id.equals(/*courseProfileExt.getKey()*/null)) {
                                ScheduleListFragment.this.schedule = schedule;
                                ((ScheduleListAdapter) recyclerView.getAdapter())
                                        .updateList(new ArrayList<>(schedule.weekdays.values()));
                                break;
                            }
                    }
                }
        );
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.schedule_list_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.btn_new:
                launchDialogWeekDay();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private void launchDialogWeekDay() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final String[] weekDays = new String[]{
                getString(R.string.monday),
                getString(R.string.tuesday),
                getString(R.string.wednesday),
                getString(R.string.thursday),
                getString(R.string.friday)
        };
        builder.setSingleChoiceItems(
                weekDays,
                -1,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        hours = new Schedule.Hours();
                        hours.id = weekDays[which];
                        launchDialogInitTime();
                    }
                }
        ).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setTitle("Día de la semana").setCancelable(true);
        builder.show();

    }

    private void launchDialogInitTime() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                getActivity(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        hours.init = 60 * hourOfDay + minute;
                        launchDialogEndTime();
                    }
                }, 0, 0, true
        );
        timePickerDialog.setTitle("Comienza");
        timePickerDialog.setCancelable(true);
        timePickerDialog.show();
    }

    private void launchDialogEndTime() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                getActivity(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        hours.end = 60 * hourOfDay + minute;
                        launchDialogEndTime();
                        mViewModel.saveHours(
                                (schedule == null) ? null : schedule.id,
                                hours
                        );
                    }
                }, 0, 0, true
        );
        timePickerDialog.setTitle("Termina");
        timePickerDialog.setCancelable(true);
        timePickerDialog.show();
    }


}
