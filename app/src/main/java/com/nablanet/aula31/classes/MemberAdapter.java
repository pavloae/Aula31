package com.nablanet.aula31.classes;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nablanet.aula31.R;
import com.nablanet.aula31.classes.ClassDay.Member;

import java.util.ArrayList;
import java.util.List;


public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberViewHolder> {

    private List<Member> members;
    private final OnMemberListener onMemberListener;
    private int layoutType;

    public MemberAdapter(List<Member> members, OnMemberListener onMemberListener, int layoutType) {
        this.layoutType = layoutType;
        this.members = members;
        this.onMemberListener = onMemberListener;
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(viewType, parent, false);
        return new MemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MemberViewHolder holder, int position) {
        holder.bin(getMember(position));
    }

    @Override
    public int getItemCount() {
        return (layoutType == ClassRecyclerView.GRID) ? 30 : members.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (layoutType == ClassRecyclerView.GRID) ?
                R.layout.item_member_grid : R.layout.item_member_list;
    }

    @Override
    public long getItemId(int position) {
        return getMember(position).hashCode();
    }

    @NonNull
    private Member getMember(int position) {
        if ((layoutType == ClassRecyclerView.GRID)){
            for (Member member : members){
                if ((member.position != null) && member.position == position)
                    return member;
            }
            return new Member(position);
        } else
            return members.get(position);
    }

    public void setGrid(int layoutType){
        this.layoutType = layoutType;
        notifyDataSetChanged();
    }

    public void updateMembers(@Nullable List<Member> members) {
        if (members == null) members = new ArrayList<>();
        this.members = members;
        notifyDataSetChanged();
    }

    public class MemberViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener, View.OnLongClickListener {

        public final View view;

        public final ImageView pictureIV;
        public final TextView lastnameTV, namesTV;

        public final RelativeLayout memberRL;
        public ImageButton absentIB, presentIB;
        public Member member;

        public MemberViewHolder(View view) {
            super(view);
            this.view = view;

            pictureIV = view.findViewById(R.id.picture_iv);
            lastnameTV = view.findViewById(R.id.lastname_tv);
            namesTV = view.findViewById(R.id.names_tv);

            memberRL = view.findViewById(R.id.member_rl);
            memberRL.setOnClickListener(this);
            memberRL.setOnLongClickListener(this);

            if (layoutType == ClassRecyclerView.LIST) {
                absentIB = view.findViewById(R.id.absent_ib);
                absentIB.setOnClickListener(this);
                absentIB.setOnLongClickListener(this);

                presentIB = view.findViewById(R.id.present_ib);
                presentIB.setOnClickListener(this);
                presentIB.setOnLongClickListener(this);
            }
        }

        public void bin(@NonNull Member member) {
            this.member = member;
            if (layoutType == ClassRecyclerView.GRID) {

                if (member.present == null || !member.present) {
                    pictureIV.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_absent_gray));
                    lastnameTV.setText("");
                    namesTV.setText("");
                } else {
                    pictureIV.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_present_green));
                    lastnameTV.setText(member.lastname);
                    namesTV.setText(member.names);
                }
            } else {
                lastnameTV.setText(member.lastname);
                namesTV.setText(member.names);
                if (member.present == null) {
                    absentIB.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_absent_gray));
                    presentIB.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_present_gray));
                } else if (member.present) {
                    absentIB.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_absent_gray));
                    presentIB.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_present_green));
                } else {
                    absentIB.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_absent_red));
                    presentIB.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_present_gray));
                }
            }
        }

        @Override
        public void onClick(View v) {

            boolean isGrid = layoutType == ClassRecyclerView.GRID;

            if (isGrid) {

                this.member.position = this.getLayoutPosition();

                if (member.isNew())
                    onMemberListener.onItemClick(member);
                else
                    onMemberListener.onMemberTrack(member);

            } else {

                if (v.getId() == R.id.absent_ib)
                    member.present = false;
                if (v.getId() == R.id.present_ib)
                    member.present = true;

                if (v.getId() == R.id.member_rl)
                    onMemberListener.onMemberTrack(member);
                else
                    onMemberListener.onItemClick(member);

            }

        }

        @Override
        public boolean onLongClick(View v) {
            return true;
        }

    }
}
