package com.home.search.response;

import java.util.List;

/**
 * Created by soncd on 13/02/2019
 */
public class Result {
    private long totalHits;

    private float maxScore;

    private List<Document> documents;

    public Result() {

    }

    public Result(long totalHits, float maxScore, List<Document> documents) {
        this.totalHits = totalHits;
        this.maxScore = maxScore;
        this.documents = documents;
    }

    public long getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(long totalHits) {
        this.totalHits = totalHits;
    }

    public float getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(float maxScore) {
        this.maxScore = maxScore;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }
}
