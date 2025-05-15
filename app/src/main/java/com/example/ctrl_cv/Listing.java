package com.example.ctrl_cv;

public class Listing {
    private String id;
    private String title;
    private String companyName;
    private String location;
    private int minSalary;
    private int maxSalary;
    private String description;
    private String userId;

    public Listing(String id, String title, String companyName, String location, int minSalary, int maxSalary, String description, String userId) {
        this.id = id;
        this.title = title;
        this.companyName = companyName;
        this.location = location;
        this.minSalary = minSalary;
        this.maxSalary = maxSalary;
        this.description = description;
        this.userId = userId;
    }

    public Listing() {
        this.id = "";
        this.title = "";
        this.companyName = "";
        this.location = "";
        this.minSalary = 0;
        this.maxSalary = 0;
        this.description = "";
        this.userId = "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public int getMinSalary() {
        return minSalary;
    }

    public void setMinSalary(int minSalary) {
        this.minSalary = minSalary;
    }

    public int getMaxSalary() {
        return maxSalary;
    }

    public void setMaxSalary(int maxSalary) {
        this.maxSalary = maxSalary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
