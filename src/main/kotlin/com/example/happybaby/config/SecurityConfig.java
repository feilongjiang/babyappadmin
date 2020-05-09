package com.example.happybaby.config;


import com.example.happybaby.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserService userService;
    /**
     * token过滤器.
     */
    @Autowired
    LindTokenAuthenticationFilter lindTokenAuthenticationFilter;

    @Autowired
    HeaderFilter headerFilter;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        /*auth.inMemoryAuthentication()
                .withUser("user").password("password").roles("USER").and() //添加内存用户和角色
                .withUser("admin").password("password").roles("USER", "ADMIN");*/
        // 这里做用户登录判断用户名密码
        auth.userDetailsService(new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) {
                UserDetails user = userService.loadUserByUsername(username); // 从你的数据库中取出用户
                if (user == null) {
                    throw new UsernameNotFoundException("用户名不存在");
                }
                return user;
            }
        }).passwordEncoder(new BCryptPasswordEncoder()); // 使用 BCryptPasswordEncoder 加盐加密
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                // 基于token，所以不需要session
                // .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                //.and()
                .authorizeRequests()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/js/**").permitAll()
                .antMatchers("/font/**").permitAll()
                .antMatchers("/img/**").permitAll()
                .antMatchers("/js/**").permitAll()
                .antMatchers("/*.js", "/robots.txt").permitAll()
                .antMatchers("/*.html").permitAll()
                .antMatchers("/manifest.json").permitAll()
                .antMatchers("/api/token").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/postlogin").permitAll()
                .antMatchers("/api/category/**").permitAll()
                .antMatchers("/api/order/verify/**").hasRole("ADMIN")
                // .antMatchers("/api/**").permitAll()
                //.antMatchers("/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login")
                .loginProcessingUrl("/postlogin")
                .and()
                .logout()
                .permitAll()
                .and();
        http.addFilterBefore(lindTokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.headers().cacheControl();
    }
}


