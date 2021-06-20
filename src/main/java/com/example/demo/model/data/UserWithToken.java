package com.example.demo.model.data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserWithToken {
    private String token;
    private String type = "Bearer";
    private String id;
    private String username;
    private String email;
    private String roles;

    public UserWithToken(String token, String id, String username, String email, String roles) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }

}
