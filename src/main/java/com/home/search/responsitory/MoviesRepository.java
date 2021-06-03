package com.home.search.responsitory;

import com.home.search.builder.HomeFilterBuilder;
import com.home.search.response.Result;
import com.home.search.exception.IndexException;
import com.home.search.builder.HomeSourceBuilder;
import com.home.search.service.HomeSearchService;
import com.home.search.utils.TypeSearch;
import org.elasticsearch.index.query.AbstractQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
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
public class MoviesRepository {

    private String index = TypeSearch.MOVIES.getIndex();
    private String type = TypeSearch.MOVIES.getType();

    @Autowired
    private HomeSearchService searchService;

    @Autowired
    private HomeSourceBuilder hsSourceBuilder;

    /**
     * index phim
     *
     * @param jsonMovies
     * @throws IndexException
     */
    public void indexMovies(List<JSONObject> jsonMovies) throws IndexException {
        searchService.setConfig(index, type);
        searchService.indexs(jsonMovies);
    }

    /**
     * Cập nhật lại phim
     *
     * @param docId
     * @param jsonDocuments
     * @return
     * @throws IndexException
     */
    public void updateMovies(long docId, JSONObject jsonDocuments) throws IndexException {
        try {
            searchService.setConfig(index, type);
            AbstractQueryBuilder builder = QueryBuilders.matchPhraseQuery("docId", docId);
            SearchHit[] searchHits = searchService.get(hsSourceBuilder.setQueryBuilder(builder).build());
            if (searchHits == null || searchHits.length == 0) {
                List<JSONObject> documents = new ArrayList<>();
                documents.add(jsonDocuments);
                searchService.indexs(documents);
            } else {
                SearchHit hit = searchHits[0];
                searchService.update(hit.getId(), jsonDocuments);
            }
        } catch (Exception e) {
            throw new IndexException("update document error = " + e.getMessage());
        }
    }

    /**
     * Tìm kiếm phim theo tên, tác giả, diễn viên
     *
     * @param query
     * @param from
     * @return
     * @throws IOException
     */
    public Result queryMovies(String query, int from) throws IOException {
        searchService.setConfig(index, type);
        MultiMatchQueryBuilder builder = QueryBuilders.multiMatchQuery(query,
                "name", "englishName", "tags", "director", "actors", "domain");
        builder.field("name", 10);
        builder.field("englishName", 10);
        builder.field("tags", 10);
        builder.maxExpansions(5);
        builder.prefixLength(5);
        builder.tieBreaker(20);
        builder.type(MultiMatchQueryBuilder.Type.BEST_FIELDS);
        builder.boost(10);
        /*AbstractQueryBuilder builder = QueryBuilders.queryStringQuery(new HomeFilterBuilder()
                .setFilter("name", query)
                .setFilter("englishName", query)
                .setFilter("director", query)
                .setFilter("actors", query)
                .setOperator(HomeFilterBuilder.Operators.OR).build()).autoGenerateSynonymsPhraseQuery(true);*/
        return searchService.search(hsSourceBuilder.setFrom(from).setQueryBuilder(builder).build());

    }

    /**
     * Lọc tìm phim
     *
     * @return
     * @throws IndexException
     */
    public Result filterMovies(int from, String similar, String country, String year, String domain) throws IOException {
        AbstractQueryBuilder abstractQueryBuilder = QueryBuilders.matchAllQuery();
        HomeFilterBuilder builder = new HomeFilterBuilder();
        if (!similar.isEmpty()) {
            builder.setFilter("similar", similar);
        }
        if (!country.isEmpty()) {
            builder.setFilter("country", country);
        }
        if (!year.isEmpty()) {
            builder.setFilter("year", year);
        }
        if (!domain.isEmpty()) {
            builder.setFilter("domain", domain);
        }
        String query = builder.build();
        if (!query.isEmpty()) {
            abstractQueryBuilder = QueryBuilders.queryStringQuery(builder.build());
        }
        searchService.setConfig(index, type);
        return searchService.search(hsSourceBuilder
                .setFrom(from)
                .setQueryBuilder(abstractQueryBuilder)
                .build());
    }

    /**
     * Xóa phim tương ứng với id
     *
     * @param docId
     */
    public void deleteMovie(long docId) throws IOException, IndexException {
        searchService.setConfig(index, type);
        AbstractQueryBuilder builder = QueryBuilders.termQuery("docId", docId);
        SearchHit[] searchHits = searchService.get(hsSourceBuilder.setQueryBuilder(builder).build());
        if (searchHits != null && searchHits.length > 0) {
            SearchHit searchHit = searchHits[0];
            searchService.delete(searchHit.getId());
        }
    }

    /**
     * Gợi ý tên phim theo tên và tên tiếng anh...
     *
     * @param suggest
     * @return
     * @throws Exception
     */
    public List<String> suggestMovies(String suggest) throws Exception {
        searchService.setConfig(index, type);
        List<String> fieldNames = new ArrayList<>();
        fieldNames.add("name");
        fieldNames.add("englishName");
        //fieldNames.add("tags");
        return searchService.getSuggestions(fieldNames, suggest);
    }
}
