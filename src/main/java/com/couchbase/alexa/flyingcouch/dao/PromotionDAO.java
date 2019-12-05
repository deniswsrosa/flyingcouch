package com.couchbase.alexa.flyingcouch.dao;


import com.couchbase.alexa.flyingcouch.model.Airport;
import com.couchbase.alexa.flyingcouch.model.Offer;
import com.couchbase.alexa.flyingcouch.model.Promotion;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.N1qlParams;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryResult;
import com.couchbase.client.java.query.N1qlQueryRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PromotionDAO {

    @Autowired
    private Bucket bucket;


    public Promotion getPromotion(String airport) {
        N1qlParams params = N1qlParams.build().adhoc(false);
        N1qlQuery query = N1qlQuery.simple("select * from `"+bucket.name()+"` " +
                " where  LOWER(originAirport) = '"+airport.toLowerCase()+"' and type = 'promotion' limit 1 ", params);

        return getPromotion(bucket.query(query));
    }

    private Promotion getPromotion(N1qlQueryResult result) {
        List<N1qlQueryRow> rows = result.allRows();

        if(rows.isEmpty()) {
            return  null;
        } else {
            //let's ignore if we have more then one match
            JsonObject json = rows.get(0).value().getObject("travel-sample");
            Promotion prom = new Promotion();
            prom.setOriginAirport(json.getString("originAirport"));
            JsonArray array = json.getArray("offers");

            List<Offer> list = new ArrayList<>();
            for(int i=0;i< array.size(); i++) {

                JsonObject obj = (JsonObject) array.get(i);
                Offer offer = new Offer();
                offer.setAirportDestination(obj.getString("airportDestination"));
                offer.setPrice(obj.getDouble("price"));
                list.add(offer);
            }

            prom.setOffers(list);
            return  prom;
        }
    }
}
