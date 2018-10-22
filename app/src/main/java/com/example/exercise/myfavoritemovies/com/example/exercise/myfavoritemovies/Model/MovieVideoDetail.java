package com.example.exercise.myfavoritemovies.com.example.exercise.myfavoritemovies.Model;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieVideoDetail {

    private Integer id;
    @SerializedName("results")
    private VideoDetail[] videoDetails = null;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public VideoDetail[] getVideoDetails() {
        return videoDetails;
    }

    public void setVideoDetails(VideoDetail[] videoDetails) {
        this.videoDetails = videoDetails;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}


