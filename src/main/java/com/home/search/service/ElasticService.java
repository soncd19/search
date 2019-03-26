package com.home.search.service;

import org.elasticsearch.client.RestHighLevelClient;

/**
 * Created by soncd on 12/02/2019
 */
public interface ElasticService<T> {

    ElasticService setConfig(String index, String type);

    ElasticService setRestHighLevelClient(RestHighLevelClient restHighLevelClient);

    T execute(Object...params);
}
