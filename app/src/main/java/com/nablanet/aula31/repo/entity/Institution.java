package com.nablanet.aula31.repo.entity;

import java.util.HashMap;
import java.util.Map;

public class Institution {

    public String id;
    public String name;

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("fullName", name);
        return map;
    }

}
