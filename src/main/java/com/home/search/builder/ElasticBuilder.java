package com.home.search.builder;

import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.update.UpdateRequest;

/**
 * Created by soncd on 20/12/2018
 */
public class ElasticBuilder {
    private String index;
    private String type;

    public ElasticBuilder() {

    }

    public ElasticBuilder(String index, String type) {
        this.index = index;
        this.type = type;
    }

    public ElasticBuilder setIndex(String index) {
        this.index = index;
        return this;
    }

    public ElasticBuilder setType(String type) {
        this.type = type;
        return this;
    }

    public IndexRequest buildIndexRequest() {
        return new IndexRequest(index, type);
    }

    public BulkRequest buildBulkRequest() {
        return new BulkRequest();
    }

    public UpdateRequest buildUpdateRequest(String docId) {
        return new UpdateRequest(index, type, docId);
    }

    public DeleteRequest buildDeleteRequest(String docId) {
        return new DeleteRequest(index, type, docId);
    }

    public SearchRequest buildSearchRequest() {
        return new SearchRequest(index);
    }
}
