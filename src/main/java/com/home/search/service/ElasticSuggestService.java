package com.home.search.service;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by soncd on 14/02/2019
 */
public class ElasticSuggestService implements ElasticService<List<String>> {

    private String index;
    private String type;
    private TransportClient transportClient;
    private List<String> fieldNames;
    private String suggest;

    public ElasticSuggestService setTransportClient(TransportClient transportClient) {
        this.transportClient = transportClient;
        return this;
    }

    public ElasticSuggestService setFieldNames(List<String> fieldNames) {
        this.fieldNames = fieldNames;
        return this;
    }

    public ElasticSuggestService setSuggest(String suggest) {
        this.suggest = suggest;
        return this;
    }

    @Override
    public ElasticService setConfig(String index, String type) {
        this.index = index;
        this.type = type;
        return this;
    }

    @Override
    public ElasticService setRestHighLevelClient(RestHighLevelClient restHighLevelClient) {
        return null;
    }

    @Override
    public List<String> execute(Object... params) {
        List<String> suggests = new ArrayList<>();
        try {
            SearchRequestBuilder searchRequestBuilder = transportClient.prepareSearch(index);
            SuggestBuilder suggestBuilder = new SuggestBuilder();
            if (fieldNames == null || fieldNames.size() == 0) {
                return new ArrayList<>();
            }
            fieldNames.forEach(fieldName -> {
                suggestBuilder.addSuggestion(fieldName,
                        SuggestBuilders.completionSuggestion(fieldName).size(10).prefix(suggest));
            });

            SearchResponse response = searchRequestBuilder.suggest(suggestBuilder).get();
            Suggest suggest = response.getSuggest();

            fieldNames.forEach(fieldName -> {
                List entries = suggest.getSuggestion(fieldName).getEntries();
                for (int i = 0; i < entries.size(); i++) {
                    try {
                        CompletionSuggestion.Entry entry = (CompletionSuggestion.Entry) entries.get(i);
                        List<CompletionSuggestion.Entry.Option> options = entry.getOptions();
                        options.forEach(option -> {
                            String suggested = option.getText().string();
                            if (!suggests.contains(suggested)) {
                                suggests.add(suggested);
                            }
                        });
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                }
            });

        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return suggests;
    }

    private Logger log = LoggerFactory.getLogger(ElasticSuggestService.class);
}
