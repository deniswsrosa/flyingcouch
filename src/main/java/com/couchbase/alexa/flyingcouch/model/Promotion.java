package com.couchbase.alexa.flyingcouch.model;

import lombok.Data;

import java.util.List;

@Data
public class Promotion {

    private String id;
    private String originAirport;
    private List<Offer> offers;
}
