package com.cbinfo.dto.form;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

public class UserForm {
    @Length(min=4, max=64, message = "Bitte gültigen Vornamen eingeben (4-64 Zeichen)")
    private String firstName;

    @Length(min=4, max=64, message = "Bitte gültigen Nachnamen eingeben (4-64 Zeichen)")
    private String lastName;

    private String email;

    @Length(min=6, max=64, message = "Bitte gültigen Passwort eingeben (6-64 Zeichen)")
    private String password;

    @NotEmpty(message = "Bitte Geschlecht eigeben")
    private String gender;

    @Length(min=4, max=64, message = "Bitte gültigen Strasse eingeben (4-64 Zeichen)")
    private String street;

    @Length(min=1, max=64, message = "Bitte gültigen Strasse Nummer eingeben (1-64 Zeichen)")
    private String houseNumber;

    @Pattern(regexp = "^\\d{6}", message = "Bitte gültigen PLZ eingeben (6 Zeichen)")
    private String zip;

    @Length(min=6, max=64, message = "Bitte gültigen Ort eingeben (6-64 Zeichen)")
    private String city;

    private String country;

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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
