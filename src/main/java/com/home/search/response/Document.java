package com.home.search.response;

import java.util.Map;

/**
 * Created by soncd on 31/01/2019
 */
public class Document {
    private long version;
    private float score;
    private Map<String, Object> payload;

    public Document() {

    }

    public Document(long version, float score, Map<String, Object> payload) {
        this.version = version;
        this.score = score;
        this.payload = payload;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }

    public void setPayload(Map<String, Object> payload) {
        this.payload = payload;
    }
}
