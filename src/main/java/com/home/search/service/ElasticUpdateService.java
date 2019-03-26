package com.home.search.service;

import com.home.search.builder.ElasticBuilder;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by soncd on 12/02/2019
 */
public class ElasticUpdateService implements ElasticService<String> {
    private RestHighLevelClient restHighLevelClient;
    private ElasticBuilder builder = new ElasticBuilder();
    private String indexId;
    private String data;

    public ElasticUpdateService setIndexId(String indexId) {
        this.indexId = indexId;
        return this;
    }

    public ElasticUpdateService setData(String data) {
        this.data = data;
        return this;
    }

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

    @Override
    public String execute(Object... params) {
        try {
            UpdateRequest request = builder.buildUpdateRequest(indexId);
            request.doc(data, XContentType.JSON);
            restHighLevelClient.updateAsync(request, RequestOptions.DEFAULT,
                    new ActionListener<UpdateResponse>() {
                        @Override
                        public void onResponse(UpdateResponse updateResponse) {
                            log.info("update document success");
                        }

                        @Override
                        public void onFailure(Exception e) {
                            log.error("update error = " + e.getMessage());
                        }
                    });
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    private Logger log = LoggerFactory.getLogger(ElasticUpdateService.class);
}
