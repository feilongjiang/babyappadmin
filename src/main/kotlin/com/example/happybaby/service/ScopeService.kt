package com.example.happybaby.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.annotation.Propagation
import com.example.happybaby.entity.Scope;
import com.example.happybaby.dao.ScopeDao;
import com.example.happybaby.repo.ScopeRepo;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = false,rollbackFor = [Exception::class])
open class ScopeService : BaseService<Scope,ScopeDao,ScopeRepo>() {
   @Autowired
   open val scopeDao: ScopeDao? = null
   @Autowired
   open val scopeRepo: ScopeRepo? = null
   override fun dao(): ScopeDao {
       return scopeDao!!
   }
   override fun repo(): ScopeRepo {
       return scopeRepo!!
   }
}
