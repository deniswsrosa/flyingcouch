package com.couchbase.alexa.flyingcouch.rest;

import com.couchbase.alexa.flyingcouch.dao.AirportDAO;
import com.couchbase.alexa.flyingcouch.dao.PromotionDAO;
import com.couchbase.alexa.flyingcouch.dao.UserDAO;
import com.couchbase.alexa.flyingcouch.model.Airport;
import com.couchbase.alexa.flyingcouch.model.Promotion;
import com.couchbase.alexa.flyingcouch.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user/")
public class UserRest {


    @Autowired
    private UserDAO userDAO;

    @Autowired
    private AirportDAO airportDAO;

    @Autowired
    private PromotionDAO promotionDAO;

    //example: http://localhost:8080/api/user/findUserByAlexaId?alexaId=1234
    @RequestMapping("/findUserByAlexaId")
    public Response findUserByAlexaId(@RequestParam("alexaId") String alexaId) throws Exception {

        User user = userDAO.getUserByAlexaId(alexaId);
        Response response = new Response();
        if(user == null) {
            response.setResponseType("ERROR");
        } else {
            response.setResponseType("SUCCESS");
            response.setValue(user);
        }

        return response;
    }


    @RequestMapping("/getMyAirport")
    public Response getMyAirport(@RequestParam("alexaId") String alexaId) throws Exception {

        User user = userDAO.getUserByAlexaId(alexaId);
        Response response = new Response();
        if(user == null|| user.getAirport() == null || user.getAirport().isEmpty()) {
            response.setResponseType("ERROR");
            response.setMessage("You haven't defined your default airport yet");
        } else {

            Airport airport = airportDAO.getAirport(user.getAirport());
            response.setResponseType("SUCCESS");
            response.setMessage("Your default airport is "+airport.getAirportname()+", "+airport.getFaa().toUpperCase());
            response.setValue(user);
        }

        return response;
    }

    //example: http://localhost:8080/api/user/saveUserAirport?alexaId=1234&airport=lhr
    @RequestMapping("/saveUserAirport")
    public Response saveUserAirport(@RequestParam("alexaId") String alexaId, @RequestParam("airport") String airport) throws Exception {

        System.out.println("============== airport="+airport);
        User user = userDAO.getUserByAlexaId(alexaId);

        if(user == null) {
            user = new User();
            user.setAlexaId(alexaId);
        }

        Airport air = airportDAO.findAirport(airport);

        Response response = new Response();
        if(air == null) {
            response.setResponseType("ERROR");
            response.setMessage("Sorry, I could not find this airport.");
        } else {

            user.setAirport(air.getFaa());
            userDAO.save(user);
            response.setResponseType("SUCCESS");
            response.setMessage("Your airport was set to "+air.getAirportname()+", "+air.getFaa().toUpperCase());
            response.setValue(user);
        }

        return response;
    }


    @RequestMapping("/getFlightsOnSales")
    public Response getFlightsOnSales(@RequestParam("alexaId") String alexaId) throws Exception {

        User user = userDAO.getUserByAlexaId(alexaId);
        Response response = new Response();

        if(user == null || user.getAirport() == null || user.getAirport().isEmpty()) {
            response.setResponseType("ERROR");
            response.setMessage("You haven't defined an airport yet, please set your default airport before using this feature");
            return response;
        }

        Airport air = airportDAO.findAirport(user.getAirport());
        Promotion promotion = promotionDAO.getPromotion(user.getAirport());

        if(promotion == null || promotion.getOffers().isEmpty()) {
            response.setResponseType("ERROR");
            response.setMessage("Humm... I could not find tickets on sale from "+air.getAirportname());
        } else {
            String message = promotion.getOffers().stream()
                    .map(e-> "from "+air.getAirportname()+" to " + e.getAirportDestination()+" starting at "+e.getPrice().intValue()+" euros")
                    .collect(Collectors.joining(","));

            user.setAirport(air.getFaa());
            userDAO.save(user);
            response.setResponseType("SUCCESS");
            response.setMessage("Here are the tickets on sale that I found: "+message);
            response.setValue(user);
        }

        return response;
    }
}
