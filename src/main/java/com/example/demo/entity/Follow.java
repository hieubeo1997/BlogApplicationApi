package com.example.demo.entity;

import lombok.AllArgsConstructor;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
@Document(collection = "follow")
public class Follow {

    @Id
    private ObjectId id;

    @Indexed
    @Field(value = "username")
    private String username;

    @Indexed
    @Field(value = "targetname")
    private String targetname;

    public Follow(String username, String targetname) {
        this.username = username;
        this.targetname = targetname;
    }

    public Follow(ObjectId id, String username, String targetname) {
        this.id = id;
        this.username = username;
        this.targetname = targetname;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public Follow(){}

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
