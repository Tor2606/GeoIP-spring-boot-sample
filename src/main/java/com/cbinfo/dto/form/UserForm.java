package com.cbinfo.dto.form;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

public class UserForm {

    private String firstName;

    private String lastName;

    @Email(message = "Please, write your e-mail in valid form")
    private String email;

    @Length(min=6, max=20, message = "Please, enter your password (6-20 symbols!)")
    private String password;
}
