package com.example.demo.model.request.userReq;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserReq {
    @NotNull(message = "Your name is required")
    @NotBlank
    private String username;

    @NotNull
    @NotBlank
    @Size(min = 4, max = 20)
    private String password;

    @Size(max = 70, message = "update your bio")
    private String bio;

    @Valid
    @URL(regexp = "(http?:\\\\/\\\\/.*\\\\.(?:png|jpg))" , message = "must be an image")
    private String image;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
