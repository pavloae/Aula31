package com.nablanet.aula31;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.nablanet.aula31.pojos.Institution;
import com.nablanet.aula31.pojos.Subject;
import com.nablanet.aula31.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SubjectActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ProgressBar progressBar;
    EditText nameET;
    NumberPicker gradeNP;
    Spinner institutionSP, subjectSP, shiftSP;

    ArrayList<Institution> institutions;
    ArrayList<Subject> subjects;

    final String[] shifts = new String[]{"Mañana", "Tarde", "Noche"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        String city = getSharedPreferences("USER", MODE_PRIVATE)
                .getString(Constants.CITY_KEY, "Junín de los Andes");

        Toolbar toolbar = findViewById(R.id.toolbar_subject);
        toolbar.setSubtitle(city);

        institutionSP = findViewById(R.id.institution_spinner);
        institutionSP.setOnItemSelectedListener(this);
        subjectSP = findViewById(R.id.subject_spinner);
        subjectSP.setOnItemSelectedListener(this);

        progressBar = findViewById(R.id.progressBar);

        nameET = findViewById(R.id.name_et);
        gradeNP = findViewById(R.id.grade_np);
        gradeNP.setMinValue(1);
        gradeNP.setMaxValue(9);
        gradeNP.setValue(4);
        shiftSP = findViewById(R.id.shift_sp);
        shiftSP.setAdapter(
                new ArrayAdapter<>(
                        this, android.R.layout.simple_spinner_dropdown_item, shifts
                )
        );

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (institutionSP.getSelectedItem() == null){
                    Snackbar.make(nameET, "Seleccione una institución", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }
                Subject subject = new Subject();
                subject.name = nameET.getText().toString();
                subject.grade = gradeNP.getValue();
                subject.shift = shiftSP.getSelectedItem().toString();
                subject.institution_id = institutions.get(institutionSP.getSelectedItemPosition()).id;
                save(subject);
            }
        });

        loadInstitutions(city);

    }

    private void loadInstitutions(final String city) {
        progressBar.setVisibility(View.VISIBLE);
        FirebaseDatabase.getInstance().getReference("institutions")
                .orderByChild("city").equalTo(city).limitToFirst(100)
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                progressBar.setVisibility(View.INVISIBLE);
                                institutions = new ArrayList<>();
                                List<String> names = new ArrayList<>();
                                Institution institution;
                                for (DataSnapshot child : dataSnapshot.getChildren()){
                                    institution = child.getValue(Institution.class);
                                    if (institution == null)
                                        continue;
                                    institution.id = child.getKey();
                                    institutions.add(institution);
                                    names.add(institution.name);
                                }

                                institutionSP.setAdapter(
                                        new ArrayAdapter<>(
                                                getBaseContext(),
                                                android.R.layout.simple_spinner_dropdown_item,
                                                names
                                        )
                                );
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                progressBar.setVisibility(View.INVISIBLE);
                                Snackbar.make(nameET, databaseError.getMessage(), Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        }
                );
    }

    private void loadSubjects(String institutionId) {
        progressBar.setVisibility(View.VISIBLE);
        FirebaseDatabase.getInstance().getReference("subjects")
                .orderByChild("institution_id").equalTo(institutionId).limitToFirst(100)
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                progressBar.setVisibility(View.INVISIBLE);
                                subjects = new ArrayList<>();
                                List<String> names = new ArrayList<>();
                                Subject subject;
                                for (DataSnapshot child : dataSnapshot.getChildren()){
                                    subject = child.getValue(Subject.class);
                                    if (subject == null)
                                        continue;
                                    subject.id = child.getKey();
                                    subjects.add(subject);
                                    names.add(
                                            String.format(
                                                    Locale.getDefault(),
                                                    "%s - %s",
                                                    subject.name, subject.grade
                                            )
                                    );
                                }

                                subjectSP.setAdapter(
                                        new ArrayAdapter<>(
                                                getBaseContext(),
                                                android.R.layout.simple_spinner_dropdown_item,
                                                names
                                        )
                                );

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                progressBar.setVisibility(View.INVISIBLE);
                                Snackbar.make(nameET, databaseError.getMessage(), Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        }
                );
    }

    private void save(Subject subject) {

        subject.id = FirebaseDatabase.getInstance().getReference("subjects").push().getKey();

        FirebaseDatabase.getInstance().getReference("subjects")
                .child(subject.id).setValue(subject)
                .addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                finish();
                            }
                        }
                ).addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(nameET, e.getMessage(), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }
        );

        FirebaseDatabase.getInstance().getReference("usuraios").runTransaction(
                new Transaction.Handler() {
                    @NonNull
                    @Override
                    public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                        return null;
                    }

                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

                    }
                }
        );
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.institution_spinner:
                loadSubjects(institutions.get(position).id);
                break;
            case R.id.subject_spinner:
                nameET.setText(subjects.get(position).name);
                gradeNP.setValue(subjects.get(position).grade);
                for (int pos = 0 ; pos < shifts.length ; pos++ )
                    if (shifts[pos].equals(subjects.get(position).shift))
                        shiftSP.setSelection(pos, true);
                break;
                default:
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        switch (parent.getId()) {
            case R.id.institution_spinner:
                break;
            case R.id.subject_spinner:
                break;
            default:
        }
    }
}
