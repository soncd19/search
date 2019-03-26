package com.home.search.responsitory;

import com.home.search.builder.HomeFilterBuilder;
import com.home.search.response.Result;
import com.home.search.exception.IndexException;
import com.home.search.builder.HomeSourceBuilder;
import com.home.search.service.HomeSearchService;
import com.home.search.utils.TypeSearch;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.AbstractQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by soncd on 18/01/2019
 */

@Component
public class MusicRepository {

    private String index = TypeSearch.MUSIC.getIndex();
    private String type = TypeSearch.MUSIC.getType();

    @Autowired
    private HomeSearchService searchService;
    @Autowired
    private HomeSourceBuilder homeSourceBuilder;

    public void indexMusic(List<JSONObject> musics) throws IndexException {
        searchService.setConfig(index, type);
        searchService.indexs(musics);
    }

    public Result queryMusics(String query, int from) throws IOException {
        searchService.setConfig(index, type);
        MultiMatchQueryBuilder builder = QueryBuilders.multiMatchQuery(query,"singers", "lyric", "similar", "country");
        builder.field("name", 10);
        builder.field("tags", 10);
        builder.maxExpansions(10);
        builder.prefixLength(10);
        builder.tieBreaker(10);
        builder.type(MultiMatchQueryBuilder.Type.BEST_FIELDS);
        builder.boost(10);
        return searchService.search(homeSourceBuilder
                .setFrom(from)
                .setQueryBuilder(builder)
                .build());
    }

    public Result filterMusics(int from, String singer, String country, String topic, String composed) throws IOException {
        AbstractQueryBuilder abstractQueryBuilder = QueryBuilders.matchAllQuery();
        HomeFilterBuilder builder = new HomeFilterBuilder();
        if (!singer.isEmpty()) {
            builder.setFilter("singers", singer);
        }
        if (!country.isEmpty()) {
            builder.setFilter("country", country);
        }
        if (!topic.isEmpty()) {
            builder.setFilter("topic", topic);
        }
        if (!composed.isEmpty()) {
            builder.setFilter("composed", composed);
        }
        String query = builder.build();
        if (!query.isEmpty()) {
            abstractQueryBuilder = QueryBuilders.queryStringQuery(builder.build());
        }
        searchService.setConfig(index, type);
        return searchService.search(homeSourceBuilder
                .setFrom(from)
                .setQueryBuilder(abstractQueryBuilder)
                .build());
    }

    /**
     * gợi ý tìm kiếm nhạc
     *
     * @param suggest
     * @return
     * @throws Exception
     */
    public List<String> suggestMusic(String suggest) throws Exception {
        searchService.setConfig(index, type);
        List<String> fieldNames = new ArrayList<>();
        fieldNames.add("name");
        fieldNames.add("suggest");
        return searchService.getSuggestions(fieldNames, suggest);
    }
}
