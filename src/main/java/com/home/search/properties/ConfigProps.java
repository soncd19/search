package com.home.search.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

/**
 * Created by soncd on 07/12/2018
 */

@Component
@ConfigurationProperties("app")
public class ConfigProps {

    @NestedConfigurationProperty
    private Clients clients = new Clients();

    @NestedConfigurationProperty
    private ElasticContainer elasticContainer = new ElasticContainer();

    @NestedConfigurationProperty
    private Index index = new Index();

    public Clients getClients() {
        return clients;
    }

    public ElasticContainer getElasticContainer() {
        return elasticContainer;
    }

    public Index getIndex() {
        return index;
    }
}
