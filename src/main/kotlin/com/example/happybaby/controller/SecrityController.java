package com.example.happybaby.controller;

import com.example.happybaby.entity.User;
import com.example.happybaby.exception.BaseHttpStatus;
import com.example.happybaby.exception.MyException;
import com.example.happybaby.service.UserService;
import com.example.happybaby.utils.APIResult;
import com.example.happybaby.utils.Bcry;
import com.example.happybaby.utils.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


@RestController
public class SecrityController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    protected UserService userService;

    @Autowired
    protected TokenProvider tokenProvider;

    @RequestMapping(value = "web/token", method = RequestMethod.GET)
    public ResponseEntity<?> token(HttpServletRequest request, Authentication authentication, HttpServletRequest req) {
        String username = authentication.getName();
        User user = this.userService.findByName(username);
        // 5 生成自定义token
        TokenProvider tokenProvider = new TokenProvider();
        String ip = req.getRemoteAddr();
        String token = tokenProvider.getToken(user, ip);
        return ResponseEntity.ok(token);
    }

    @RequestMapping(value = "api/login", method = RequestMethod.POST)
    public APIResult token(@RequestBody Map<String, String> map, HttpServletRequest req, HttpServletResponse response) {
        String username = map.get("username");
        String password = map.get("password");
        User user = this.userService.findByName(username);
        // 5 生成自定义token
        Boolean isUser = Bcry.checkPassword(user.getPassword(), password);
        if (isUser) {
            String ip = req.getRemoteAddr();
            String token = tokenProvider.getToken(user, ip);
            Map<String, Object> result = new HashMap<>();
            user.setPassword("");
            user.setToken(token);
            response.setHeader("authorization", token);
            return APIResult.ok(user);
        } else {
            throw new MyException(BaseHttpStatus.USER_NOT_EXIST);
        }
    }

    @RequestMapping(value = "api/verifyLogin", method = RequestMethod.GET)
    public APIResult verifyToken(HttpServletRequest req, HttpServletResponse response) {
        User user = userService.getAuthUser();
        user.setPassword("");
        String ip = req.getRemoteAddr();
        String token = tokenProvider.getToken(user, ip);
        user.setPassword("");
        user.setToken(token);
        response.setHeader("authorization", token);
        return APIResult.ok(user);
    }
}
