package com.example.happybaby.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.annotation.Propagation
import com.example.happybaby.entity.Annex;
import com.example.happybaby.dao.AnnexDao;
import com.example.happybaby.repo.AnnexRepo;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = false,rollbackFor = [Exception::class])
open class AnnexService : BaseService<Annex,AnnexDao,AnnexRepo>() {
   @Autowired
   open val annexDao: AnnexDao? = null
   @Autowired
   open val annexRepo: AnnexRepo? = null
   override fun dao(): AnnexDao {
       return annexDao!!
   }
   override fun repo(): AnnexRepo {
       return annexRepo!!
   }
}
