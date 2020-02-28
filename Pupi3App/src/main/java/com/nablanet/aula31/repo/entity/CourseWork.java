package com.nablanet.aula31.repo.entity;

import androidx.annotation.Nullable;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Clase para la parametrización de los valores en "/repository/$work_id/"
 */
public class CourseWork extends KeyImpl {

    @Exclude public static final int HOMEWORK = 0;
    @Exclude public static final int CLASSWORK = 1;

    @Nullable private String course_id;
    @Nullable private Integer type;
    @Nullable private Integer number;
    @Nullable private String modality;
    @Nullable private String name;
    @Nullable private Long date;
    @Nullable private String topics;
    @Nullable private String resources_url;
    @Nullable private Criteria criteria1;
    @Nullable private Criteria criteria2;
    @Nullable private Criteria criteria3;

    @Nullable
    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(@Nullable String course_id) {
        this.course_id = course_id;
    }

    @Nullable
    public Integer getType() {
        return type;
    }

    public void setType(@Nullable Integer type) {
        this.type = type;
    }

    @Nullable
    public Integer getNumber() {
        return number;
    }

    public void setNumber(@Nullable Integer number) {
        this.number = number;
    }

    @Nullable
    public String getModality() {
        return modality;
    }

    public void setModality(@Nullable String modality) {
        this.modality = modality;
    }

    @Nullable
    public String getName() {
        return name;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    @Nullable
    public Long getDate() {
        return date;
    }

    public void setDate(@Nullable Long date) {
        this.date = date;
    }

    @Nullable
    public String getTopics() {
        return topics;
    }

    public void setTopics(@Nullable String topics) {
        this.topics = topics;
    }

    @Nullable
    public String getResources_url() {
        return resources_url;
    }

    public void setResources_url(@Nullable String resources_url) {
        this.resources_url = resources_url;
    }

    @Nullable
    public Criteria getCriteria1() {
        return criteria1;
    }

    public void setCriteria1(@Nullable Criteria criteria1) {
        this.criteria1 = criteria1;
    }

    @Nullable
    public Criteria getCriteria2() {
        return criteria2;
    }

    public void setCriteria2(@Nullable Criteria criteria2) {
        this.criteria2 = criteria2;
    }

    @Nullable
    public Criteria getCriteria3() {
        return criteria3;
    }

    public void setCriteria3(@Nullable Criteria criteria3) {
        this.criteria3 = criteria3;
    }

    @Exclude
    Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        if (course_id != null) map.put("courseId", course_id);
        if (type != null) map.put("type", type);
        if (modality != null) map.put("modality", modality);
        if (name != null) map.put("fullName", name);
        if (date != null) map.put("date", date);
        if (topics != null) map.put("topics", topics);
        if (resources_url != null) map.put("resources_url", resources_url);
        if (criteria1 != null) map.put("criteria1", criteria1.toMap());
        if (criteria2 != null) map.put("criteria2", criteria2.toMap());
        if (criteria3 != null) map.put("criteria3", criteria3.toMap());
        return map;
    }

}
