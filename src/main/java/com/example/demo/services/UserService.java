package com.example.demo.services;

import com.example.demo.entity.Follow;
import com.example.demo.model.data.UserData;
import com.example.demo.model.request.userReq.CreateUserReq;
import com.example.demo.model.request.userReq.FollowReq;
import com.example.demo.model.request.userReq.UpdateUserReq;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public interface UserService {

    public UserData createUser(CreateUserReq req);
    public UserData updateUser(UpdateUserReq req, ObjectId id);
    public List<UserData> searchUserByUsername(String username);
    public UserData getUserByUsername(String username);
    public UserData getUserByEmail(String email);
    public boolean isUserFollowTarget(String user, String target);
    public Follow follow(FollowReq req);
    public Follow unFollow(Follow fl);
}
