package com.example.ctrl_cv;

public class Application {
    private String userId;
    private String listingId;
    private String status;

    public Application(String userId, String listingId, String status) {
        this.listingId = listingId;
        this.userId = userId;
        this.setStatus(status);
    }

    public Application() {
        this.setStatus("pending");
        this.userId = "";
        this.listingId = "";
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getListingId() {
        return listingId;
    }

    public void setListingId(String listingId) {
        this.listingId = listingId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (status.equals("pending") || status.equals("accepted") || status.equals("rejected")) {
            this.status = status;
        } else {
            this.status = "pending";
        }
    }
}
