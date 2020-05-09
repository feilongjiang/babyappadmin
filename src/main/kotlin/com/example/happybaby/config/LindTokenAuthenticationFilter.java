package com.example.happybaby.config;

import com.example.happybaby.exception.BaseHttpStatus;
import com.example.happybaby.exception.MyException;
import com.example.happybaby.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Objects;

@Component
public class LindTokenAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    protected RedisTemplate<String, String> redisTemplate;
    String tokenHead = "token";
    String tokenHeader = "authorization";

    @Autowired
    protected UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authToken = request.getHeader(this.tokenHeader);
        String token = request.getHeader(this.tokenHeader);
        String token12 = request.getHeader("access-control-request-headers");
        Enumeration<String> header = request.getHeaderNames();
        String ww = request.getMethod();
        if (authToken != null && !authToken.equals("")) {
            //final String authToken = authHeader.substring(tokenHead.length()); // The part after "Bearer "
            String ip = request.getRemoteAddr();
            if (Objects.equals(redisTemplate.opsForValue().get(ip + ":token"), authToken)) {
                String username = redisTemplate.opsForValue().get(ip + ":username");
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = this.userService.loadUserByUsername(username);
                    //可以校验token和username是否有效，目前由于token对应username存在redis，都以默认都是有效的
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(
                            request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } else {
                throw new MyException(BaseHttpStatus.UNAUTHORIZED);
            }

        }

        filterChain.doFilter(request, response);
    }
}
