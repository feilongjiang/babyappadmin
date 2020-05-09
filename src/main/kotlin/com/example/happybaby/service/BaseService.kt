package com.example.happybaby.service

import com.example.happybaby.dao.BaseDao
import com.example.happybaby.exception.BaseHttpStatus
import com.example.happybaby.exception.MyException
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Transactional(propagation = Propagation.REQUIRED, readOnly = false,rollbackFor = [Exception::class])
abstract class BaseService<T, E : BaseDao<T>, F : JpaRepository<T, Long>> : InterfaceService<T> {
    /*abstract fun readMap(id: Long): MutableMap<String, *>;
    abstract fun readList(): MutableList<*>;*/
    //abstract fun createMap(map:MutableMap<String,*>);
    open fun read(id: Long): Any? {
        return this.dao().findById(id)
    }

    open fun read(): List<T>? {
        var elements = this.dao().findAll()
        return elements
    }

    open fun read(pageable: Pageable): MutableMap<String, Any>? {
        var page = this.repo().findAll(pageable) ?: return null
        var result: MutableMap<String, Any> = TreeMap<String, Any>()
        result.put("elements", page.toList())
        result.put("total", page.totalPages)
        return result
    }

    fun pagination(page: Int = 1, total: Int = 0, limit: Int = 10): Map<String, Int> {
        return mapOf(
                "page" to page,
                "limit" to limit,
                "total" to total
        )
    }

    override fun update(element: T) {
        this.dao().update(element)
    }

    open fun create(element: T) {
        this.dao().update(element)
    }

    open fun create(map: MutableMap<String, Any>) {
        throw MyException(BaseHttpStatus.PARAM_IS_INVALID)
    }

    override fun delete(id: Long) {
        this.dao().delete(this.dao().findById(id))
    }


    abstract fun dao(): E;
    abstract fun repo(): JpaRepository<T, Long>

    // 插入一个
    override open fun add(element: T) {
        this.dao().insert(element)
    }

    override fun add(elements: List<T>) {
        this.dao().insert(elements)
    }

    override fun delete(element: T) {
        this.dao().delete(element)
    }

    /**
     * 查询单个
     */
    override fun query(id: Long): T {
        return this.dao().findById(id)
    }

    /**
     * 根据id查询多个
     */
    override fun query(ids: Array<Long>): List<T> {
        return this.dao().findByIds(ids)
    }

    /**
     * 查询全部
     */
    override fun query(): List<T>? {
        return this.dao().findAll()
    }


    //查询全部用户返回指定字段
    open fun filterFieldQuery(params: Array<String>): List<T>? {
        return this.dao().filterField(params).findAll()
    }
    // 条件查询

    //条件查询多个结果
    open fun query(arg: String, params: Any): MutableList<T>? {
        var result: MutableList<T>? = this.dao().where(arg, params)?.findByDC() as? MutableList<T>
        return result

    }

    //条件查询一个结果
    open fun queryOne(arg: String, params: Any): T? {
        if (this.query(arg, params)?.isEmpty()!!) {
            return null
        } else {
            return this.query(arg, params)?.get(0)
        }
    }

    open fun findByName(name: String): T? {
        return queryOne("name", name)
    }
}
