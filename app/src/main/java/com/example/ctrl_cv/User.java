package com.example.ctrl_cv;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String uid;
    private String name;
    private String email;
    private String phoneNumber;
    private List<String> applications;
    private List<String> myListings;

    public User(String uid, String name, String email, String phoneNumber, List<String> applications, List<String> myListings) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.applications = applications;
        this.myListings = myListings;
    }

    public User() {
        this.uid = "";
        this.name = "";
        this.email = "";
        this.phoneNumber = "";
        this.applications = new ArrayList<>();
        this.myListings = new ArrayList<>();
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<String> getApplications() {
        return applications;
    }

    public void setApplications(List<String> applications) {
        this.applications = applications;
    }

    public List<String> getMyListings() {
        return myListings;
    }

    public void setMyListings(List<String> myListings) {
        this.myListings = myListings;
    }
}
