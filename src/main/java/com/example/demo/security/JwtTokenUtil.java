package com.example.demo.security;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import java.util.Date;


@Component
public class JwtTokenUtil {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);

    // Token có hạn trong vòng 12 giờ kể từ thời điểm tạo
    @Value("${jwt.duration}")
    public Integer duration;

    // Lấy giá trị key được cấu hình trong file appliacation.properties
    @Value("${jwt.secret}")
    private String secret;

    //Hàm này sinh token cho mỗi request đúng, phải truyền userdetail vào vì chúng lưu giữ thông tin người đăng nhập
    public String generateToken(Authentication authentication) {
        // Lưu thông tin Authorities của user vào claims
        CustomUserDetails userPrincipal = (CustomUserDetails) authentication.getPrincipal();

        String token =  Jwts.builder()
        // Định nghĩa các claims: username, issue, expire
                            .setSubject(userPrincipal.getUsername())
                            .setIssuedAt(new Date(System.currentTimeMillis()))
                            //1 ngày
                            .setExpiration(new Date(System.currentTimeMillis() + duration * 1000))
                            .signWith(SignatureAlgorithm.HS512, secret)
                            .compact();
        return token;
    }
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }
    //Trích xuất thông tin trong token
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}
