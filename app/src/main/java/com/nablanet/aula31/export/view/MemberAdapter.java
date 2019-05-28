package com.nablanet.aula31.export.view;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.nablanet.aula31.R;
import com.nablanet.aula31.export.entity.MemberCourseExport;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ItemViewHolder> {

    private List<MemberCourseExport> memberList;
    private int layoutType;

    public MemberAdapter(List<MemberCourseExport> members, int layoutType) {
        this.layoutType = layoutType;
        this.memberList = members;
    }

    @NonNull
    @Override
    public MemberAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(viewType, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MemberAdapter.ItemViewHolder holder, int position) {
        holder.bin(getMember(position));
    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return layoutType;
    }

    @Override
    public long getItemId(int position) {
        MemberCourseExport member = getMember(position);
        return (member == null) ? position : member.hashCode();
    }

    private MemberCourseExport getMember(int position) {
        return memberList.get(position);
    }

    public List<MemberCourseExport> getMemberList() {
        return memberList;
    }

    public List<MemberCourseExport> getMemberList(boolean checked) {
        List<MemberCourseExport> members = new ArrayList<>();
        for (MemberCourseExport memberCourseExport : memberList)
            if (memberCourseExport.checked == checked)
                members.add(memberCourseExport);
        return members;
    }

    public List<String> getMemberKeyList(boolean checked) {
        List<String> members = new ArrayList<>();
        for (MemberCourseExport memberCourseExport : memberList)
            if (memberCourseExport.checked == checked)
                members.add(memberCourseExport.memberId);
        return members;
    }

    public void updateMembers(@Nullable List<MemberCourseExport> members) {
        if (members == null) members = new ArrayList<>();
        this.memberList = members;
        notifyDataSetChanged();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final View itemView;
        public final TextView fullName;
        public final CheckBox checkBox;

        public MemberCourseExport member;

        public ItemViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            fullName = itemView.findViewById(R.id.fullname_tv);
            checkBox = itemView.findViewById(R.id.checkbox);
            checkBox.setOnClickListener(this);
        }

        public void bin(@NonNull MemberCourseExport member) {
            this.member = member;
            fullName.setText(
                    String.format(
                            Locale.getDefault(), "%s, %s", member.lastname,
                            member.names
                    )
            );
            checkBox.setChecked(member.checked);
        }

        @Override
        public void onClick(View v) {
            member.checked = !member.checked;
            notifyDataSetChanged();
        }

    }

}
