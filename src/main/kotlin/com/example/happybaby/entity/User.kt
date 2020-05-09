package com.example.happybaby.entity

import javax.persistence.*
@Entity
@Table(name = "user")
open class User : BaseEntity() {

    @get:Basic
    @get:Column(name = "name", nullable = false, length = 32)
    open var  name:String?=null

    @get:Basic
    @get:Column(name = "password", nullable = false, length = 60)
    open var  password:String?=null

    @get:Basic
    @get:Column(name = "role_id", nullable = false)
    open var  roleId:Long?=null

    @get:Basic
    @get:Column(name = "phone", nullable = true, length = 11)
    open var  phone:String?=null

    @get:Basic
    @get:Column(name = "email", nullable = true, length = 50)
    open var  email:String?=null

    @get:Basic
    @get:Column(name = "sex", nullable = true)
    open var  sex:Byte?=null

    @get:Basic
    @get:Column(name = "adress", nullable = true, length = 128)
    open var  adress:String?=null

    @get:Basic
    @get:Column(name = "token", nullable = true, length = 255)
    open var  token:String?=null

    @get:Basic
    @get:Column(name = "meid", nullable = true, length = 128)
    open var  meid:String?=null

    @get:Basic
    @get:Column(name = "avatar", nullable = true)
    open var  avatar:Long?=null


}
