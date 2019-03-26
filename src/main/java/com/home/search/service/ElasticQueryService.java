package com.home.search.service;

import com.home.search.builder.ElasticBuilder;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by soncd on 12/02/2019
 */
public class ElasticQueryService implements ElasticService<SearchHits> {

    private ElasticBuilder builder = new ElasticBuilder();
    private RestHighLevelClient restHighLevelClient;
    private SearchSourceBuilder sourceBuilder;

    public ElasticService setSearchSourceBuilder(SearchSourceBuilder searchSourceBuilder) {
        this.sourceBuilder = searchSourceBuilder;
        return this;
    }

    @Override
    public ElasticService setRestHighLevelClient(RestHighLevelClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
        return this;
    }

    @Override
    public ElasticService setConfig(String index, String type) {
        builder.setIndex(index);
        builder.setType(type);
        return this;
    }

    @Override
    public SearchHits execute(Object... params) {
        try {
            SearchRequest request = builder.buildSearchRequest().source(sourceBuilder);
            SearchResponse searchResponse = restHighLevelClient.search(request);
            return searchResponse.getHits();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    private Logger log = LoggerFactory.getLogger(ElasticQueryService.class);
}
