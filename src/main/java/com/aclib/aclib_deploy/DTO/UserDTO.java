package com.aclib.aclib_deploy.DTO;

import com.aclib.aclib_deploy.Entity.User;

public class UserDTO {
    private long userId;
    private String username;
    private String email;
    private String phone;
    private String avatarUrl;
    private String bio;
    private User.UserRole role;

    //constructor
    public UserDTO() {}

    public UserDTO(long userId, String username, String email, String phone, User.UserRole role) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.role = User.UserRole.valueOf(role.name());
    }

    //getter and setter
    public long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public User.UserRole getRole() {
        return role;
    }

    public void setRole(User.UserRole role) {
        this.role = role;
    }
}
