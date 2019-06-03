package com.nablanet.aula31.tracking;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import com.nablanet.aula31.R;
import com.nablanet.aula31.repo.entity.Profile;

public class EditMemberDialog extends DialogFragment {

    TrackingViewModel trackingViewModel;
    EditText lastnameET, namesET;
    String courseId, memberId;
    Profile profile;

    public static EditMemberDialog getInstance(
            String courseId, String memberId, Profile profile
    ) {
        EditMemberDialog editMemberDialog = new EditMemberDialog();
        editMemberDialog.courseId = courseId;
        editMemberDialog.memberId = memberId;
        editMemberDialog.profile = profile;
        return editMemberDialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        trackingViewModel = ViewModelProviders.of(getActivity()).get(TrackingViewModel.class);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        @SuppressLint("InflateParams")
        View view = getActivity().getLayoutInflater().inflate(
                R.layout.profile_member_dialog, null
        );

        lastnameET = view.findViewById(R.id.lastname_et);
        lastnameET.setText(profile.getLastname());

        namesET = view.findViewById(R.id.names_et);
        namesET.setText(profile.getNames());

        return builder.setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .create();
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        lastnameET.requestFocus();
        if (getDialog().getWindow() != null)
            getDialog().getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
            );
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        ((AlertDialog) getDialog()).getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditMemberDialog.this.getDialog().cancel();
                    }
                }
        );
        ((AlertDialog) getDialog()).getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        trackingViewModel.updateMember(buildMember());
                        EditMemberDialog.this.getDialog().cancel();
                    }
                }
        );
    }

    private MemberTracks buildMember() {
        MemberTracks memberTracks = new MemberTracks();
        memberTracks.course_id = courseId;
        memberTracks.id = memberId;
        profile.setLastname(lastnameET.getText().toString());
        profile.setNames(namesET.getText().toString());
        memberTracks.profile = profile;
        return memberTracks;
    }

}
