package com.example.demo.repository;

import com.example.demo.entity.Follow;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface FollowRepository extends MongoRepository<Follow, String> {
    public Follow findFollowByUsernameAndTargetname(String username, String targetname);

    public List<Follow> findFollowByTargetname(String targetname);

    public List<Follow> findFollowByUsername(String username);

    public int countFollowByTargetname(String target);

    public int countFollowByUsername(String username);
}
