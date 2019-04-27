package com.nablanet.aula31.tracking;

import android.annotation.TargetApi;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.nablanet.aula31.R;
import com.nablanet.aula31.views.RatingPersonView;

public class MemberTrackActivity extends AppCompatActivity {

    public static final String COURSE_ID_KEY = "course_id_key";
    public static final String MEMBER_ID_KEY = "member_id_key";
    public static final String CLASS_ID_KEY = "class_id_key";
    public static final String LASTNAME_KEY = "lastname_key";
    public static final String NAMES_KEY = "names_key";

    SoundPool soundPool;
    int oneStar, twoStar, threeStar, fourStar, fiveStar;

    TrackingViewModel trackingViewModel;

    TextView lastname, names;
    EditText comment;
    RatingPersonView ratingPersonView, averageRate;
    String userId, memberId, classId, courseId;
    MemberTrack.Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        if (getIntent().getExtras() == null) return;

        lastname = findViewById(R.id.lastname);
        names = findViewById(R.id.names);
        comment = findViewById(R.id.comment_et);

        ratingPersonView = findViewById(R.id.person_rb);
        ratingPersonView.setOnRatingBarChangeListener(getOnRatingBarChangeListener());

        averageRate = findViewById(R.id.average_rate);

        userId = FirebaseAuth.getInstance().getUid();
        memberId = getIntent().getExtras().getString(MEMBER_ID_KEY);
        classId = getIntent().getExtras().getString(CLASS_ID_KEY);
        courseId = getIntent().getExtras().getString(COURSE_ID_KEY);

        lastname.setText(getIntent().getExtras().getString(LASTNAME_KEY, ""));
        names.setText(getIntent().getExtras().getString(NAMES_KEY, ""));

        if (TextUtils.isEmpty(userId) || TextUtils.isEmpty(memberId) || TextUtils.isEmpty(classId)){
            finish();
            return;
        }

        trackingViewModel = ViewModelProviders.of(this).get(TrackingViewModel.class);
        trackingViewModel.getCurrentProfileLiveData(memberId)
                .observe(this, new Observer<MemberTrack.Profile>() {
                    @Override
                    public void onChanged(@Nullable MemberTrack.Profile profile) {
                        if (profile == null) return;
                        MemberTrackActivity.this.profile = profile;
                        lastname.setText(profile.lastname);
                        names.setText(profile.names);
                    }
                });
        trackingViewModel.getCurrentObservationLiveData(memberId, classId)
                .observe(this, new Observer<MemberTrack.Observation>() {
                    @Override
                    public void onChanged(@Nullable MemberTrack.Observation observation) {
                        if (observation == null) return;
                        comment.setText(observation.comment);
                        ratingPersonView.setRating(observation.rate);
                    }
                });
        trackingViewModel.getDatabaseError().observe(this, new Observer<DatabaseError>() {
            @Override
            public void onChanged(@Nullable DatabaseError databaseError) {
                if (databaseError != null)
                    Snackbar.make(ratingPersonView, databaseError.getMessage(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
            }
        });
        trackingViewModel.getAverageRateLiveData().observe(this, new Observer<Float>() {
            @Override
            public void onChanged(@Nullable Float aFloat) {
                if (aFloat != null)
                    averageRate.setRating(aFloat);
            }
        });
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

    public void editProfile(View view) {
        EditMemberDialog.getInstance(courseId, memberId, profile)
                .show(getSupportFragmentManager(), null);
    }

    private RatingBar.OnRatingBarChangeListener getOnRatingBarChangeListener() {
        return new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (!fromUser) return;
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

                if (TextUtils.isEmpty(userId) || TextUtils.isEmpty(memberId) || TextUtils.isEmpty(classId))
                    return;

                trackingViewModel.saveTrack(
                        memberId, classId, userId,
                        new MemberTrack.Observation(
                                Math.round(rating),
                                comment.getText().toString()
                        )
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

    @SuppressWarnings("deprecation")
    protected void createOldSoundPool(){
        soundPool = new SoundPool(5,AudioManager.STREAM_MUSIC,0);
    }

}
