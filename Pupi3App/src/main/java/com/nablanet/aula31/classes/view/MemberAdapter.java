package com.nablanet.aula31.classes.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nablanet.aula31.R;
import com.nablanet.aula31.classes.OnMemberListener;
import com.nablanet.aula31.classes.entity.MemberItem;

import java.util.ArrayList;
import java.util.List;


public class MemberAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MemberItem> memberItemList;
    private final OnMemberListener onMemberListener;
    private int layoutType;
    private int activePupitre = -1;
    @Nullable private String activeMemberId;

    public MemberAdapter(List<MemberItem> memberExts, OnMemberListener onMemberListener, int layoutType) {
        this.layoutType = layoutType;
        this.memberItemList = memberExts;
        this.onMemberListener = onMemberListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(viewType, parent, false);
        return (layoutType == ClassRecyclerView.GRID) ?
                new PupitreViewHolder(view) : new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        MemberItem memberItem = getMember(position);
        if (layoutType == ClassRecyclerView.GRID)
            ((PupitreViewHolder) holder).bin(getMember(position));
        else if (memberItem != null)
            ((ItemViewHolder) holder).bin(memberItem);
    }

    @Override
    public int getItemCount() {
        return (layoutType == ClassRecyclerView.GRID) ? 30 : memberItemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (layoutType == ClassRecyclerView.GRID) ?
                R.layout.item_member_grid : R.layout.item_member_list;
    }

    @Override
    public long getItemId(int position) {
        MemberItem memberItem = getMember(position);
        return (memberItem == null) ? position : memberItem.hashCode();
    }

    private MemberItem getMember(int position) {
        if ((layoutType == ClassRecyclerView.GRID)) {
            for (MemberItem memberItem : memberItemList)
                if (memberItem.isAtPupitre(position))
                    return memberItem;
            return null;
        }
        return memberItemList.get(position);
    }

    public void setGrid(int layoutType){
        this.layoutType = layoutType;
        notifyDataSetChanged();
    }

    public void setCurrentMember(@Nullable MemberItem member) {

        if (member == null) {
            activePupitre = -1;
            activeMemberId = null;
        } else {
            activePupitre = (member.getPosition() == null) ? -1 : member.getPosition();
            activeMemberId = member.getKey();
        }

        notifyDataSetChanged();

    }

    public void updateMembers(@Nullable List<MemberItem> memberExts) {
        if (memberExts == null)
            memberExts = new ArrayList<>();
        this.memberItemList = memberExts;
        notifyDataSetChanged();
    }

    public class PupitreViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener, View.OnLongClickListener {

        public final View pupitreView;

        public CardView pupitre;

        public final ImageView pictureIV;
        public final TextView lastnameTV, namesTV;

        public final RelativeLayout memberRL;
        @Nullable public MemberItem memberItem;

        public PupitreViewHolder(View pupitreView) {
            super(pupitreView);
            this.pupitreView = pupitreView;

            pictureIV = pupitreView.findViewById(R.id.picture_iv);
            lastnameTV = pupitreView.findViewById(R.id.lastname_tv);
            namesTV = pupitreView.findViewById(R.id.names_tv);

            memberRL = pupitreView.findViewById(R.id.member_rl);
            memberRL.setOnClickListener(this);
            memberRL.setOnLongClickListener(this);

            pupitre = pupitreView.findViewById(R.id.pupitre_cv);

        }

        public void bin(@Nullable MemberItem memberItem) {
            this.memberItem = memberItem;

            if (memberItem == null || memberItem.getPresent() == null || !memberItem.getPresent()) {
                pictureIV.setImageDrawable(
                        pupitreView.getResources().getDrawable(R.drawable.ic_absent_gray)
                );
                lastnameTV.setText("");
                namesTV.setText("");
            } else {
                pictureIV.setImageDrawable(
                        pupitreView.getResources().getDrawable(R.drawable.ic_present_green)
                );
                lastnameTV.setText(memberItem.getLastname());
                namesTV.setText(memberItem.getNames());
            }

            if (memberItem != null && memberItem.isAtPupitre(activePupitre))
                pupitre.setCardBackgroundColor(
                        pupitreView.getResources().getColor(R.color.colorBackgroundFloating)
                );
            else
                pupitre.setCardBackgroundColor(
                        null
                );
        }

        @Override
        public void onClick(View v) {

            // Si el pupítre estaba vacío, mandamos un nuevo miembro con su ubicación para que
            // sea completado y actualizado en la base de datos
            if (memberItem == null) {
                memberItem = new MemberItem(getLayoutPosition());
                onMemberListener.onItemClick(memberItem);
            }

            // Si el pupitre no estaba vacío y no es el pupítre activo, lo marcamos como activo
            else if (getLayoutPosition() != activePupitre)
            {
                activePupitre = getLayoutPosition();
                onMemberListener.onItemClick(memberItem);
            }

            // Si el pupítre no estaba vacío y es el activo, lanzamos el pedido para hacer el
            // seguimiento del alumno
            else  {
                onMemberListener.onMemberTrack(memberItem);
            }

        }

        @Override
        public boolean onLongClick(View v) {
            return (memberItem == null) || onMemberListener.onItemLongClick(memberItem);
        }

    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener, View.OnLongClickListener {

        public final View itemView;
        public CardView itemCV;

        public final ImageView pictureIV;
        public final TextView lastnameTV, namesTV;

        public final RelativeLayout memberRL;
        public ImageButton absentIB, presentIB;
        public MemberItem memberItem;

        public ItemViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;

            itemCV = itemView.findViewById(R.id.item_cv);

            pictureIV = itemView.findViewById(R.id.picture_iv);
            lastnameTV = itemView.findViewById(R.id.lastname_tv);
            namesTV = itemView.findViewById(R.id.names_tv);

            memberRL = itemView.findViewById(R.id.member_rl);
            memberRL.setOnClickListener(this);
            memberRL.setOnLongClickListener(this);

            absentIB = itemView.findViewById(R.id.absent_ib);
            absentIB.setOnClickListener(this);
            absentIB.setOnLongClickListener(this);

            presentIB = itemView.findViewById(R.id.present_ib);
            presentIB.setOnClickListener(this);
            presentIB.setOnLongClickListener(this);
        }

        public void bin(@NonNull MemberItem memberItem) {
            this.memberItem = memberItem;

            lastnameTV.setText(memberItem.getLastname());
            namesTV.setText(memberItem.getNames());
            if (memberItem.getPresent() == null) {
                absentIB.setImageDrawable(
                        itemView.getResources().getDrawable(R.drawable.ic_absent_gray)
                );
                presentIB.setImageDrawable(
                        itemView.getResources().getDrawable(R.drawable.ic_present_gray)
                );
            } else if (memberItem.getPresent()) {
                absentIB.setImageDrawable(
                        itemView.getResources().getDrawable(R.drawable.ic_absent_gray)
                );
                presentIB.setImageDrawable(
                        itemView.getResources().getDrawable(R.drawable.ic_present_green)
                );
            } else {
                absentIB.setImageDrawable(
                        itemView.getResources().getDrawable(R.drawable.ic_absent_red)
                );
                presentIB.setImageDrawable(
                        itemView.getResources().getDrawable(R.drawable.ic_present_gray)
                );
            }

            if (activeMemberId != null && activeMemberId.equals(memberItem.getKey()))
                itemCV.setCardBackgroundColor(
                        itemCV.getResources().getColor(R.color.colorBackgroundFloating)
                );
            else
                itemCV.setCardBackgroundColor(
                        null
                );

        }

        @Override
        public void onClick(View v) {

            if (v.getId() == R.id.absent_ib)
                memberItem.setPresent(false);
            if (v.getId() == R.id.present_ib)
                memberItem.setPresent(true);

            if (v.getId() == R.id.member_rl)
                onMemberListener.onMemberTrack(memberItem);
            else
                onMemberListener.onItemClick(memberItem);

        }

        @Override
        public boolean onLongClick(View v) {

            return onMemberListener.onItemLongClick(memberItem);

        }

    }

}
