/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Tweny
 */
@Entity
@Table(name = "address")
@NamedQueries({
    @NamedQuery(name = "Address.deleteAllRows", query = "DELETE from Address"),
    @NamedQuery(name = "Address.getAllRows", query = "SELECT a from Address a"),
    @NamedQuery(name = "Address.getAddress", query = "SELECT a from Address a WHERE a = :a")
})
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String street;
    private int zip;
    private String city;

    //***************One to Many****************
    //mappedBy fortæller vilken fremmednøgle/variablenavn den skal mappes til i Fee objektet (private Person person)
    //Det er Person der ejer relationen, da det er ONE der ejer en ONE-TO-MANY relation
    //Husk at lave en this.fees = new ArrayList<Fee>(); i konstruktøren
    @OneToMany(mappedBy = "address", cascade = {CascadeType.PERSIST, CascadeType.MERGE}) //Der findes også CascadeType.REMOVE hvis den må slette uden der sker noget
    private List<Person> persons;

    public void addPerson(Person person) {
        if (person != null) {
            person.setAddress(this);
            this.persons.add(person);
        }
    }
    
//    public void removePerson(Person person) {
//        if (person != null) {
//            this.persons.remove(person);
//        }
//    }

    public List<Person> getPersons() {
        return persons;
    }
    //*****************************************

    public Address() {
    }

    public Address(String street, int zip, String city) {
        this.street = street;
        this.zip = zip;
        this.city = city;
        this.persons = new ArrayList<Person>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Address other = (Address) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.zip != other.zip) {
            return false;
        }
        if (!Objects.equals(this.street, other.street)) {
            return false;
        }
        if (!Objects.equals(this.city, other.city)) {
            return false;
        }
        return true;
    }

    


    @Override
    public String toString() {
        return "Address{" + "id=" + id + ", street=" + street + ", zip=" + zip + ", city=" + city + ", persons=" + persons + '}';
    }

    
}
