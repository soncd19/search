package com.home.search.service;

import com.home.search.response.Result;
import com.home.search.response.ResultBuilder;
import com.home.search.exception.IndexException;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * Created by soncd on 20/12/2018
 */
@Component
public class HomeSearchService {

    private final RestHighLevelClient restHighLevelClient;
    private final TransportClient transportClient;
    private String index;
    private String type;

    @Autowired
    public HomeSearchService(RestHighLevelClient restHighLevelClient, TransportClient transportClient) {
        this.restHighLevelClient = restHighLevelClient;
        this.transportClient = transportClient;
    }

    public void setConfig(String index, String type) {
        this.index = index;
        this.type = type;
    }

    /**
     * Đánh index cho nhiều tài liệu cùng lúc
     *
     * @param jsonDocuments
     * @return
     * @throws IndexException
     */
    public void indexs(List<JSONObject> jsonDocuments) throws IndexException {
        ElasticExecutorService<String> service = new ElasticExecutorService<>();
        service.setElasticService(new ElasticIndexService()
                .setData(jsonDocuments)
                .setConfig(index, type)
                .setRestHighLevelClient(restHighLevelClient));
        try {
            service.execute(jsonDocuments);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Cập nhật lại tài liệu
     *
     * @param indexId
     * @param jsonDocuments
     * @return
     * @throws IndexException
     */
    public void update(String indexId, JSONObject jsonDocuments) throws IndexException {
        try {
            ElasticExecutorService<String> service = new ElasticExecutorService<>();
            service.setElasticService(new ElasticUpdateService()
                    .setIndexId(indexId)
                    .setData(jsonDocuments.toString())
                    .setConfig(index, type)
                    .setRestHighLevelClient(restHighLevelClient));

            service.execute();
        } catch (Exception e) {
            throw new IndexException("update document error = " + e.getMessage());
        }
    }

    /**
     * Xóa tài liệu tương ứng với id
     *
     * @param indexId
     */
    public void delete(String indexId) throws IndexException {
        try {
            ElasticExecutorService<String> service = new ElasticExecutorService<>();
            service.setElasticService(new ElasticDeleteService()
                    .setIndexId(indexId)
                    .setConfig(index, type)
                    .setRestHighLevelClient(restHighLevelClient));
            service.execute();
        } catch (Exception e) {
            throw new IndexException("delete documentId = " + indexId + " error = " + e.getMessage());
        }
    }

    /**
     * Tìm tài liệu và trả về thông tin của tài liệu
     *
     * @param sourceBuilder
     * @return
     * @throws IOException
     */
    public SearchHit[] get(SearchSourceBuilder sourceBuilder) throws IOException {
        ElasticExecutorService<SearchHits> service = new ElasticExecutorService<>();
        service.setElasticService(new ElasticQueryService()
                .setSearchSourceBuilder(sourceBuilder)
                .setConfig(index, type)
                .setRestHighLevelClient(restHighLevelClient));
        try {
            SearchHits searchHits = service.execute();
            return searchHits.getHits();

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IOException(e);
        }
    }

    /**
     * Tìm kiếm trả về kết quả
     *
     * @param sourceBuilder
     * @return
     * @throws IOException
     */
    public Result search(SearchSourceBuilder sourceBuilder) throws IOException {
        ElasticExecutorService<SearchHits> service = new ElasticExecutorService<>();
        service.setElasticService(new ElasticQueryService()
                .setSearchSourceBuilder(sourceBuilder)
                .setConfig(index, type)
                .setRestHighLevelClient(restHighLevelClient));
        try {
            SearchHits searchHits = service.execute();
            return new ResultBuilder().setSearchHits(searchHits).build();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IOException(e);
        }
    }

    /**
     * Gợi ý theo trường cung cấp và từ được gợi ý
     *
     * @param fieldNames
     * @param suggest
     * @return
     * @throws Exception
     */
    public List<String> getSuggestions(List<String> fieldNames, String suggest) throws Exception {
        ElasticExecutorService<List<String>> service = new ElasticExecutorService<>();
        service.setElasticService(new ElasticSuggestService()
                .setTransportClient(transportClient)
                .setFieldNames(fieldNames)
                .setSuggest(suggest)
                .setConfig(index, type));
        return service.execute();
    }

    private Logger log = LoggerFactory.getLogger(HomeSearchService.class);
}
