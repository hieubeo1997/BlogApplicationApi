package com.example.demo.entity;

import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.UUID;

public class JsonWebToken implements Serializable {

    @Id
    private String id;
    private String token;


    public JsonWebToken(String token) {
        this.id = "ID_"+ UUID.randomUUID().toString().replace("-","");
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
