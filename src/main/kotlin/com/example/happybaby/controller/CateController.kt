package com.example.happybaby.controller

import com.example.happybaby.dao.CatergoryDao
import com.example.happybaby.entity.Catergory
import com.example.happybaby.exception.BaseHttpStatus
import com.example.happybaby.exception.MyException
import com.example.happybaby.repo.CatergoryRepo
import com.example.happybaby.service.BaseService
import com.example.happybaby.service.CatergoryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@CrossOrigin(origins = ["http://localhost:8080", "null"])
@RestController
@RequestMapping("api/category")
class CateController : BaseController<Catergory, CatergoryDao, CatergoryRepo>() {
    @Autowired
    internal var catergoryService: CatergoryService? = null

    override fun service(): BaseService<Catergory, CatergoryDao, CatergoryRepo> {
        if (this.catergoryService == null) {
            throw MyException(BaseHttpStatus.INTERNAL_SERVER_ERROR)
        }
        return this.catergoryService!!;
    }

}
