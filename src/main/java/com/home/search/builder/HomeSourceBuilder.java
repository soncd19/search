package com.home.search.builder;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Created by soncd on 21/01/2019
 */
@Component
public class HomeSourceBuilder {
    private final SearchSourceBuilder sourceBuilder;

    @Autowired
    public HomeSourceBuilder(SearchSourceBuilder sourceBuilder) {
        this.sourceBuilder = sourceBuilder;

    }

    public HomeSourceBuilder setFrom(int from) {
        sourceBuilder.from(from);
        return this;
    }

    public HomeSourceBuilder sort(String name, SortOrder sortOrder) {
        sourceBuilder.sort(name, sortOrder);
        return this;
    }

    public HomeSourceBuilder setQueryBuilder(QueryBuilder queryBuilder) {
        sourceBuilder.query(queryBuilder);
        return this;
    }

    public SearchSourceBuilder build() {
        return sourceBuilder;
    }
}
