package com.example.happybaby.dao

import org.springframework.stereotype.Repository
import org.hibernate.SessionFactory
import com.example.happybaby.entity.Role;

@Repository("RoleDao")
open class RoleDao(sessionFactory: SessionFactory) :BaseDao<Role>(sessionFactory){

   override fun getTable(): Class<Role> {
       return Role::class.java
    }

}
