package com.example.happybaby.entity

import javax.persistence.*
@Entity
@Table(name = "scope")
open class Scope : BaseEntity() {

    @get:Basic
    @get:Column(name = "username", nullable = false, length = 32)
    open var  username:String?=null

    @get:Basic
    @get:Column(name = "cate_name", nullable = false, length = 32)
    open var  cateName:String?=null

    @get:Basic
    @get:Column(name = "scope", nullable = false)
    open var  scope:Int=0


}
