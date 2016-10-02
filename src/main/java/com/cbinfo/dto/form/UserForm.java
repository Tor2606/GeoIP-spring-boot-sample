package com.cbinfo.dto.form;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class UserForm {
    @Email(message = "Please, write your e-mail in valid form!")
    @NotEmpty(message = "You have to write your email!!")
    private String email;

    @Length(min=6, max=20, message = "Please, enter your password (6-20 symbols)!")
    private String password;

    private String firstName;

    private String lastName;



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
}
