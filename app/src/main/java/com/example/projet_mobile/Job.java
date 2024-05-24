package com.example.projet_mobile;

public class Job {
    private String id;
    private String companyName;
    private String location;
    private String date; // This should match the key used in addOffre
    private String description;
    private String remuneration; // This should match the key used in addOffre
    private String metierCible;
    private String createdBy;

    public Job() {
        // Constructor needed for Firebase
    }

    public Job(String id, String companyName, String location, String date, String description, String remuneration, String metierCible, String createdBy) {
        this.id = id;
        this.companyName = companyName;
        this.location = location;
        this.date = date;
        this.description = description;
        this.remuneration = remuneration;
        this.metierCible = metierCible;
        this.createdBy = createdBy;
    }

    // Getters and setters for each attribute
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRemuneration() {
        return remuneration;
    }

    public void setRemuneration(String remuneration) {
        this.remuneration = remuneration;
    }

    public String getMetierCible() {
        return metierCible;
    }

    public void setMetierCible(String metierCible) {
        this.metierCible = metierCible;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
