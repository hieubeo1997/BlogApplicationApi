package com.example.demo.model.mapper;

import com.example.demo.entity.User;
import com.example.demo.model.data.ProfileData;
import com.example.demo.model.data.UserData;
import com.example.demo.model.request.userReq.CreateUserReq;
import com.example.demo.model.request.userReq.UpdateUserReq;
import org.bson.types.ObjectId;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class UserMapper {
    public static User toUser(CreateUserReq req){
        User user = new User();
        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());
        String hashPwd = BCrypt.hashpw(req.getPassword(), BCrypt.gensalt(12));
        user.setPassword(hashPwd);
        user.setRole("USER");
        return user;
    }
    public static UserData toUserData(User user){
        UserData tmp = new UserData();
        tmp.setId(user.get_id());
        tmp.setUsername(user.getUsername());
        tmp.setEmail(user.getEmail());
        tmp.setBio(user.getBio());
        tmp.setImage(user.getImage());
        tmp.setRole(user.getRole());
        return tmp;
    }
    public static User updateToUser(UpdateUserReq req, ObjectId id, String email){
        User user = new User();
        user.set_id(id);
        user.setUsername(req.getUsername());
        user.setEmail(email);
        user.setBio(req.getBio());
        user.setImage(req.getImage());
        String hashPwd = BCrypt.hashpw(req.getPassword(), BCrypt.gensalt(12));
        user.setPassword(hashPwd);
        user.setRole("USER");
        return user;
    }

    public static ProfileData toProfile(UserData data, boolean following, int follower){
        ProfileData newProfile = new ProfileData();
        newProfile.setUsername(data.getUsername());
        newProfile.setBio(data.getBio());
        newProfile.setImage(data.getImage());
        newProfile.setFollowing(following);
        newProfile.setFollower(follower);
        return newProfile;
    }
}
