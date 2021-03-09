/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.PersonDTO;
import dtos.PersonsDTO;
import entities.Person;
import facades.FacadeExample;
import facades.PersonFacade;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import utils.EMF_Creator;

/**
 *
 * @author Tweny
 */
@Path("person")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class PersonResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();

    private static final PersonFacade PERSONFACADE = PersonFacade.getPersonFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @GET
    public String demo() {
        return "{\"msg\":\"Hello World\"}";
    }

    @Path("all")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getAllPersonsDTO() {
        PersonsDTO personsDTO = PERSONFACADE.getAllPersonsAsDTO();
        return GSON.toJson(personsDTO);
    }

    @Path("id/{id}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getPersonById(@PathParam("id") int id) {
        PersonDTO personDTO = PERSONFACADE.getPerson(id);
        return GSON.toJson(personDTO);
    }
//
//    @POST
//    public Response addPerson(String person) {
//        //System.out.println(person);
//        PersonDTO personDTO = GSON.fromJson(person, PersonDTO.class); //manual conversion
//        personDTO = PERSONFACADE.addPerson(personDTO);
//        return Response.ok(personDTO).build();
//    }
//
//    @Path("/{id}")
//    @PUT
//    public Response editPerson(@PathParam("id") int id, String person) {
//        //System.out.println(person);
//        PersonDTO personDTO = GSON.fromJson(person, PersonDTO.class); //manual conversion
//        personDTO.setId(id);
//        personDTO = PERSONFACADE.editPerson(personDTO);
//        return Response.ok(personDTO).build();
//    }
//
//    @Path("/{id}")
//    @DELETE
//    public String deleteCar(@PathParam("id") int id) {
//        PersonDTO personDTO = PERSONFACADE.deletePerson(id);
//        System.out.println(personDTO);
//        return "{\"status\": \"removed\"}";
//    }
}
