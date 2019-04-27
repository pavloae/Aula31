package com.nablanet.aula31.classes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

public class ClassRecyclerView extends RecyclerView {

    public static final int LIST = 1;
    public static final int GRID = 6;

    private int layoutType = LIST;
    private GridLayoutManager gridLayoutManager;
    private MemberAdapter memberAdapter;

    public ClassRecyclerView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public ClassRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ClassRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public void init(Context context) {
        if (!(context instanceof OnMemberListener))
            throw new ClassCastException(
                    "La clase " + context.getClass().getName() +
                            "debe implementar la interface OnMemberListener"
            );
        if (getItemAnimator() != null) getItemAnimator().setChangeDuration(700);
        memberAdapter = new MemberAdapter(new ArrayList<ClassDay.Member>(), (OnMemberListener) context, LIST);
        memberAdapter.setHasStableIds(true);
        setAdapter(memberAdapter);
        gridLayoutManager = new GridLayoutManager(context, 1);
        setLayoutManager(gridLayoutManager);
    }

    public void setLayoutType(int layoutType) {
        this.layoutType = layoutType;
        gridLayoutManager.setSpanCount(layoutType);
        if (memberAdapter != null)
            memberAdapter.setGrid(layoutType);
    }

    public int getLayoutType(){
        return layoutType;
    }

    public void updateList(List<ClassDay.Member> members) {
        if (memberAdapter != null)
            memberAdapter.updateMembers(members);
    }

}
