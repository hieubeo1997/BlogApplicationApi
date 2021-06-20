package com.example.demo.model.request.userReq;
import lombok.NoArgsConstructor;


import javax.validation.constraints.NotNull;

@NoArgsConstructor
public class FollowReq {
    @NotNull
    private String username;
    @NotNull
    private String targetname;

    public FollowReq(@NotNull String username, @NotNull String targetname) {
        this.username = username;
        this.targetname = targetname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTargetname() {
        return targetname;
    }

    public void setTargetname(String targetname) {
        this.targetname = targetname;
    }
}
