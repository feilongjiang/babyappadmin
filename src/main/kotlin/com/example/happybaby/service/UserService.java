package com.example.happybaby.service;

import com.example.happybaby.dao.UserDao;
import com.example.happybaby.entity.User;
import com.example.happybaby.repo.UserRepo;
import com.example.happybaby.dao.RoleDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;


@Service
@Transactional(rollbackFor = Exception.class)
public class UserService extends BaseService<User, UserDao, UserRepo> {

    @Autowired
    private UserDao userDao;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RoleDao roleDao;

    private static final Logger logger = LogManager.getLogger(UserService.class);

    public Boolean checkPassword(String username, String password) {
        User user = this.findByName(username);

        return true;
    }

    /**
     * 根据用户名获取用户 - 用户的角色、权限等信息
     */
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. 查询用户
        User user = this.findByName(username);
        if (user == null) {
            //log.warn("User: {} not found", login);
            throw new UsernameNotFoundException("User " + username + " was not found in db");
            //这里找不到必须抛异常
        }
        // 2. 设置角色
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        String role = roleDao.findById(user.getRoleId()).getName();
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_" + role.toUpperCase());
        grantedAuthorities.add(grantedAuthority);

        return new org.springframework.security.core.userdetails.User(username,
                user.getPassword(), grantedAuthorities);
    }

    public User getUserDetail(String name) {
        User user = this.findByName(name);
        class UserDetail extends User {
            private String department;

            public void setDepartment(String department) {
                this.department = department;
            }

            public String getDepartment() {
                return department;
            }
        }
        UserDetail userDetail = new UserDetail();
        userDetail.setName(name);
        return userDetail;
    }

    public User getAuthUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return this.findByName(username);
    }

    @NotNull
    @Override
    public UserDao dao() {
        return this.userDao;
    }

    @NotNull
    @Override
    public UserRepo repo() {
        return this.repo();
    }
}
