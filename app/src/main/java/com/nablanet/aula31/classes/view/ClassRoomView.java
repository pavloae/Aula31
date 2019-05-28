package com.nablanet.aula31.classes.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.nablanet.aula31.views.PView;

import java.util.ArrayList;
import java.util.List;


public class ClassRoomView extends TableLayout {

    List<PView> pViewList;

    public ClassRoomView(Context context) {
        super(context, null);
    }

    public ClassRoomView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f);
        TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f);

        tableLayoutParams.bottomMargin = 5;
        tableLayoutParams.topMargin = 5;
        tableRowParams.leftMargin = 5;
        tableRowParams.rightMargin = 5;

        pViewList = new ArrayList<>();

        for (int row = 0 ; row < 6 ; row++) {
            TableRow tableRow = new TableRow(context, attrs);
            tableRow.setLayoutParams(tableLayoutParams);
            for (int col = 0 ; col < 6 ; col++) {
                PView pView = new PView(context, attrs);
                pView.setLayoutParams(tableRowParams);
                pViewList.add(pView);
                tableRow.addView(pView);
            }
            addView(tableRow);
        }

    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        for (PView pView : pViewList)
            pView.setOnTouchListener(l);
    }

}
