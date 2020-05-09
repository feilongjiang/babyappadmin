package com.example.happybaby.webController;

import com.example.happybaby.service.UserService;
import com.example.happybaby.utils.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("/")
public class WebHomeController {
    @Autowired
    UserService userService;

    @RequestMapping("/")
    @ResponseBody
    public String home(HttpServletRequest request, Authentication authentication) {
        HttpSession session = request.getSession();
        return "index.html";
    }

    @RequestMapping("detail")
    public String detail(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return "detail";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(HttpServletRequest request) {
        return "login.html";
    }

    @RequestMapping(value = "/postlogin", method = RequestMethod.POST)
    //@RequestParam String username, @RequestParam String password
    public String postLogin() {
        TokenProvider tokenProvider = new TokenProvider();
        return "redirect:token";
    }


}
