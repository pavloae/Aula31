package com.nablanet.aula31.tracking.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import com.nablanet.aula31.R;
import com.nablanet.aula31.repo.entity.Profile;
import com.nablanet.aula31.tracking.viewmodel.TrackingViewModel;

public class EditMemberDialog extends DialogFragment {

    TrackingViewModel viewModel;

    EditText lastnameET, namesET;
    Profile profile;

    public static EditMemberDialog getInstance(Profile profile) {
        EditMemberDialog editMemberDialog = new EditMemberDialog();
        editMemberDialog.profile = profile;
        return editMemberDialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        assert getActivity() != null;
        viewModel = ViewModelProviders.of(getActivity()).get(TrackingViewModel.class);
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
                        dismiss();
                    }
                }
        );
        ((AlertDialog) getDialog()).getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        profile.setLastname(lastnameET.getText().toString());
                        profile.setNames(namesET.getText().toString());
                        viewModel.updateProfile(profile);
                    }
                }
        );
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

}
