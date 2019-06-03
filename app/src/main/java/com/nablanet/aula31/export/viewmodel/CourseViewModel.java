package com.nablanet.aula31.export.viewmodel;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.nablanet.aula31.ExecutorFactory;
import com.nablanet.aula31.export.entity.CourseExt;
import com.nablanet.aula31.repo.FireBaseRepo;
import com.nablanet.aula31.repo.entity.Course;

import java.util.concurrent.Executor;

public class CourseViewModel extends ViewModel {

    public Executor executor = ExecutorFactory.getInstanceSingleThreadExecutor();
    FireBaseRepo fireBaseRepo = FireBaseRepo.getInstance();

    private LiveData<CourseExt> courseLive;

    @NonNull
    public LiveData<CourseExt> getCourseExportLiveData(@NonNull String courseId) {
        if (courseLive == null)
            courseLive = Transformations.map(
                    fireBaseRepo.getCourse(courseId),
                    new Function<Course, CourseExt>() {
                        @Override
                        public CourseExt apply(Course course) {
                            if (course == null) return null;
                            return new CourseExt(
                                    course.getKey(), course.getProfile(), course.getMembers()
                            );
                        }
                    }
            );
        return courseLive;
    }

}
