package com.example.projet_mobile;

public class Candidat {
    private String userId;
    private String name;
    private String firstName;
    private String email;
    private String num;
    private String nationality;
    private String dateOfBirth;
    private String city;
    private String comment;
    private String etat;

    public Candidat() {
        // Firestore requires a no-argument constructor
    }

    public Candidat(String userId, String name, String firstName, String email, String num, String nationality, String dateOfBirth, String city, String comment, String etat) {
        this.userId = userId;
        this.name = name;
        this.firstName = firstName;
        this.email = email;
        this.num = num;
        this.nationality = nationality;
        this.dateOfBirth = dateOfBirth;
        this.city = city;
        this.comment = comment;
        this.etat = etat;
    }

    // Getters and setters for all fields
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }
}
