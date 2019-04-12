package com.nablanet.aula31.classes;

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
import com.nablanet.aula31.courses.Course;
import com.nablanet.aula31.courses.Membership;

public class AddMemberDialog extends DialogFragment {

    ClassViewModel classViewModel;
    EditText lastnameET, namesET;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        assert getActivity() != null;
        classViewModel = ViewModelProviders.of(getActivity()).get(ClassViewModel.class);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        assert getActivity() != null;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        @SuppressLint("InflateParams")
        View view = getActivity().getLayoutInflater().inflate(
                R.layout.profile_member_dialog, null
        );
        lastnameET = view.findViewById(R.id.lastname_et);
        namesET = view.findViewById(R.id.names_et);

        return builder.setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setNeutralButton("NEXT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

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
        ((AlertDialog) getDialog()).getButton(DialogInterface.BUTTON_NEUTRAL).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        classViewModel.saveMemberToCourse(buildMember());
                        cleanDialog();
                        lastnameET.requestFocus();
                    }
                }
        );
        ((AlertDialog) getDialog()).getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cleanDialog();
                        AddMemberDialog.this.getDialog().cancel();
                    }
                }
        );
        ((AlertDialog) getDialog()).getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        classViewModel.saveMemberToCourse(buildMember());
                        AddMemberDialog.this.getDialog().cancel();
                    }
                }
        );
    }

    private Course.Member buildMember() {
        Course.Member member = new Course.Member();
        member.lastname = lastnameET.getText().toString();
        member.names = namesET.getText().toString();
        member.role = Membership.STUDENT;
        member.state = Membership.ACCEPTED;
        return member;
    }

    private void cleanDialog() {
        lastnameET.setText("");
        namesET.setText("");
    }

}
