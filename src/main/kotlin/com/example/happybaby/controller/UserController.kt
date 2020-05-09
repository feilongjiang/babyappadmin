package com.example.happybaby.controller

import com.example.happybaby.dao.UserDao
import com.example.happybaby.entity.User
import com.example.happybaby.exception.BaseHttpStatus
import com.example.happybaby.exception.MyException
import com.example.happybaby.repo.UserRepo
import com.example.happybaby.service.BaseService
import com.example.happybaby.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@CrossOrigin(origins = ["http://localhost:8080", "null"])
@RestController
@RequestMapping("api/user")
class UserController : BaseController<User, UserDao, UserRepo>() {
    @Autowired
    internal var userService: UserService? = null

    override fun service(): BaseService<User, UserDao, UserRepo> {
        if (this.userService == null) {
            throw MyException(BaseHttpStatus.INTERNAL_SERVER_ERROR)
        }
        return this.userService!!;
    }

}
