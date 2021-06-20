package com.example.demo.controller;


import com.example.demo.entity.Follow;
import com.example.demo.exception.DuplicateRecordException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.data.ProfileData;
import com.example.demo.model.data.UserData;
import com.example.demo.model.mapper.UserMapper;
import com.example.demo.model.request.userReq.FollowReq;
import com.example.demo.model.request.userReq.UpdateUserReq;
import com.example.demo.repository.FollowRepository;
import com.example.demo.security.JwtTokenUtil;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UsersController {
    private AuthenticationManager authenticationManager;
    private JwtTokenUtil jwtTokenUtil;
    private UserService userService;
    private FollowRepository followRepository;

    @Autowired
    public UsersController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, UserService userService, FollowRepository followRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
        this.followRepository = followRepository;
    }

    public UsersController() {
    }

    public UserData getCurrentUser(Principal principal) {
        UserData userData = userService.getUserByUsername(principal.getName());
        return userData;
    }

    //OK
    @GetMapping(value = "/myprofile")
    public ResponseEntity<?> currentUser(Principal principal) {
        UserData userData = getCurrentUser(principal);
        return ResponseEntity.ok(userData);
    }

    //Van chua co anh - OK
    @PutMapping(value = "/update")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UpdateUserReq req, Principal principal) {
        UserData currentUser = userService.getUserByUsername(principal.getName());
        UserData result = userService.updateUser(req, currentUser.getId());
        return ResponseEntity.ok(result);
    }

    //search list username - da ok
    @GetMapping(value = "/profile/{targetname}")
    public ResponseEntity<?> findAnotherUser(@PathVariable String targetname, Principal principal) {
        UserData currentUser = getCurrentUser(principal);
        List<UserData> listResult = userService.searchUserByUsername(targetname);
        List<ProfileData> listProfile = new ArrayList<>();
        listResult.forEach(userData -> {
            listProfile.add(UserMapper
                    .toProfile(userData,
                            userService.isUserFollowTarget(currentUser.getUsername(), userData.getUsername()),
                            followRepository.countFollowByTargetname(userData.getUsername())));

        });
        return ResponseEntity.ok(listProfile);
    }

    //Follow thanh` cong
    @PostMapping(value = "/follow/{targetname}")
    public ResponseEntity<?> followOther(@PathVariable String targetname, Principal principal) {
        UserData currentUser = getCurrentUser(principal);
        if (followRepository.findFollowByUsernameAndTargetname(currentUser.getUsername(), targetname) != null) {
            throw new DuplicateRecordException("Bad Request, Follow has been existed!!");
        }
        FollowReq newFollowReq = new FollowReq(currentUser.getUsername(), targetname);
        userService.follow(newFollowReq);
        return ResponseEntity.ok(newFollowReq);
    }

    //Unfollow thanh` cong
    @DeleteMapping(value = "/unfollow/{targetname}")
    public ResponseEntity<?> unfollowOther(@PathVariable String targetname, Principal principal) {
        UserData currentUser = getCurrentUser(principal);
        if (followRepository.findFollowByUsernameAndTargetname(currentUser.getUsername(), targetname) == null) {
            throw new NotFoundException("Follow record has not been existed");
        }
        Follow deleteFollow = followRepository.findFollowByUsernameAndTargetname(currentUser.getUsername(), targetname);
        userService.unFollow(deleteFollow);
        return ResponseEntity.ok(deleteFollow);
    }

    @GetMapping(value = "/follow-list/{target}")
    public ResponseEntity<?> listFollowTarget(@PathVariable(value = "target") String target) {
//        UserData userhientai = getCurrentUser(principal);
        List<Follow> listRs = followRepository.findFollowByTargetname(target);
        return ResponseEntity.ok(listRs);
    }
}
