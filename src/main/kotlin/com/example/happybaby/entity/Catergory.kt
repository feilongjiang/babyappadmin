package com.example.happybaby.entity

import javax.persistence.*
@Entity
@Table(name = "catergory")
open class Catergory : BaseEntity() {

    @get:Basic
    @get:Column(name = "name", nullable = false, length = 32)
    open var  name:String?=null

    @get:Basic
    @get:Column(name = "description", nullable = true, length = 255)
    open var  description:String?=null

    @get:Basic
    @get:Column(name = "pid", nullable = false)
    open var  pid:Long?=null


}
