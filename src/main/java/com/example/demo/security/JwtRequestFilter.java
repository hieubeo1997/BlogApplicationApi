package com.example.demo.security;
import com.example.demo.exception.InternalServerException;
import com.example.demo.repository.redis.RedisTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Qualifier("jwtUserDetailsService")
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private RedisTokenRepository redisTokenRepository;
//    @Autowired
//    private TokenBlacklistRepo tokenBlacklistRepo;

    private static String HEADER = "Authorization";

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = parseJwt(httpServletRequest);
            // Parse thông tin từ token
            //validate token

            if (jwt != null && (jwtTokenUtil.validateJwtToken(jwt))) {
                String username = jwtTokenUtil.getUserNameFromJwtToken(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                        userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            throw new InternalServerException("Cannot set user authentication: {}" + e);
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader(HEADER);
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            String token = headerAuth.substring(7, headerAuth.length());
            return token;
        }
         return null;
        }
}
