package com.nablanet.aula31.courses.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nablanet.aula31.OnItemListener;
import com.nablanet.aula31.R;
import com.nablanet.aula31.repo.entity.Membership;

import java.util.ArrayList;
import java.util.List;

public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.CourseVH> {

    private List<Membership> membershipList = new ArrayList<>();
    private final OnItemListener<Membership> onItemListener;

    public CoursesAdapter(@NonNull OnItemListener<Membership> onItemListener) {
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public CourseVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CourseVH(
                LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false)
        );
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

    public void updateList(@Nullable List<Membership> membershipList) {
        if (membershipList == null)
            membershipList = new ArrayList<>();
        this.membershipList = membershipList;
        notifyDataSetChanged();
    }

    class CourseVH extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        final TextView courseName, instituteName;

        @Nullable
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
            courseName.setText(membership.getCourse_name());
            instituteName.setText(membership.getInstitution_name());

        }

        @Override
        public void onClick(View v) {

            if (membership != null)
                onItemListener.onItemClick(membership);

        }

        @Override
        public boolean onLongClick(View v) {

            if (membership != null)
                return onItemListener.onItemLongClick(membership);

            return true;

        }
    }

}
