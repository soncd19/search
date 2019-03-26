package com.home.search.properties;

import java.util.List;

/**
 * Created by soncd on 07/12/2018
 */
public class Index {
    private String cluster;
    private List<String> names;
    private String type;
    private int shard;
    private int replica;
    private int from;
    private int size;
    private int timeout;

    public String getCluster() {
        return cluster;
    }

    public List<String> getNames() {
        return names;
    }

    public String getType() {
        return type;
    }

    public int getShard() {
        return shard;
    }

    public int getReplica() {
        return replica;
    }

    public int getFrom() {
        return from;
    }

    public int getSize() {
        return size;
    }

    public int getTimeout() {
        return timeout;
    }
    
    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setShard(int shard) {
        this.shard = shard;
    }

    public void setReplica(int replica) {
        this.replica = replica;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
