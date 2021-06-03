package com.home.search.service;

import com.home.search.builder.ElasticBuilder;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by soncd on 12/02/2019
 */
public class ElasticIndexService implements ElasticService<String> {
    private RestHighLevelClient restHighLevelClient;
    private ElasticBuilder builder = new ElasticBuilder();
    private List<JSONObject> objects;

    public ElasticIndexService setData(List<JSONObject> objects) {
        this.objects = objects;
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
            BulkRequest bulkRequest = builder.buildBulkRequest();
            objects.forEach(document -> {
                try {
                    IndexRequest request = builder.buildIndexRequest();
                    bulkRequest.add(request.source(document.toString(), XContentType.JSON));
                } catch (Exception e) {
                    log.error("builder document error with document = " + document.toString());
                }
            });
            BulkResponse bulkResponses = null;
            try {
                bulkResponses = restHighLevelClient.bulk(bulkRequest);
            } catch (Exception e) {
                log.error("bulk error document = " + bulkRequest.payloads().get(0));
            }

            if (bulkResponses != null && bulkResponses.hasFailures()) {
                for (BulkItemResponse bulkItemResponse : bulkResponses) {
                    if (bulkItemResponse.isFailed()) {
                        BulkItemResponse.Failure failure = bulkItemResponse.getFailure();
                        log.error("bulk item Error " + failure.toString());
                    }
                }
            }
            return "success";
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    private Logger log = LoggerFactory.getLogger(ElasticIndexService.class);

}
