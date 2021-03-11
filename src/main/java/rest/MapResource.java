/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import dtos.PersonDTO;
import facades.MapFacade;
import facades.PersonFacade;
import java.io.IOException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Tweny
 */
@Path("map")
public class MapResource {
    private static final MapFacade MAPFACADE = new MapFacade();
    
    @Path("{countrycode}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getPersonById(@PathParam("countrycode") String country) {
        return MAPFACADE.fetchData(country);
    }
}
