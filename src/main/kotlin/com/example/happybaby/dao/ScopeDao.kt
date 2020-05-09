package com.example.happybaby.dao

import org.springframework.stereotype.Repository
import org.hibernate.SessionFactory
import com.example.happybaby.entity.Scope;

@Repository("ScopeDao")
open class ScopeDao(sessionFactory: SessionFactory) :BaseDao<Scope>(sessionFactory){

   override fun getTable(): Class<Scope> {
       return Scope::class.java
    }

}
