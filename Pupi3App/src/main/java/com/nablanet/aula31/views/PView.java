package com.nablanet.aula31.views;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.nablanet.aula31.R;
import com.nablanet.aula31.repo.entity.User;

public class PView extends CardView {

    User member;
    ImageView pupilIV;
    public TextView lastnameTV, namesTV;
    int visibility;

    public PView(Context context) {
        super(context, null);
    }

    public PView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.pupil_view, this);
        pupilIV = findViewById(R.id.pupil_image);
        lastnameTV = findViewById(R.id.lastname);
        namesTV = findViewById(R.id.names);
        setVisibility(INVISIBLE);
    }

    public PView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setUser(@NonNull User member) {
        this.member = member;
        pupilIV.setImageResource(R.drawable.ic_subject_color);
        lastnameTV.setText(member.getLastname());
        namesTV.setText(member.getNames());
        setVisibility(VISIBLE);
    }

    public User getUser() {
        return member;
    }

    @Override
    public void setVisibility(int visibility) {
        this.visibility = visibility;
        pupilIV.setVisibility(visibility);
        lastnameTV.setVisibility(visibility);
        namesTV.setVisibility(visibility);
    }

    @Override
    public int getVisibility() {
        return this.visibility;
    }
}
