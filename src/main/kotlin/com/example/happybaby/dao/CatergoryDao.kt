package com.example.happybaby.dao

import org.springframework.stereotype.Repository
import org.hibernate.SessionFactory
import com.example.happybaby.entity.Catergory;

@Repository("CatergoryDao")
open class CatergoryDao(sessionFactory: SessionFactory) :BaseDao<Catergory>(sessionFactory){

   override fun getTable(): Class<Catergory> {
       return Catergory::class.java
    }

}
