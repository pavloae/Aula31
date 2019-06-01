package com.nablanet.aula31.export.viewmodel;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.nablanet.aula31.ExecutorFactory;
import com.nablanet.aula31.export.entity.CourseExport;
import com.nablanet.aula31.repo.FireBaseRepo;
import com.nablanet.aula31.repo.entity.Course;

import java.util.concurrent.Executor;

public class CourseViewModel extends ViewModel {

    public Executor executor = ExecutorFactory.getInstanceSingleThreadExecutor();
    FireBaseRepo fireBaseRepo = FireBaseRepo.getInstance();

    private LiveData<CourseExport> courseLive;

    @NonNull
    public LiveData<CourseExport> getCourseExportLiveData(@NonNull String courseId) {
        if (courseLive == null)
            courseLive = Transformations.map(
                    fireBaseRepo.getCourse(courseId),
                    new Function<Course, CourseExport>() {
                        @Override
                        public CourseExport apply(Course course) {
                            if (course == null) return null;
                            return new CourseExport(
                                    course.course_id, course.profile, course.members
                            );
                        }
                    }
            );
        return courseLive;
    }

}
