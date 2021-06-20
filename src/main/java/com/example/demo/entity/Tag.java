package com.example.demo.entity;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@EqualsAndHashCode(of = "name")
@Document(collection = "tags")
public class Tag {
    @Id
    private String id;

    @NotBlank
    @NotNull
    @Field(value = "name")
    private String name;

    public Tag() {
    }

    public Tag(@NotBlank @NotNull String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
