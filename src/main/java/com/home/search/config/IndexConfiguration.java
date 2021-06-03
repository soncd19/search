package com.home.search.config;

import com.home.search.properties.ConfigProps;
import com.home.search.properties.Index;
import com.home.search.utils.FileUtils;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import java.io.IOException;
import java.util.List;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * Created by soncd on 07/12/2018
 */

@Component
@Profile("production")
public class IndexConfiguration {
    private final TransportClient transportClient;
    private final ConfigProps configProps;
    private Logger log = LoggerFactory.getLogger(IndexConfiguration.class);

    public IndexConfiguration(TransportClient transportClient, ConfigProps configProps) {
        this.transportClient = transportClient;
        this.configProps = configProps;
    }

    @PostConstruct
    private void createIndexWithMapping() {
        IndicesAdminClient indicesAdminClient = transportClient.admin().indices();

        Index index = configProps.getIndex();
        List<String> names = index.getNames();

        names.forEach(name -> {

            IndicesExistsRequest indicesExistsRequest = new IndicesExistsRequest(name);
            IndicesExistsResponse indicesExistsResponse = indicesAdminClient.exists(indicesExistsRequest).actionGet();

            if (!indicesExistsResponse.isExists()) {
                indicesAdminClient.prepareCreate(name)
                        .setSettings(Settings.builder()
                                .put("index.number_of_shards", index.getShard())
                                .put("index.number_of_replicas", index.getReplica()))
                        .get();

                try {
                    String indexMapping = IndexMapping.mapping(name);
                    indicesAdminClient.preparePutMapping(name)
                            .setType(index.getType())
                            .setSource(indexMapping, XContentType.JSON)
                            .get();

                } catch (Exception e) {
                    log.error("create builder mapping error = " + e.getMessage());
                }
            }
        });
    }

    private static class IndexMapping {

        private static String sIndexPath = System.getProperty("user.dir") + "/src/main/resources/templates/";

        private static String mapping(String indexName) throws IOException {
            String filePath = indexNameToJsonIndex(indexName);
            return FileUtils.fileToString(filePath);
        }

        private static String indexNameToJsonIndex(String name) {
            return sIndexPath + name + ".json";
        }
    }
}
