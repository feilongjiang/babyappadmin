package com.example.happybaby.controller

import com.example.happybaby.dao.BaseDao
import com.example.happybaby.service.BaseService
import com.example.happybaby.utils.APIResult
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.web.bind.annotation.*

interface IBaseController<T, E : BaseDao<T>, F : JpaRepository<T, Long>> {
    // service
    fun service(): BaseService<T, E, F>

    //查询
    fun read(): APIResult


    //通过id查询一个
    fun read(@PathVariable id: Long): APIResult


    fun read(pageable: Pageable): APIResult

    //增加
    fun create(@RequestBody(required = false) map: MutableMap<String, Any>, model: T): APIResult

    //更新
    fun update(model: T): APIResult

    //删除
    fun delete(@PathVariable id: Long): APIResult
}

abstract class BaseController<T, E : BaseDao<T>, F : JpaRepository<T, Long>> : IBaseController<T, E, F> {

    @RequestMapping(value = ["readpage"], method = [RequestMethod.GET])
    override fun read(pageable: Pageable): APIResult {
        return APIResult.ok(this.service().read(pageable))
    }

    @RequestMapping(value = ["read"], method = [RequestMethod.GET])
    override fun read(): APIResult {
        return APIResult.ok(this.service().read())
    }

    //查询一个对象
    @RequestMapping(value = ["read/{id}"], method = [RequestMethod.GET])
    override fun read(@PathVariable id: Long): APIResult {
        return APIResult.ok(this.service().read(id))
    }


    @RequestMapping(value = ["create"], method = [RequestMethod.POST])
    override fun create(@RequestBody(required = false) map: MutableMap<String, Any>, model: T): APIResult {
        if (map.isEmpty()) {
            this.service().create(model)
        } else {
            this.service().create(map)
        }
        return APIResult.ok()
    }


    //更新
    @RequestMapping(value = ["update"], method = [RequestMethod.GET])
    override fun update(model: T): APIResult {
        this.service().update(model)
        return APIResult.ok()
    }

    //删除
    @RequestMapping(value = ["delete/{id}"], method = [RequestMethod.DELETE])
    override fun delete(@PathVariable id: Long): APIResult {
        this.service().delete(id)
        return APIResult.ok()
    }


    companion object {
        val models: String? = "models"
    }
}
