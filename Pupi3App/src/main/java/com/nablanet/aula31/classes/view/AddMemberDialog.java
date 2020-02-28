package com.nablanet.aula31.classes.view;

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
import com.nablanet.aula31.classes.viewmodel.ClassViewModel;
import com.nablanet.aula31.repo.entity.Member;
import com.nablanet.aula31.repo.entity.Membership;

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

        // Guardamos el miembro actual y continuamos la carga
        ((AlertDialog) getDialog()).getButton(DialogInterface.BUTTON_NEUTRAL).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveMember();
                        cleanDialog();
                    }
                }
        );

        // Cancelamos la carga del mimembro actual y cerramos el diálogo
        ((AlertDialog) getDialog()).getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        close();
                    }
                }
        );

        // Guardamos el miembro actual y cerramos el diálogo
        ((AlertDialog) getDialog()).getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveMember();
                        close();
                    }
                }
        );
    }

    private void saveMember() {
        classViewModel.saveMemberToCourse(getMember());
    }

    private void close() {
        getDialog().cancel();
    }

    private Member getMember() {
        Member memberItem = new Member();
        memberItem.setLastname(lastnameET.getText().toString());
        memberItem.setNames(namesET.getText().toString());
        memberItem.setRole(Membership.STUDENT);
        memberItem.setState(Membership.ACCEPTED);
        return memberItem;
    }

    private void cleanDialog() {
        lastnameET.setText("");
        namesET.setText("");
        lastnameET.requestFocus();
    }

}
