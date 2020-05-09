package com.example.happybaby.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.annotation.Propagation
import com.example.happybaby.entity.Catergory;
import com.example.happybaby.dao.CatergoryDao;
import com.example.happybaby.repo.CatergoryRepo;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = [Exception::class])
open class CatergoryService : BaseService<Catergory, CatergoryDao, CatergoryRepo>() {
    @Autowired
    open val catergoryDao: CatergoryDao? = null

    @Autowired
    open val catergoryRepo: CatergoryRepo? = null
    override fun dao(): CatergoryDao {
        return catergoryDao!!
    }

    override fun repo(): CatergoryRepo {
        return catergoryRepo!!
    }
}
