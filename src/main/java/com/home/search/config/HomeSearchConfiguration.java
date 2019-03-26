package com.home.search.config;

import com.home.search.properties.ConfigProps;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * Created by soncd on 07/12/2018
 */

@Configuration
public class HomeSearchConfiguration {

    @Autowired
    private ConfigProps configProps;

    @Profile("production")
    @Bean(destroyMethod = "close")
    @SuppressWarnings("all")
    public TransportClient getTransportClient() throws UnknownHostException {
        Settings settings = Settings.builder()
                .put("cluster.name", configProps.getIndex().getCluster())
                .build();
        return new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName(configProps.getClients().getHostname()),
                        configProps.getClients().getTransportPort()));
    }

    @Profile({"production"})
    @Bean(destroyMethod = "close")
    public RestHighLevelClient getRestClient() {
        return new RestHighLevelClient(RestClient.builder(new HttpHost(configProps.getClients().getHostname(),
                configProps.getClients().getHttpPort(), configProps.getClients().getScheme())));
    }

    @Bean
    public SearchSourceBuilder getSearchSourceBuilder() {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.from(configProps.getIndex().getFrom());
        sourceBuilder.size(configProps.getIndex().getSize());
        sourceBuilder.timeout(new TimeValue(configProps.getIndex().getTimeout(), TimeUnit.SECONDS));

        return sourceBuilder;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedMethods(Arrays.asList("DELETE", "GET", "POST", "PATCH", "PUT"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
