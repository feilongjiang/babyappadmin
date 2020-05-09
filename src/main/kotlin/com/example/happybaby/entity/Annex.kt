package com.example.happybaby.entity

import javax.persistence.*
@Entity
@Table(name = "annex")
open class Annex : BaseEntity() {

    @get:Basic
    @get:Column(name = "name", nullable = true, length = 128)
    open var  name:String?=null

    @get:Basic
    @get:Column(name = "type", nullable = true, length = 32)
    open var  type:String?=null

    @get:Basic
    @get:Column(name = "size", nullable = true)
    open var  size:Int=0

    @get:Basic
    @get:Column(name = "url", nullable = false, length = 255)
    open var  url:String?=null

    @get:Basic
    @get:Column(name = "status", nullable = true)
    open var  status:Byte?=null


}
