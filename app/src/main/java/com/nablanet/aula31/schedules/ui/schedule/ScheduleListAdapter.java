package com.nablanet.aula31.schedules.ui.schedule;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nablanet.aula31.R;
import com.nablanet.aula31.schedules.Schedule;

import java.util.ArrayList;
import java.util.List;

public class ScheduleListAdapter extends RecyclerView.Adapter<ScheduleListAdapter.ScheduleViewHolder> {

    List<Schedule.Hours> hours;

    public ScheduleListAdapter(@Nullable List<Schedule.Hours> hours) {
        if (hours == null) hours = new ArrayList<>();
        this.hours = hours;
    }

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(viewType, null, false);
        return new ScheduleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        holder.bind(hours.get(position));
    }

    @Override
    public int getItemCount() {
        return hours.size();
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.item_schedule_list;
    }

    public void updateList(List<Schedule.Hours> hours) {
        this.hours = hours;
        notifyDataSetChanged();
    }

    public class ScheduleViewHolder extends RecyclerView.ViewHolder {

        Schedule.Hours hours;
        final TextView weekday, initTime, endTime;

        public ScheduleViewHolder(View itemView) {
            super(itemView);
            weekday = itemView.findViewById(R.id.week_day_tv);
            initTime = itemView.findViewById(R.id.init_tv);
            endTime = itemView.findViewById(R.id.end_tv);
        }

        public void bind(Schedule.Hours hours) {
            this.hours = hours;
            weekday.setText(hours.id);
            initTime.setText(hours.init);
            initTime.setText(hours.end);
        }

    }

}
