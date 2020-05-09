package com.example.happybaby.service

import com.example.happybaby.entity.Role
import com.example.happybaby.entity.User
import com.example.happybaby.utils.Bcry
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest
open class UserServiceTest {
    @Autowired
    private val userService: UserService? = null

    @Autowired
    private val roleService: RoleService? = null


    fun add() {
        userService!!.add(createUser())
    }

    private fun createUser(): List<User> {
        val users: MutableList<User> = ArrayList()
        val usernames = arrayOf("江飞龙", "老婆")
        val password = "123456"
        var length: Int
        var role: Role?
        for (i in usernames.indices) {
            if (usernames[i] == "宋总" || usernames[i] == "焦总") {
                role = roleService!!.findByName("admin")
                length = 12
            } else {
                length = 10
                role = roleService!!.findByName("user")
            }
            val user = User()
            user.name = usernames[i]
            user.password = Bcry.genPassword(password, length)
            user.roleId = role!!.id
            users.add(user)
        }
        return users
    }
}
