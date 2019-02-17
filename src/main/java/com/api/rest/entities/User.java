package com.api.rest.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Past;
import java.util.Date;

@Document(collection = User.COLLECTION_NAME)
public class User {


    public static final String COLLECTION_NAME = "users";

    @Id
    private Integer id;
    private String name;

    @JsonFormat(pattern = "dd/MM/yyyy",shape = JsonFormat.Shape.STRING,timezone = "CET")
    @Past(message = "Only the past is valid")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date birthdate;


    public User() {

    }

    public User(Integer id, String name, Date birthdate) {
        this.id = id;
        this.name = name;
        this.birthdate = birthdate;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthdate=" + birthdate +
                '}';
    }
}
