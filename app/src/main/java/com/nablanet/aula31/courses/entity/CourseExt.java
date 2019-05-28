package com.nablanet.aula31.courses.entity;

import com.google.firebase.database.Exclude;
import com.nablanet.aula31.repo.entity.Member;

import java.util.Map;

public class CourseExt {

    @Exclude
    public String id;

    public CourseProfileExt profile;
    public Map<String, Member> members;

    private CourseExt(){}

}
