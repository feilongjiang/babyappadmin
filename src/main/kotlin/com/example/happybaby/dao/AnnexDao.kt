package com.example.happybaby.dao

import org.springframework.stereotype.Repository
import org.hibernate.SessionFactory
import com.example.happybaby.entity.Annex;

@Repository("AnnexDao")
open class AnnexDao(sessionFactory: SessionFactory) :BaseDao<Annex>(sessionFactory){

   override fun getTable(): Class<Annex> {
       return Annex::class.java
    }

}
