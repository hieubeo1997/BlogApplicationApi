package com.example.demo.controller;
import com.example.demo.entity.JsonWebToken;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.InternalServerException;
import com.example.demo.model.data.UserData;
import com.example.demo.model.data.UserWithToken;
import com.example.demo.model.request.userReq.AuthenticateReq;
import com.example.demo.model.request.userReq.CreateUserReq;
import com.example.demo.repository.redis.RedisTokenRepository;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.security.JwtTokenUtil;
import com.example.demo.services.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/authenticate")
public class AuthenController {
    private AuthenticationManager authenticationManager;
    private JwtTokenUtil jwtTokenUtil;
    private UserService userService;
    PasswordEncoder passwordEncoder;
    private RedisTokenRepository redisTokenRepository;
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Autowired
    public AuthenController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, UserService userService, PasswordEncoder passwordEncoder, RedisTokenRepository redisTokenRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.redisTokenRepository =  redisTokenRepository;
    }

    @PostMapping(value = "/register")
    public ResponseEntity<Object> createUser(@Valid @RequestBody CreateUserReq req) {
        UserData userData = userService.createUser(req);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(userData.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthenticateReq req){
        Authentication authentication = authenticationManager.authenticate(
           new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenUtil.generateToken(authentication);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return ResponseEntity.ok(new UserWithToken(
            token, userDetails.getId().toString(), userDetails.getUsername(), userDetails.getEmail(), userDetails.getAuthorities().toString()
        ));
    }
    @PostMapping(value = "/logout")
    @Cacheable(value = "JsonWebToken", key = "#p0")
    public String logout(HttpServletRequest request){
        String headerAuth = request.getHeader("Authorization");
        if(headerAuth.isEmpty() || headerAuth.isBlank() || !headerAuth.startsWith("Bearer ")){
            throw new BadRequestException("Bad Request!!!");
        }
        String token = headerAuth.substring(7, headerAuth.length());
        JsonWebToken atoken = new JsonWebToken(token);
        try {
            redisTokenRepository.save(atoken);
            return "You have log out successful!Please log in again to enjoy our services";
        }catch (Exception e){
            LOG.info("Error gi do : ");
            throw new InternalServerException("Error : " + e);
        }
    }
}
