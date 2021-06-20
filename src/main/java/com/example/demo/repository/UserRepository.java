package com.example.demo.repository;

import com.example.demo.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    public User findUserByUsername(String username);

    public User findUserBy_id(ObjectId _id);

    public User findUserByEmail(String email);

    public List<User> findUsersByUsernameOrEmail(String username, String email);

    @Query(value = "{'username': {$regex : ?0, $options: 'i'}}")
    public List<User> findUserByUsernameLike(String username);

}
