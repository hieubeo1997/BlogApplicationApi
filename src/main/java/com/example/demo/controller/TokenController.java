package com.example.demo.controller;
import com.example.demo.repository.redis.RedisTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;


@RestController
@RequestMapping("/token")
public class TokenController {
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    private RedisTokenRepository redisTokenRepository;
    @Autowired
    public TokenController(RedisTokenRepository redisTokenRepository) {
        this.redisTokenRepository = redisTokenRepository;
    }
//    @PostMapping(value = "/add")
//    @Cacheable(value = "JsonWebToken", key = "#p0")
//    public JsonWebToken addBlackListToken(HttpServletRequest request){
//        String headerAuth = request.getHeader("Authorization");
//        if(headerAuth.isEmpty() || headerAuth.isBlank() || !headerAuth.startsWith("Bearer ")){
//            throw new BadRequestException("Bad Request!!!");
//        }
//        String token = headerAuth.substring(7, headerAuth.length());
//        JsonWebToken atoken = new JsonWebToken(token);
//        try {
//            redisTokenRepository.save(atoken);
//            return atoken;
//        }catch (Exception e){
//            LOG.info("Error gi do : ");
//            throw new InternalServerException("Error : " + e);
//        }
//    }
    @GetMapping(value = "/getkey")
    public Map<String,Object> getKey(@RequestParam(required = false ,value = "regex") String regex){
        return redisTokenRepository.findAllKeys(regex);
    }
    @GetMapping(value = "/getvalues")
    public Map<String,Object> getToken(){
        return redisTokenRepository.findAllValues();
    }
}
