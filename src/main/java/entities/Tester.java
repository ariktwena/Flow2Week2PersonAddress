/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import dtos.PersonDTO;
import dtos.PersonsDTO;
import facades.PersonFacade;
import static facades.PersonFacade.getPersonFacade;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import utils.EMF_Creator;

/**
 *
 * @author Tweny
 */
public class Tester {
    public static void main(String[] args) {
        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        PersonFacade personFacade = getPersonFacade(emf);
        
        Person person1 = new Person("John");
        Person person2 = new Person("Sam");
        Person person3 = new Person("Billy");
        Person person4 = new Person("Delete Me");
        
        Address address1 = new Address("vej1", 1111, "by1"); 
        Address address2 = new Address("vej2", 2222, "by2");
        Address address3 = new Address("vej1", 1111, "by1");
        
        //Create
        person1.setAddress(address1);
        PersonDTO personDTO1 = new PersonDTO(person1);
        personDTO1 = personFacade.addPerson(personDTO1);
        
        person2.setAddress(address2);
        PersonDTO personDTO2 = new PersonDTO(person2);
        personDTO2 = personFacade.addPerson(personDTO2);
        
        person3.setAddress(address1);
        PersonDTO personDTO3 = new PersonDTO(person3);
        personDTO3 = personFacade.addPerson(personDTO3);
        
        person4.setAddress(address1);
        PersonDTO personDTO4 = new PersonDTO(person4);
        personDTO4 = personFacade.addPerson(personDTO4);
        
        //Delete
        System.out.println(personDTO4.toString());
        personFacade.deletePerson(personDTO4.getId());
        
        //Edit
        //personFacade.deletePerson(personDTO2.getId());
        personDTO3.setName("BillyNew");
        personDTO3 = personFacade.editPerson(personDTO3);
        System.out.println(personDTO3.toString());
        
        
        personDTO3.setStreet("Vej1New");
        personDTO3 = personFacade.editPerson(personDTO3);
        System.out.println(personDTO3.toString());
        
        //Read
        System.out.println(personDTO1.toString());
        personDTO1 = personFacade.getPerson(personDTO1.getId());
        System.out.println(personDTO1.toString());
        
        
//Test edit
//        Person person5 = new Person("Delete Me3");
//        Address address4 = new Address("Testvej", 7778, "bySlet"); 
//        person5.setAddress(address4);
//        PersonDTO personDTO5 = new PersonDTO(person5);
//        personDTO5 = personFacade.addPerson(personDTO5);
//        System.out.println(personDTO5);
//        
//        Address address2 = new Address("vej2", 2222, "by2");
//        personDTO5.setStreet(address2.getStreet());
//        personDTO5.setZip(address2.getZip());
//        personDTO5.setCity(address2.getCity());
//        personDTO5.setName("Det nye navn");
//        personDTO5 = personFacade.editPerson(personDTO5);
//        System.out.println(personDTO5);

        PersonsDTO p = personFacade.getAllPersonsAsDTO();
        List<PersonDTO> list = p.getAll();
        
        for(PersonDTO dto : list){
            System.out.println(dto.getName());
        }
        
        
        List<PersonDTO> list1 = personFacade.getAllPersons();
        
        for(PersonDTO dto : list1){
            System.out.println(dto.getName());
        }
    }
}
