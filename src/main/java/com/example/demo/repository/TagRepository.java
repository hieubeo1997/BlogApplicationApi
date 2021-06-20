package com.example.demo.repository;

import com.example.demo.entity.Tag;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TagRepository extends MongoRepository<Tag, String> {
    public List<Tag> findAll();

    public Tag findTagByName(String name);

    public List<Tag> findTagByNameLike(String name);

    public Tag findTagById(String id);

}
