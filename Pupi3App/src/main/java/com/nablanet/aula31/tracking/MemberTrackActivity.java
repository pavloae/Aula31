package com.nablanet.aula31.tracking;

import android.annotation.TargetApi;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nablanet.aula31.R;
import com.nablanet.aula31.repo.Response;
import com.nablanet.aula31.repo.entity.ClassTrack;
import com.nablanet.aula31.repo.entity.Observation;
import com.nablanet.aula31.repo.entity.Profile;
import com.nablanet.aula31.tracking.view.EditMemberDialog;
import com.nablanet.aula31.tracking.viewmodel.TrackingViewModel;
import com.nablanet.aula31.utils.Util;
import com.nablanet.aula31.views.RatingPersonView;

public class MemberTrackActivity extends AppCompatActivity {

    public static final String MEMBER_ID_KEY = "member_id_key";
    public static final String CLASS_ID_KEY = "class_id_key";

    SoundPool soundPool;
    int oneStar, twoStar, threeStar, fourStar, fiveStar;

    TrackingViewModel viewModel;

    TextView lastname, names;
    RatingPersonView averageRate;

    EditText comment;

    RatingPersonView ratingPersonView;

    TextView dateTV;

    Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_track);

        if (getIntent().getExtras() == null) return;

        lastname = findViewById(R.id.lastname);
        names = findViewById(R.id.names);
        averageRate = findViewById(R.id.average_rate);

        comment = findViewById(R.id.comment_et);

        ratingPersonView = findViewById(R.id.person_rb);
        ratingPersonView.setOnRatingBarChangeListener(getOnRatingBarChangeListener());

        dateTV = findViewById(R.id.date_tv);

        init();

    }

    @Override
    protected void onStart() {
        super.onStart();
        createSoundPool();
    }

    @Override
    protected void onStop() {
        if (soundPool != null){
            soundPool.release();
            soundPool = null;
        }
        super.onStop();
    }

    private void init() {

        String memberId = getIntent().getStringExtra(MEMBER_ID_KEY);
        String classId = getIntent().getStringExtra(CLASS_ID_KEY);

        if (memberId == null || classId == null) {
            finish();
            return;
        }

        viewModel = ViewModelProviders.of(this).get(TrackingViewModel.class);

        viewModel.getResponse().observe(this, new Observer<Response>() {
            @Override
            public void onChanged(@Nullable Response response) {
                if (response != null && !response.success)
                    Toast.makeText(
                            getApplication(), response.getMessage(), Toast.LENGTH_SHORT
                    ).show();
            }
        });

        viewModel.setCurrentMember(memberId);
        viewModel.setCurrentClass(classId);

        viewModel.getProfile()
                .observe(this, new Observer<Profile>() {
                    @Override
                    public void onChanged(@Nullable Profile profile) {

                        MemberTrackActivity.this.profile = profile;

                        if (profile == null) {

                            lastname.setText(null);
                            names.setText(null);

                        } else {

                            lastname.setText(profile.getLastname());
                            names.setText(profile.getNames());

                        }

                    }
                });

        viewModel.getCurrentClass().observe(this, new Observer<ClassTrack>() {
            @Override
            public void onChanged(@Nullable ClassTrack classTrack) {

                Log.d(
                        "getCurrentClass",
                        "classTrack" + ((classTrack == null) ? "null" : classTrack.getKey())
                );

                dateTV.setText(
                        classTrack == null ? null : Util.getStringDate(classTrack.getDate())
                );

            }
        });


        viewModel.getObservation().observe(this, new Observer<Observation>() {
            @Override
            public void onChanged(@Nullable Observation observation) {
                if (observation == null) {
                    comment.setText(null);
                    ratingPersonView.setRating(0);
                } else {
                    comment.setText(observation.getComment());
                    ratingPersonView.setRating(
                            observation.getRate() == null ? 0 : observation.getRate()
                    );
                }
            }
        });

        viewModel.getAverage().observe(this, new Observer<Float>() {
            @Override
            public void onChanged(@Nullable Float aFloat) {
                averageRate.setRating(
                        aFloat == null ? 0 : aFloat
                );
            }
        });

    }

    public void editProfile(View view) {
        EditMemberDialog.getInstance(profile).show(getSupportFragmentManager(), null);
    }

    private RatingBar.OnRatingBarChangeListener getOnRatingBarChangeListener() {

        return new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (!fromUser)
                    return;

                if (rating < 1.5)
                    soundPool.play(oneStar, 0.5f, 0.5f, 1, 0, 1);
                else if (rating < 2.5)
                    soundPool.play(twoStar, 0.5f, 0.5f, 1, 0, 1);
                else if (rating < 3.5)
                    soundPool.play(threeStar, 0.5f, 0.5f, 1, 0, 1);
                else if (rating < 4.5)
                    soundPool.play(fourStar, 0.5f, 0.5f, 1, 0, 1);
                else
                    soundPool.play(fiveStar, 0.5f, 0.5f, 1, 0, 1);

                viewModel.saveTrack(
                        new Observation(Math.round(rating), comment.getText().toString())
                );

            }
        };
    }

    protected void createSoundPool() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            createNewSoundPool();
        } else {
            createOldSoundPool();
        }
        oneStar = soundPool.load(getBaseContext(), R.raw.nosedive_1_star, 1);
        twoStar = soundPool.load(getBaseContext(), R.raw.nosedive_2_stars, 1);
        threeStar = soundPool.load(getBaseContext(), R.raw.nosedive_3_stars, 1);
        fourStar = soundPool.load(getBaseContext(), R.raw.nosedive_4_stars, 1);
        fiveStar = soundPool.load(getBaseContext(), R.raw.nosedive_5_stars, 1);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void createNewSoundPool(){
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .build();
    }

    protected void createOldSoundPool(){
        soundPool = new SoundPool(5,AudioManager.STREAM_MUSIC,0);
    }

    public void onPreviousClass(View view) {
        viewModel.setPreviousClass();
    }

    public void onCalendar(View view) {

    }

    public void onNextClass(View view) {
        viewModel.setNextClass();
    }

}
