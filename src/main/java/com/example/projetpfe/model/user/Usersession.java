package com.example.projetpfe.model.user;

public class Usersession {
    private String firstname;
    private Integer id;
    public Usersession(String firstname, Integer id) {
        this.firstname = firstname;
        this.id=id;
    }

    // Getter and Setter
    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public Integer getId()
    {
        return id;
    }
    public void setId(Integer id)
    {
        this.id=id;
    }
}
