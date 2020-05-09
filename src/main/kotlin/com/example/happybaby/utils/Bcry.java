package com.example.happybaby.utils;

import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class Bcry {
/*

    public static void main(String[] args) {
        String password = "1";
        String salt = BCrypt.gensalt(10);
        String hashed = BCrypt.hashpw(password, salt);
        String ps = MD5Encoder.encode((hashed + salt).getBytes());
        System.out.println(ps);
// previously been hashed
        String candidate = "1";
//String candidate = "wrongtestpassword";
        if (BCrypt.checkpw(candidate, hashed))
            System.out.println(hashed + " size = " + hashed.length());
        else
            System.out.println("It does not match");
    }
*/

    public static Boolean checkPassword(String password, String candidate) {
        return BCrypt.checkpw(candidate, password);
    }

    public static String genPassword(String password, int pLength) {
        String salt = BCrypt.gensalt(pLength);
        String hashed = BCrypt.hashpw(password, salt);
        return hashed;
    }

    public static String genPassword(String password) {
        return genPassword(password, 10);
    }
}
