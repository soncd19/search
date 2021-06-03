package com.home.search.utils;

/**
 * Created by soncd on 19/12/2018
 */
public enum TypeSearch {

    MOVIES("movies", "doc"),
    MUSIC("musics", "doc");

    private String index;
    private String type;

    TypeSearch(String index, String type) {
        this.index = index;
        this.type = type;
    }

    public String getIndex() {
        return index;
    }

    public String getType() {
        return type;
    }

    public static TypeSearch getType(String type) {
        for (int i = 0; i < values().length; i++) {
            if (values()[i].index.equalsIgnoreCase(type)) {
                return values()[i];
            }
        }
        return null;
    }

    public static String getIndex(String type) {
        TypeSearch typeSearch = getType(type);
        return typeSearch != null ? typeSearch.getIndex() : "";
    }
}
