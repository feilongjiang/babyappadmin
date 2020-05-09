package com.example.happybaby.service

import com.example.happybaby.entity.Catergory
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.LinkedHashMap

@SpringBootTest
open class CatergoryServiceTest {
    @Autowired
    var catergoryService: CatergoryService? = null


    fun createCate() {
        var catergorys = getCate();
        catergoryService!!.add(catergorys)
    }

    fun update() {
        var category = catergoryService!!.query(1L)
        catergoryService!!.update(category)
    }

    fun getCate(): MutableList<Catergory> {
        var catergorys: MutableList<Catergory> = ArrayList<Catergory>()
        var add_reduce = arrayListOf("个位数加减法", "两位数加减法", "三位数加减法", "三位数以下混合加减法", "个位数乘法", "两位数乘法",
                "个位数除法", "两位数除法", "个位数四则运算", "两位数四则运算")
        var rootId = 1L;
        for (name in add_reduce) {
            var root = Catergory();
            root.name = name;
            root.pid = 0;
            root.id = rootId++
            catergorys.add(root)
        }
        return catergorys
    }
}
