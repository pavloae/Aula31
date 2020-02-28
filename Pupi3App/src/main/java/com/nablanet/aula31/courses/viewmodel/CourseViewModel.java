package com.nablanet.aula31.courses.viewmodel;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.TextUtils;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.nablanet.aula31.repo.AbsentLiveData;
import com.nablanet.aula31.repo.DataResult;
import com.nablanet.aula31.repo.FireBaseRepo;
import com.nablanet.aula31.repo.Response;
import com.nablanet.aula31.repo.Utils;
import com.nablanet.aula31.repo.entity.Course;
import com.nablanet.aula31.repo.entity.CourseProfile;
import com.nablanet.aula31.repo.entity.Membership;

import java.util.ArrayList;
import java.util.List;

public class CourseViewModel extends ViewModel {

    FireBaseRepo fireBaseRepo = FireBaseRepo.getInstance();

    private final MutableLiveData<Response> responseLive = new MutableLiveData<>();

    private MutableLiveData<String> courseId = new MutableLiveData<>();

    private LiveData<CourseProfile> courseProfileLive;
    private LiveData<List<Membership>> membershipsLive;

    @NonNull
    public LiveData<Response> getResponse() {
        return responseLive;
    }

    @NonNull
    public LiveData<List<Membership>> getMemberships() {
        if (membershipsLive == null)
            membershipsLive = Transformations.map(
                    fireBaseRepo.getMemberships(),
                    new Function<DataResult<Membership>, List<Membership>>() {
                        @Override
                        public List<Membership> apply(DataResult<Membership> dataResult) {

                            if (dataResult != null && dataResult.getDatabaseError() != null)

                                responseLive.setValue(
                                        new Response(
                                                false,
                                                dataResult.getDatabaseError().getMessage()
                                        )
                                );

                            else if (dataResult != null && dataResult.getMap() != null)

                                return new ArrayList<>(dataResult.getMap().values());

                            return null;

                        }
                    }
            );

        return membershipsLive;
    }

    public void setCourseId(@Nullable String courseId) {
        this.courseId.setValue(courseId);
    }

    @NonNull
    public LiveData<String> getCourseId() {
        return courseId;
    }

    @NonNull
    public LiveData<CourseProfile> getCourseProfile() {

        if (courseProfileLive != null)

            return courseProfileLive;

        final Function<DataResult<CourseProfile>, CourseProfile> function =
                new Function<DataResult<CourseProfile>, CourseProfile>() {

            @Override
            public CourseProfile apply(DataResult<CourseProfile> input) {

                if (input != null && input.getDatabaseError() != null)

                    responseLive.setValue(
                            new Response(false, input.getDatabaseError().getMessage())
                    );

                else if (input != null)

                    return input.getValue();

                return null;

            }

        };

        courseProfileLive = Transformations.switchMap(
                courseId,
                new Function<String, LiveData<CourseProfile>>() {
                    @Override
                    public LiveData<CourseProfile> apply(@Nullable String input) {

                        if (input == null)
                            return new AbsentLiveData<>();

                        return Transformations.map(
                                fireBaseRepo.getCourseProfile(input), function
                        );

                    }
                }
        );

        return courseProfileLive;

    }

    public void leaveCourse(Membership membership) {
        if (membership != null && membership.getKey() != null)
            fireBaseRepo.deleteMembership(membership.getKey());
    }

    public void createCourse(final CourseProfile courseProfile) {

        final Course course = new Course();
        course.setProfile(courseProfile);

        fireBaseRepo.create(course)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        createOwnerMembership(course.getKey(), courseProfile);
                    }
                });

    }

    public void updateCourseProfile(@NonNull final CourseProfile courseProfile) {

        String courseId = this.courseId.getValue();

        if (TextUtils.isEmpty(courseId)) {
            createCourse(courseProfile);
            return;
        }

        assert courseId != null;

        fireBaseRepo.updateCourseProfile(courseId, courseProfile);

    }

    private void createOwnerMembership(
            @NonNull String courseId, @NonNull CourseProfile courseProfile
    ) {

        Membership membership = new Membership();
        membership.setCourse_id(courseId);
        membership.setCourse_name(Utils.getCourseName(courseProfile));
        membership.setInstitution_name(
                courseProfile.getInstitution() == null ?
                        null : courseProfile.getInstitution().getName()
        );
        membership.setRole(Membership.TEACHER);
        membership.setState(Membership.ACCEPTED);

        fireBaseRepo.createMembership(membership).addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        responseLive.setValue(new Response(false, e.getMessage()));
                    }
                }
        );

    }

}
