/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import dtos.PersonDTO;
import java.util.List;
import javax.ws.rs.WebApplicationException;

/**
 *
 * @author Tweny
 */
public interface IPersonFacade {
    
    public PersonDTO addPerson(PersonDTO personDTO) throws WebApplicationException;

    public PersonDTO deletePerson(int id) throws WebApplicationException;

    public PersonDTO getPerson(int id) throws WebApplicationException;

    public List<PersonDTO> getAllPersons() throws WebApplicationException;

    public PersonDTO editPerson(PersonDTO personDTO) throws WebApplicationException;
}
