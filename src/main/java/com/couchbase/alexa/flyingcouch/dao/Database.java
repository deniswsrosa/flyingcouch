package com.couchbase.alexa.flyingcouch.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;


@Configuration
public class Database {


    private Cluster cluster;
    private Bucket bucket;


    @Bean
    public Bucket getBucket() {

        if(bucket == null ) {
            cluster = CouchbaseCluster.create("127.0.0.1");
            cluster.authenticate("Administrator", "password");
            bucket = cluster.openBucket("travel-sample");

            System.out.println("------------------ criando novo bucket");
        }

        return bucket;
    }
}
