package com.example.happybaby.entity

import com.fasterxml.jackson.annotation.JsonFormat
import java.sql.Timestamp
import javax.persistence.*

@MappedSuperclass
abstract class BaseEntity {
    @get:GeneratedValue(strategy = GenerationType.IDENTITY)
    @get:Column(name = "id", nullable = false)
    @get:Id
    open var id: Long = 0
    @get:Basic
    @get:Column(name = "created_at", nullable = false, updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    open var createdAt: java.sql.Timestamp? = null

    @get:Basic
    @get:Column(name = "updated_at", nullable = false,columnDefinition="timestamp default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    open var updatedAt: java.sql.Timestamp? = null
}
