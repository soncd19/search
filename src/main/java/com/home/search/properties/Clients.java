package com.home.search.properties;

/**
 * Created by soncd on 07/12/2018
 */

public class Clients {
    private String hostname;
    private String scheme;
    private int httpPort;
    private int containerPort;
    private int transportPort;


    public String getHostname() {
        return hostname;
    }

    public String getScheme() {
        return scheme;
    }

    public int getHttpPort() {
        return httpPort;
    }

    public int getContainerPort() {
        return containerPort;
    }

    public int getTransportPort() {
        return transportPort;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public void setHttpPort(int httpPort) {
        this.httpPort = httpPort;
    }

    public void setContainerPort(int containerPort) {
        this.containerPort = containerPort;
    }

    public void setTransportPort(int transportPort) {
        this.transportPort = transportPort;
    }
}
