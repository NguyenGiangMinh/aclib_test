package com.aclib.aclib_deploy.DTO;

public class RegistrationResponse {
    private String registrationId;
    private String notifications;

    public RegistrationResponse(String registrationId, String notifications) {
        this.registrationId = registrationId;
        this.notifications = notifications;
    }

    // Getters and Setters
    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public String getNotifications() {
        return notifications;
    }

    public void setNotifications(String notifications) {
        this.notifications = notifications;
    }
}
