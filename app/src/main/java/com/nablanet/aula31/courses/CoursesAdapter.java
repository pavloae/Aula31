package com.nablanet.aula31.courses;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nablanet.aula31.OnItemListener;
import com.nablanet.aula31.R;
import com.nablanet.aula31.repo.entity.Membership;

import java.util.List;

public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.CourseVH> {

    private List<Membership> membershipList;
    private final OnItemListener<Membership> onItemListener;

    public CoursesAdapter(List<Membership> membershipList, @Nullable OnItemListener<Membership> onItemListener) {
        this.membershipList = membershipList;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public CourseVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(viewType, parent, false);
        return new CourseVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseVH holder, int position) {
        holder.bindViewHolder(membershipList.get(position));
    }

    @Override
    public int getItemCount() {
        return membershipList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.item_course;
    }

    public void updateList(@NonNull List<Membership> membershipList) {
        this.membershipList = membershipList;
        notifyDataSetChanged();
    }

    class CourseVH extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        final TextView courseName, instituteName;
        String courseId;
        Membership membership;

        CourseVH(View itemView) {
            super(itemView);
            courseName = itemView.findViewById(R.id.course_name_tv);
            instituteName = itemView.findViewById(R.id.institute_name_tv);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        void bindViewHolder(Membership membership) {
            this.membership = membership;
            courseId = membership.course_id;
            courseName.setText(membership.course_name);
            instituteName.setText(membership.institution_name);
        }

        @Override
        public void onClick(View v) {
            onItemListener.onItemClick(membership);
        }

        @Override
        public boolean onLongClick(View v) {
            return onItemListener.onItemLongClick(membership);
        }
    }

}
