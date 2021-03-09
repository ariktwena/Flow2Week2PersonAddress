/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import dtos.PersonDTO;
import dtos.PersonsDTO;
import dtos.RenameMeDTO;
import entities.Address;
import entities.Person;
import entities.RenameMe;
import interfaces.IPersonFacade;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.ws.rs.WebApplicationException;
import utils.EMF_Creator;

/**
 *
 * @author Tweny
 */
public class PersonFacade implements IPersonFacade {

    private static PersonFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private PersonFacade() {
    }

    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static PersonFacade getPersonFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PersonFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    @Override
    public PersonDTO addPerson(PersonDTO personDTO) throws WebApplicationException {
        EntityManager em = emf.createEntityManager();
        if (personDTO.getName() == null) {
            throw new WebApplicationException("Name is missing", 400);
        } else if (personDTO.getStreet() == null || personDTO.getZip() == 0 || personDTO.getCity() == null) {
            throw new WebApplicationException("Address has to have a street, zip and city.", 400);
        }
        try {
            Person person = new Person(personDTO.getName());
            Address address = new Address(personDTO.getStreet(), personDTO.getZip(), personDTO.getCity());

            //Check if the address is in the database
            if (doesAddressExist(address) == null) {
                //Add the perons to the adress
                address.addPerson(person);

                //Persist the address
                em.getTransaction().begin();
                em.persist(address);
                em.getTransaction().commit();
                return new PersonDTO(person);
            } else {
                //Vi får et address objekt med id
                address = doesAddressExist(address);

                //Vi henter adressen via id så den bliver managed
                address = em.find(Address.class, address.getId());

                //Vi indesætter data i vores DB (Begge linjer kan bruges)
                address.addPerson(person);
                //person.setAddress(address); //(kan også bruges)

                //Persist the address
                em.getTransaction().begin();
                em.persist(address);
                //em.merge(address); //Kan også bruges
                //em.persist(person); //Kan også bruges
                em.getTransaction().commit();
                return new PersonDTO(person);
            }
        } catch (RuntimeException ex) {
            throw new WebApplicationException("Internal Server Problem. We are sorry for the inconvenience", 500);
        } finally {
            em.close();
        }
    }

    @Override
    public PersonDTO deletePerson(int id) throws WebApplicationException {
        int rowCount;
        EntityManager em = emf.createEntityManager();
        try {
            Person person = em.find(Person.class, id);
            System.out.println(person);
            System.out.println(person.getAddress().getId());

//            Query query = em.createQuery("SELECT a FROM Address a JOIN a.persons p WHERE p.id = :id");
//            query.setParameter("id", id);
//            Address addressToDelete = (Address) query.getSingleResult();
//            System.out.println(addressToDelete);
            Address addressToDelete1 = em.find(Address.class, person.getAddress().getId());
            System.out.println(addressToDelete1);

            PersonDTO personDTO = new PersonDTO(person);

            if (findNumberOfAddresses(addressToDelete1) > 1) {
                em.getTransaction().begin();
                em.remove(person);
                em.getTransaction().commit();
                return personDTO;
            } else {
                em.getTransaction().begin();
                em.remove(person);
                em.remove(addressToDelete1);
                em.getTransaction().commit();
                return personDTO;
            }

        } catch (NullPointerException ex) {
            throw new WebApplicationException("Could not delete, provided id does not exist", 404);
        } catch (RuntimeException ex) {
            throw new WebApplicationException("Internal Server Problem. We are sorry for the inconvenience", 500);
        } finally {
            em.close();
        }
    }

    @Override
    public PersonDTO getPerson(int id) throws WebApplicationException {
        EntityManager em = emf.createEntityManager();
        try {
            PersonDTO personDTO = new PersonDTO(em.find(Person.class, id));
            return personDTO;
        } catch (NullPointerException ex) {
            throw new WebApplicationException(String.format("No person with provided id: (%d) found", id), 404);
        } catch (RuntimeException ex) {
            throw new WebApplicationException("Internal Server Problem. We are sorry for the inconvenience", 500);
        } finally {
            em.close();
        }
    }

    @Override
    public List<PersonDTO> getAllPersons() throws WebApplicationException {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Person> query = em.createQuery("SELECT person FROM Person person", Person.class);
            List<Person> persons = query.getResultList();
            System.out.println(persons.size());
            return PersonDTO.convertPersonsTopersonDTOs(persons);
        } catch (RuntimeException ex) {
            throw new WebApplicationException("Internal Server Problem. We are sorry for the inconvenience", 500);
        } finally {
            em.close();
        }
    }

    public PersonsDTO getAllPersonsAsDTO() throws WebApplicationException {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Person> query = em.createQuery("SELECT person FROM Person person", Person.class);
            List<Person> persons = query.getResultList();
            System.out.println(persons.size());
            return new PersonsDTO(persons);
        } catch (RuntimeException ex) {
            throw new WebApplicationException("Internal Server Problem. We are sorry for the inconvenience", 500);
        } finally {
            em.close();
        }
    }

    @Override
    public PersonDTO editPerson(PersonDTO personDTO) throws WebApplicationException {
        //int updateCount;
        if (personDTO.getName() == null) {
            throw new WebApplicationException("Name is missing", 400);
        } else if (personDTO.getStreet() == null || personDTO.getZip() == 0 || personDTO.getCity() == null) {
            throw new WebApplicationException("Address has to have a street, zip and city.", 400);
        }
        EntityManager em = emf.createEntityManager();

        //What to edit
        Person personOriginal = em.find(Person.class, personDTO.getId());
        Address addressOriginal = em.find(Address.class, personOriginal.getAddress().getId());

        //Ændre persondata/navn til det nye
        personOriginal.setName(personDTO.getName());

        //Sæt "ny" adresse
        Address newAddress = new Address(personDTO.getStreet(), personDTO.getZip(), personDTO.getCity());

        //Test om adresse findes
        Address addressToTest = doesAddressExist(newAddress);
        if (addressToTest == null) {
            //Link person to new address
            newAddress.addPerson(personOriginal);

            //If old adress has 1 person attached, thene delete
            int numberOfPeopleLivingThere = findNumberOfAddresses(addressOriginal);

            try {
                em.getTransaction().begin();
                em.persist(newAddress);
                em.merge(personOriginal);

                //Remove old adress if no one i living there
                if (numberOfPeopleLivingThere < 2) {
                    em.remove(addressOriginal);
                }

                em.getTransaction().commit();
                return new PersonDTO(personOriginal);
            } catch (RuntimeException ex) {
                throw new WebApplicationException("Internal Server Problem. We are sorry for the inconvenience", 500);
            } finally {
                em.close();
            }

        } else if (addressToTest != null && !addressOriginal.equals(addressToTest)) {
            //Link person to existing address
            addressToTest = em.find(Address.class, addressToTest.getId());
            addressToTest.addPerson(personOriginal);

            //If old adress has 1 person attached, thene delete
            int numberOfPeopleLivingThere = findNumberOfAddresses(addressOriginal);

            try {
                em.getTransaction().begin();
                em.merge(personOriginal);
                em.merge(addressToTest);

                //Remove old adress if no one i living there
                if (numberOfPeopleLivingThere < 2) {
                    em.remove(addressOriginal);
                }

                em.getTransaction().commit();
                return new PersonDTO(personOriginal);
            } catch (RuntimeException ex) {
                throw new WebApplicationException("Internal Server Problem. We are sorry for the inconvenience", 500);
            } finally {
                em.close();
            }
        } else {
            //Only updatename
            try {
                em.getTransaction().begin();
                em.merge(personOriginal);
                em.getTransaction().commit();
                return new PersonDTO(personOriginal);
            } catch (RuntimeException ex) {
                throw new WebApplicationException("Internal Server Problem. We are sorry for the inconvenience", 500);
            } finally {
                em.close();
            }
        }
    }

    private Address doesAddressExist(Address address) {
        EntityManager em = emf.createEntityManager();
        try {
            Query query = em.createQuery("SELECT a FROM Address a WHERE a.street = :street AND a.zip = :zip AND a.city = :city ", Address.class);
            query.setParameter("street", address.getStreet());
            query.setParameter("zip", address.getZip());
            query.setParameter("city", address.getCity());
            Address addreessExists = (Address) query.getSingleResult();
            //System.out.println("address exists: " + addreessExists);
            return addreessExists;
        } catch (NoResultException ex) {
            //System.out.println("Her");
            return address = null;
        } finally {
            em.close();
        }
    }

    private int findNumberOfAddresses(Address address) {
        EntityManager em = emf.createEntityManager();
        try {
            //Vi søger efter hvor mange der bor på adressen
            Query query = em.createQuery("SELECT p FROM Address a JOIN a.persons p WHERE a.street = :street AND a.zip = :zip AND a.city = :city ");
            query.setParameter("street", address.getStreet());
            query.setParameter("zip", address.getZip());
            query.setParameter("city", address.getCity());
            List<Person> personList = query.getResultList();
            System.out.println("Number of adresses: " + personList.size());
            return personList.size();
        } catch (NoResultException ex) {
            //System.out.println("Her");
            return 0;
        } finally {
            em.close();
        }
    }

    public static void main(String[] args) {
        emf = EMF_Creator.createEntityManagerFactory();
        PersonFacade fe = getPersonFacade(emf);
//        fe.getAll().forEach(dto->System.out.println(dto));
    }
}
