package com.example.happybaby.utils;


import com.example.happybaby.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;


import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class TokenProvider {
    @Autowired
    @Qualifier("commonProperties")
    protected Map<String, String> commonMap;

    @Autowired
    protected StringRedisTemplate redisTemplate;
    private Long validity;
    private final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

    public void setValidity(int expire) {
        this.validity = System.currentTimeMillis() + 1000L * expire;
    }

    public long getValidity() {
        if (this.validity == null) {
            Integer expire = Integer.valueOf(commonMap.get("token.expire"));
            setValidity(expire);
        }

        return this.validity;
    }

    //genterator token
    private String createToken(User user) {
        String token = computeSignature(user);
        return token;
    }

    // 验证token
    public boolean validateToken(String authToken, UserDetails userDetails) {
        return BCrypt.checkpw(userDetails.getUsername(), authToken);
    }

    public String computeSignature(User user) {
        String salt = BCrypt.gensalt(10);
        String hashed = BCrypt.hashpw(user.getName(), BCrypt.gensalt());
        return hashed;
    }

    private void saveToken(String token, User user, String key) {
        if (token == null) {
            return;
        }
        if (hasToken(key + ":token")) {
            String listKey = "oa:usertoken:" + user.getId(); //列表
            Long listSize = redisTemplate.opsForList().size(listKey);//列表尺寸
            if (listSize != null && listSize > 30) {
                String deleteKey = redisTemplate.opsForList().rightPop(listKey);// 每个用户最多保存30个token,超出从右边删除
                delete(user, deleteKey);
            }
            redisTemplate.opsForList().leftPush(listKey, key);//从左边保存
        } else {
        }
        redisTemplate.opsForValue().set(key + ":token", token, getValidity(), TimeUnit.MILLISECONDS);//保存token
        redisTemplate.opsForValue().set(key + ":username", user.getName(), getValidity(), TimeUnit.MILLISECONDS);//保存token
        redisTemplate.opsForValue().set(key, token, getValidity(), TimeUnit.MILLISECONDS);//保存token
    }

    public String getToken(User user, String key) {
        String token = null;
        if (hasToken(key + ":token")) {
            token = redisTemplate.opsForValue().get(key);
        } else {
            token = createToken(user);
        }
        saveToken(token, user, key);
        return token;
    }

    public Boolean hasToken(String key) {
        Boolean has = redisTemplate.hasKey(key);
        return has != null && has;
    }

    public void delete(User user, String key) {
        String listKey = "oa:usertoken:" + user.getId(); //列表
        redisTemplate.delete(key + ":token");
        redisTemplate.delete(key + "username");
        redisTemplate.opsForList().remove(listKey, 0, key);
    }

}
