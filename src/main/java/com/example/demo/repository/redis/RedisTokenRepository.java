package com.example.demo.repository.redis;
import com.example.demo.entity.JsonWebToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Repository
public class RedisTokenRepository {
    private ValueOperations valueOperations;

    private RedisTemplate redisTemplate;

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    public RedisTokenRepository(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.valueOperations = this.redisTemplate.opsForValue();
    }
    public void save(JsonWebToken token){
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(token.getId(),token.getToken());
        redisTemplate.expire(token.getId(), 12,  TimeUnit.HOURS);
    }
    public void delete(String id)
    {
        Object tokenDeleted = redisTemplate.opsForValue().get(id);
        redisTemplate.delete(tokenDeleted);
    }

    public Map<String,Object> findAllKeys(String regex)
    {
        Set<String> listKeys =  redisTemplate.keys("*"+ regex +"*");
        Map<String,Object> respone = new LinkedHashMap<>();
        respone.put("keys", listKeys);
        return  respone;
    }
    public Map<String, Object> findAllValues()
    {
        Set<String> keys = redisTemplate.keys("ID_*");
        if (keys.isEmpty()){
            return null;
        }
        List<String> values = valueOperations.multiGet(keys);
        Map<String,Object> respone = new LinkedHashMap<>();
        respone.put("values", values);
        return respone;
    }
    public boolean isTokenInBlacklist(String token){
        Set<String> keys = redisTemplate.keys("ID_*");
        boolean result = false;
        List<String> values = valueOperations.multiGet(keys);
        for(String value: values){
            if(token.equals(value)){
                result = true;
                break;
            }
        }
        return result;
    }
}
