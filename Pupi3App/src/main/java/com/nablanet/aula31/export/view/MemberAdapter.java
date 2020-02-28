package com.nablanet.aula31.export.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.nablanet.aula31.R;
import com.nablanet.aula31.export.entity.MemberExt;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ItemViewHolder> {

    private List<MemberExt> memberList;
    private int layoutType;

    public MemberAdapter(List<MemberExt> members, int layoutType) {
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
        MemberExt member = getMember(position);
        return (member == null) ? position : member.hashCode();
    }

    private MemberExt getMember(int position) {
        return memberList.get(position);
    }

    public List<MemberExt> getMemberList() {
        return memberList;
    }

    public List<String> getMemberKeyList(boolean checked) {
        List<String> members = new ArrayList<>();
        for (MemberExt memberExt : memberList)
            if (memberExt.checked == checked)
                members.add(memberExt.getKey());
        return members;
    }

    public void updateMembers(@Nullable List<MemberExt> members) {
        if (members == null) members = new ArrayList<>();
        this.memberList = members;
        notifyDataSetChanged();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final View itemView;
        public final TextView fullName;
        public final CheckBox checkBox;

        public MemberExt member;

        public ItemViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            fullName = itemView.findViewById(R.id.fullname_tv);
            checkBox = itemView.findViewById(R.id.checkbox);
            checkBox.setOnClickListener(this);
        }

        public void bin(@NonNull MemberExt member) {
            this.member = member;
            fullName.setText(
                    String.format(
                            Locale.getDefault(), "%s, %s", member.getLastname(),
                            member.getNames()
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
