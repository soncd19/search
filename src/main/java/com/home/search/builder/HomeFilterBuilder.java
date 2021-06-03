package com.home.search.builder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by soncd on 18/01/2019
 */
public class HomeFilterBuilder {

    private Operators operator = Operators.AND;
    private List<String> filters = new ArrayList<>();

    public HomeFilterBuilder() {

    }

    public HomeFilterBuilder setFilter(String key, String value) {
        String filter = key + ":" + value;
        filters.add(filter);
        return this;
    }

    public HomeFilterBuilder setOperator(Operators operator) {
        this.operator = operator;
        return this;
    }

    public String build() {
        StringBuilder builder = new StringBuilder();
        String typeOperator = null;
        if (filters.size() == 0) {
            return "";
        }
        switch (operator) {
            case AND:
                typeOperator = "AND";
                break;
            case OR:
                typeOperator = "OR";
                break;
        }

        for (int i = 0; i < filters.size(); i++) {
            if (i == filters.size() - 1) {
                builder.append("(").append(filters.get(i)).append(")");
            } else {
                builder.append("(").append(filters.get(i)).append(") ").append(typeOperator).append(" ");
            }
        }

        return builder.toString();
    }
    public enum Operators {
        AND,
        OR,
        NOT
    }
}
