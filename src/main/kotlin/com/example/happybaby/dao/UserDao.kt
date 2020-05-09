package com.example.happybaby.dao

import org.springframework.stereotype.Repository
import org.hibernate.SessionFactory
import com.example.happybaby.entity.User;

@Repository("UserDao")
open class UserDao(sessionFactory: SessionFactory) :BaseDao<User>(sessionFactory){

   override fun getTable(): Class<User> {
       return User::class.java
    }

}
