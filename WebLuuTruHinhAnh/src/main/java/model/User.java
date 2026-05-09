package model;

import java.util.Date;

public class User {
    private int id;
    private String email, password, fullname, role;
    private Date created_at;

    public User() {
    }

    public User(int id, String email, String password, String fullname, String role, Date created_at) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.fullname = fullname;
        this.role = role;
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }
}
