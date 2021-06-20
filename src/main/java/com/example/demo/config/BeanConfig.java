package com.example.demo.config;

import com.example.demo.entity.JsonWebToken;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class BeanConfig {
    @Bean
    JedisConnectionFactory jedisConnectionFactory(){
        RedisStandaloneConfiguration redisStandaloneConfiguration =
                new RedisStandaloneConfiguration("localhost",6379);
        redisStandaloneConfiguration.setPassword(RedisPassword.of("daylamatkhau"));
        return new JedisConnectionFactory(redisStandaloneConfiguration);
    }
    @Bean
    public RedisTemplate<String , JsonWebToken> redisTemplate(){
        RedisTemplate template = new RedisTemplate<String, JsonWebToken>();
        jedisConnectionFactory().getPoolConfig().setMaxIdle(30);
        jedisConnectionFactory().getPoolConfig().setMinIdle(0);

        //để truyền được dữ liệu từ redis đi
        template.setDefaultSerializer(new StringRedisSerializer());
        template.setConnectionFactory(jedisConnectionFactory());
        template.setEnableTransactionSupport(true);
        template.setExposeConnection(true);
        return template;
    }

    @Bean
    public JedisPoolConfig poolConfig() {
        final JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setTestOnBorrow(true);
        jedisPoolConfig.setMaxTotal(100);
        jedisPoolConfig.setMaxIdle(30);
        jedisPoolConfig.setMinIdle(0);
        jedisPoolConfig.setTestOnReturn(true);
        jedisPoolConfig.setTestWhileIdle(true);
        return jedisPoolConfig;
    }
}
