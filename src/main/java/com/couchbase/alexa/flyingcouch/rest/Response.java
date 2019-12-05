package com.couchbase.alexa.flyingcouch.rest;

import lombok.Data;

@Data
public class Response {

    private String responseType;
    private String message;
    private Object value;
}
