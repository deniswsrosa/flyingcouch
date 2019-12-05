package com.couchbase.alexa.flyingcouch.dao;

import com.couchbase.alexa.flyingcouch.model.User;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.RawJsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.*;
import com.couchbase.client.java.query.dsl.Expression;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;


@Component
public class UserDAO {


    @Autowired
    private Bucket bucket;

    private ObjectMapper mapper = new ObjectMapper();


    public User getUserByAlexaId(String alexaId) {

        N1qlParams params = N1qlParams.build().adhoc(false);
        //we only care about the first match
        N1qlQuery query = N1qlQuery.simple("select meta(t).id as userId, t.* from `"+bucket.name()+"` t where t.alexaId = '"+alexaId+"' and t.type = 'user' limit 1 ", params);

        List<N1qlQueryRow> rows = bucket.query(query).allRows();

        if(rows.isEmpty()) {
            return  null;
        } else {

            JsonObject json = rows.get(0).value();
            User user = new User();
            user.setType("user");
            user.setId(json.getString("userId"));
            user.setAirport(json.getString("airport"));
            user.setAlexaId(json.getString("alexaId"));

            return  user;
        }
    }


    public User save(User user) throws Exception {
        if(user.getId() == null) {
            user.setId(UUID.randomUUID().toString());
        }

        user.setType("user");

        RawJsonDocument jsonDocument = RawJsonDocument.create(user.getId(), mapper.writeValueAsString(user));
        bucket.upsert(jsonDocument);
        return user;
    }

}
