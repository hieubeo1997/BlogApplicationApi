package com.example.demo.services.impl;

import com.example.demo.entity.Follow;
import com.example.demo.entity.User;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.DuplicateRecordException;
import com.example.demo.exception.InternalServerException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.data.UserData;
import com.example.demo.model.mapper.UserMapper;
import com.example.demo.model.request.userReq.CreateUserReq;
import com.example.demo.model.request.userReq.FollowReq;
import com.example.demo.model.request.userReq.UpdateUserReq;
import com.example.demo.repository.FollowRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private FollowRepository followRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, FollowRepository followRepository) {
        this.userRepository = userRepository;
        this.followRepository = followRepository;
    }

    @Override
    public UserData createUser(CreateUserReq req) {
        List<User> checkusers = userRepository.findUsersByUsernameOrEmail(req.getUsername(), req.getEmail());
        if(!checkusers.isEmpty()) {
            throw new DuplicateRecordException("This username has been existed!");
        }else{
            User user = UserMapper.toUser(req);
            try {
                userRepository.save(user);
            }catch (Exception e){
                throw new InternalServerException("Error Database");
            }
            return UserMapper.toUserData(user);
        }
    }

    @Override
    public UserData updateUser(UpdateUserReq req, ObjectId id) {
        User userEdit = userRepository.findUserBy_id(id);
        if(userEdit == null){
            throw  new NotFoundException("Cannot find this user");
        }else if(userRepository.findUserByUsername(req.getUsername()) != null){
            throw new DuplicateRecordException("Your new username has been used");
        }
        User updateUser = UserMapper.updateToUser(req, userEdit.get_id(), userEdit.getEmail());
        try{
            userRepository.save(updateUser);
        }catch (Exception ex){
            throw new InternalServerException("Database can not update!");
        }
        return UserMapper.toUserData(updateUser);
    }

    @Override
    public List<UserData> searchUserByUsername(String username) {
        List<User> result = userRepository.findUserByUsernameLike(username);
        List<UserData> rs = new ArrayList<>();
        result.forEach( user -> {
            rs.add(UserMapper.toUserData(user));
        });
        return rs;
    }

    @Override
    public UserData getUserByUsername(String username) {
        return UserMapper.toUserData(userRepository.findUserByUsername(username));
    }

    @Override
    public UserData getUserByEmail(String email) {
        return UserMapper.toUserData(userRepository.findUserByEmail(email));
    }

    @Override
    public boolean isUserFollowTarget(String user, String target) {
        if(followRepository.findFollowByUsernameAndTargetname(user,target) == null){
            return false;
        }
        else
            return true;
    }

    public Follow findFollowRecord(String username, String targetname){
        if(userRepository.findUserByUsername(username) == null
                || userRepository.findUserByUsername(targetname)==null){
            throw new BadRequestException("Username or Targetname does not existed!");
        }
        else
            return new Follow(username,targetname);
    }
    @Override
    public Follow follow(FollowReq req) {
        Follow newRecord = findFollowRecord(req.getUsername(), req.getTargetname());
        try{
            followRepository.save(newRecord);
        }catch (Exception ex){
            throw new InternalServerException("Database can not update!");
        }
        return newRecord;
    }

    @Override
    public Follow unFollow(Follow fl) {
        try{
            followRepository.delete(fl);
        }catch (Exception ex){
            throw new InternalServerException("Database can not update: " + ex);
        }
        return fl;
    }
}
