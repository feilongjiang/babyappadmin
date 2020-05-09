package com.example.happybaby.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.annotation.Propagation
import com.example.happybaby.entity.Role;
import com.example.happybaby.dao.RoleDao;
import com.example.happybaby.repo.RoleRepo;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = false,rollbackFor = [Exception::class])
open class RoleService : BaseService<Role,RoleDao,RoleRepo>() {
   @Autowired
   open val roleDao: RoleDao? = null
   @Autowired
   open val roleRepo: RoleRepo? = null
   override fun dao(): RoleDao {
       return roleDao!!
   }
   override fun repo(): RoleRepo {
       return roleRepo!!
   }
}
