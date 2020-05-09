package com.example.happybaby.entity

import javax.persistence.*
@Entity
@Table(name = "role")
open class Role : BaseEntity() {

    @get:Basic
    @get:Column(name = "name", nullable = false, length = 32)
    open var  name:String?=null

    @get:Basic
    @get:Column(name = "display_name", nullable = true, length = 32)
    open var  displayName:String?=null

    @get:Basic
    @get:Column(name = "descrpition", nullable = true, length = 255)
    open var  descrpition:String?=null


}
