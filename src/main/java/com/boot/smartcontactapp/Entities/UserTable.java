package com.boot.smartcontactapp.Entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "user")
public class UserTable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String Name;
    @Column(unique = true)
    private String email;
    private String password;
    private String role;
    private boolean enabled;
    private String imageURL;
    @Column(length = 500)
    private String about;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "userTable") // one user may have many contacts
    private List<ContactTable> contacts = new ArrayList<>();


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
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

    @Override
    public String toString() {
        return "UserTable{" +
                "id=" + id +
                ", Name='" + Name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", enabled=" + enabled +
                ", imageURL='" + imageURL + '\'' +
                ", about='" + about + '\'' +
                '}';
    }



    public UserTable() {
    }

    public List<ContactTable> getContacts() {
        return contacts;
    }

    public void setContacts(List<ContactTable> contacts) {
        this.contacts = contacts;
    }
}
