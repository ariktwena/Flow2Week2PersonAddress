package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.PersonDTO;
import dtos.PersonsDTO;
import facades.PersonFacade;
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
public class PersonResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();

    private static final PersonFacade PERSONFACADE = PersonFacade.getPersonFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @GET
    @Produces({MediaType.APPLICATION_JSON})
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

    @Path("{id}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getPersonById(@PathParam("id") int id) {
        PersonDTO personDTO = PERSONFACADE.getPerson(id);
        return GSON.toJson(personDTO);
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response addPerson(String person) {
        PersonDTO personDTO = GSON.fromJson(person, PersonDTO.class); //manual conversion
        personDTO = PERSONFACADE.addPerson(personDTO);
        return Response.ok(personDTO).build();
    }

    @Path("{id}")
    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response editPerson(@PathParam("id") int id, String person) {
        PersonDTO personDTO = GSON.fromJson(person, PersonDTO.class); //manual conversion
        personDTO.setId(id);
        personDTO = PERSONFACADE.editPerson(personDTO);
        return Response.ok(personDTO).build();
    }

    @Path("{id}")
    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    public String deleteCar(@PathParam("id") int id) {
        PersonDTO personDTO = PERSONFACADE.deletePerson(id);
        System.out.println(personDTO);
        return "{\"status\": \"removed\"}";
    }
}
