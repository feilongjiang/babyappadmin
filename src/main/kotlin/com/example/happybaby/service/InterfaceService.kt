package com.example.happybaby.service


interface InterfaceService<T> {
    fun add(element: T)
    fun add(elements: List<T>)
    fun delete(id: Long)
    fun delete(element: T)
    fun query(id: Long): T
    fun query(id: Array<Long>): List<T>
    fun query(): List<T>?
    fun update(element: T)
}
