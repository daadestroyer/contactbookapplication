package com.boot.smartcontactapp.Entities;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotBlank(message = "Name should not be blank!")
    @Size(min = 2 , max = 20,message = "min 2 and max 20 character are allowed!")
    private String name;

    @Column(unique = true)
    @NotBlank(message="Email should not be blank")
    @Pattern(regexp = "^[a-zA-Z0-9+._.-]+@[a-zA-Z0-9.-]+$" , message = "Invalid Email!")
    private String email;

    @NotBlank(message = "Password should not be blank")
    @Size(min=6 , max = 1000 , message = "Password must be greater than 6 and less than 20 character!")
    private String password;

    private String role;
    private boolean enabled;
    private String imageURL;

    @NotBlank(message = "About should not be blank!")
    @Length(min = 20 , max = 1000 , message = "About must be greater than 6 and less than 1000 length!")
    private String about;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "user") // one user may have many contacts
    private List<Contact> contacts = new ArrayList<>();



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public User() {
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    @Override
    public String toString() {
        return "UserTable{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", enabled=" + enabled +
                ", imageURL='" + imageURL + '\'' +
                ", about='" + about + '\'' +
                ", contacts=" + contacts +
                '}';
    }

}
