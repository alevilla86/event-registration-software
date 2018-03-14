package com.ers.core.dto;

/**
 *
 * @author avillalobos
 */
public class UserCreateDto {
    
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String type;

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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "UserCreateDto{" + "email=" + email + ", firstName=" + firstName + ", lastName=" + lastName + ", type=" + type + '}';
    }

}
