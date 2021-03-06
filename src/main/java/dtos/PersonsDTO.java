/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

import entities.Person;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tweny
 */
public class PersonsDTO {
        
    private List<PersonDTO> all = new ArrayList<>();

    //Dette er en konstruktør
    public PersonsDTO(List<Person> personEntities) {
        personEntities.forEach((p) -> {
            all.add(new PersonDTO(p));
        });
    }
    
    public List<PersonDTO> getAll() {
        return all;
    }
}
