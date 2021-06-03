package com.home.search.service;

import com.home.search.builder.ElasticBuilder;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by soncd on 12/02/2019
 */
public class ElasticDeleteService implements ElasticService<String> {

    private RestHighLevelClient restHighLevelClient;
    private ElasticBuilder builder = new ElasticBuilder();
    private String indexId;

    @Override
    public ElasticService setConfig(String index, String type) {
        builder.setIndex(index);
        builder.setType(type);
        return this;
    }

    @Override
    public ElasticService setRestHighLevelClient(RestHighLevelClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
        return this;
    }

    public ElasticDeleteService setIndexId(String indexId) {
        this.indexId = indexId;
        return this;
    }

    @Override
    public String execute(Object... params) {
        try {
            DeleteRequest deleteRequest = builder.buildDeleteRequest(indexId);
            restHighLevelClient.deleteAsync(deleteRequest, RequestOptions.DEFAULT,
                    new ActionListener<DeleteResponse>() {
                        @Override
                        public void onResponse(DeleteResponse deleteResponse) {
                            log.info("delete document by documentID = " + indexId);
                        }

                        @Override
                        public void onFailure(Exception e) {
                            log.error(e.getMessage());
                        }
                    });
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    private Logger log = LoggerFactory.getLogger(ElasticDeleteService.class);
}
