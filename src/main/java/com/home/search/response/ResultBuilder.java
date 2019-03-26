package com.home.search.response;

import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by soncd on 13/02/2019
 */
public class ResultBuilder {
    private SearchHits searchHits;

    public ResultBuilder setSearchHits(SearchHits searchHits) {
        this.searchHits = searchHits;
        return this;
    }

    public Result build() {
        List<Document> documents = new ArrayList<>();
        SearchHit[] hits = searchHits.getHits();
        for (int i = 0; i < hits.length; i++) {
            SearchHit hit = hits[i];
            Document document = new Document(hit.getVersion(), hit.getScore(), hit.getSourceAsMap());
            documents.add(document);
        }
        return new Result(searchHits.getTotalHits(), searchHits.getMaxScore(), documents);
    }
}
