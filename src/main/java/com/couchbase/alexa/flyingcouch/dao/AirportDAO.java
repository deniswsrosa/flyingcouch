package com.couchbase.alexa.flyingcouch.dao;

import com.couchbase.alexa.flyingcouch.model.Airport;
import com.couchbase.alexa.flyingcouch.model.User;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.N1qlParams;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryResult;
import com.couchbase.client.java.query.N1qlQueryRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AirportDAO {


    @Autowired
    private Bucket bucket;

    public Airport findAirport(String airport) {
        N1qlParams params = N1qlParams.build().adhoc(false);
        //we only care about the first match
        N1qlQuery query = N1qlQuery.simple("select t.faa as faa, t.airportname as airportname from `"+bucket.name()+"` t " +
                " where  ( LOWER(t.airportname) = '"+airport.toLowerCase()+"' or  LOWER(t.faa) = '"+airport.toLowerCase()+"' ) and t.type = 'airport' limit 1 ", params);

        return getAirport(bucket.query(query));
    }


    public Airport getAirport(String airport) {
        N1qlParams params = N1qlParams.build().adhoc(false);
        N1qlQuery query = N1qlQuery.simple("select t.faa as faa, t.airportname as airportname from `"+bucket.name()+"` t " +
                " where  LOWER(t.faa) = '"+airport.toLowerCase()+"' and t.type = 'airport' limit 1 ", params);

        return getAirport(bucket.query(query));


    }

    private Airport getAirport(N1qlQueryResult result) {

        List<N1qlQueryRow> rows = result.allRows();

        if(rows.isEmpty()) {
            return  null;
        } else {
            //let's ignore if we have more then one match
            JsonObject json = rows.get(0).value();
            Airport air = new Airport();
            air.setFaa(json.getString("faa"));
            air.setAirportname(json.getString("airportname"));
            return  air;
        }
    }
}
