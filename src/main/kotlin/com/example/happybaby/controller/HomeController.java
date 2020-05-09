package com.example.happybaby.controller;

import com.example.happybaby.entity.User;
import com.example.happybaby.service.UserService;
import com.example.happybaby.utils.APIResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;


@RestController
@RequestMapping("api")
public class HomeController {
    @Autowired
    UserService userService;

    @RequestMapping(value = "indexdata", method = RequestMethod.GET)
    @ResponseBody
    public APIResult indexdata(HttpServletRequest request, Authentication authentication) {
        String roleName = authentication.getAuthorities().toString();
        String username = authentication.getName();
        Map<String, Object> result = new LinkedHashMap<>();
        // 获取userinfo
        User user = userService.getUserDetail(username);
        if (user != null) result.put("userInfo", user);
        return APIResult.ok(result);
    }


}
