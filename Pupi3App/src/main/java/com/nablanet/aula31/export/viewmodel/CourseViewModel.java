package com.nablanet.aula31.export.viewmodel;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.annotation.NonNull;

import com.nablanet.aula31.export.entity.CourseExt;
import com.nablanet.aula31.repo.FireBaseRepo;
import com.nablanet.aula31.repo.entity.Course;

public class CourseViewModel extends ViewModel {

    FireBaseRepo fireBaseRepo = FireBaseRepo.getInstance();

    private LiveData<CourseExt> courseLive;

    @NonNull
    public LiveData<CourseExt> getCourseExtLiveData(@NonNull String courseId) {
        if (courseLive == null)
            courseLive = Transformations.map(
                    fireBaseRepo.getCourse(courseId),
                    new Function<Course, CourseExt>() {
                        @Override
                        public CourseExt apply(Course course) {
                            if (course == null)
                                return null;
                            return new CourseExt(course);
                        }
                    }
            );
        return courseLive;
    }

}
