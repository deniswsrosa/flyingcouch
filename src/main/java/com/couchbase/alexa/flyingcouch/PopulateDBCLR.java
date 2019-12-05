package com.couchbase.alexa.flyingcouch;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.query.N1qlParams;
import com.couchbase.client.java.query.N1qlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class PopulateDBCLR implements CommandLineRunner {

    @Autowired
    private Bucket bucket;

    @Override
    public void run(String... strings) throws Exception {


        String promotion = "UPSERT INTO `travel-sample` ( KEY, VALUE ) \n" +
                "  VALUES \n" +
                "  ( \n" +
                "    \"promotion1\", \n" +
                "    {\n" +
                "\"type\": \"promotion\",\n" +
                "\"originAirport\": \"LHR\",\n" +
                "\"offers\": [\n" +
                "    {\n" +
                "    \"airportDestination\": \"Munich\",\n" +
                "    \"price\": 75\n" +
                "    },\n" +
                "    {\n" +
                "    \"airportDestination\": \"Paris\",\n" +
                "    \"price\": 120\n" +
                "    }\n" +
                "]\n" +
                "}\n" +
                "  )";

        N1qlQuery query = N1qlQuery.simple(promotion);
        bucket.query(query);
    }
}
