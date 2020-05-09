package com.example.happybaby.config

import com.zaxxer.hikari.HikariDataSource
import me.liuwj.ktorm.database.Database
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource


@Configuration
open class KtormConfiguration {

    @Autowired
    lateinit var dataSource: DataSource

    //@Autowired
    // lateinit var dataSource: HikariDataSource

    @Bean
    open fun database(): Database {
        return Database.connectWithSpringSupport(dataSource)
    }
}
