package com.example.exercise.myfavoritemovies.com.example.exercise.myfavoritemovies.Model;

import java.util.HashMap;
import java.util.Map;

public class Reviews {

    public Integer id;
    public Integer page;
    public Review[] results = null;
    public Integer totalPages;
    public Integer totalResults;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public Review[] getVideoReviews() {
        return results;
    }
}