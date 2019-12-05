package com.couchbase.alexa.flyingcouch.model;

import lombok.Data;

@Data
public class User {


    private String id;
    private String alexaId;
    private String airport;
    private String type = "user";
}
