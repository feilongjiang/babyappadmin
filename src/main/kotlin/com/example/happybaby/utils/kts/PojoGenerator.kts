package com.example.happybaby.utils.kts

import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.DriverManagerDataSource

import java.io.File
import java.io.PrintWriter
import java.sql.SQLException
import java.util.*
import kotlin.collections.ArrayList

val database = "happybaby"
val username = "root"
val password = "root"
val JDBC_DRIVER = "com.mysql.cj.jdbc.Driver"
val sqlUrl = "jdbc:mysql://127.0.0.1:3306/$database?serverTimezone=UTC&useUnicode=true&amp&characterEncoding=UTF8"
val packageName = "com.example.$database.entity"

main()
// 入口函数
fun main() {

    // 项目目录
    val BASEDIR = System.getProperty("user.dir").replace("com\\example\\$database\\utils\\kts", "")
    println("开始生成 " + BASEDIR)
    var start = Date().time
    // 要生成的包目录
    val dir = BASEDIR + packageName.replace(".", "/") + "/"
    println("开始生成 " + dir)
    println("开始生成 ")
    var tables = queryTable();
    for (table in tables) {
        println("正在转换表: " + table.tablename)
        var colums = table.columns
        colums.forEach {
            it.annos = annos(it)
            it.name = underlineToHump(it.name.toString(), false)
        }
        var className = underlineToHump(table.tablename!!, true) // 下划线转驼峰命名
        var file = File(dir, className + ".kt")
        if (!file.exists()) {
            file.createNewFile()
        }
        generate(file.printWriter(), table.tablename!!, className!!, colums)
    }
    var end = Date().time
    var time = end - start
    var hour = time / (1000 * 60 * 60)
    var minute = (time - hour * (1000 * 60 * 60)) / (1000 * 60)
    var second = (time - hour * (1000 * 60 * 60) - minute * (1000 * 60)) / 1000
    println("转换完成,共完成" + tables.size + "个表, 用时" + hour + "时" + minute + "分" + second + "秒")
    System.exit(0)
}


/**
 * 获取 JdbcTemplate
 * @param JdbcTemplate
 */
fun jdbcTemplate(): JdbcTemplate {
    val dataSource = DriverManagerDataSource() //数据源
    Class.forName(JDBC_DRIVER);
    dataSource.setDriverClassName(JDBC_DRIVER)
    dataSource.url = sqlUrl
    dataSource.username = username
    dataSource.password = password
    var jdbcTemplate: JdbcTemplate = JdbcTemplate(dataSource)
    return jdbcTemplate
}

/**
 * 用于生成实体类的表对象
 */
class Table {
    var tablename: String? = null
    var tableComment: String? = null
    var columns: MutableList<Column> = ArrayList()
}

/**
 * 用户接收未转换前端column数据
 */
class SqlColumn {
    var field: String? = null // 字段名
    var type: String? = null //是否为空
    var `null`: String? = null
    var key: String? = null //是否有主键或外键约束
    var default: String? = null// 默认值
    var extra: String? = null// 额外约束
}

/**
 * 转换后的columns数据
 */
class Column {
    var type: String? = null
    var name: String? = null
    var isNullable = false
    var comment: String? = null
    var key: String? = null
    var maxLength: Int = -1
    var annos: String? = null
}

/**
 *  获取转换后的表数据
 *  @return Table
 */
fun queryTable(): MutableList<Table> {
    /**
     * 查询所有表名
     */
    var sql = "SELECT table_name FROM information_schema. TABLES WHERE table_schema = '$database'"
    var tableNames = jdbcTemplate().queryForList(sql)
    var tables = ArrayList<Table>()
    for (tableName in tableNames) {
        /**
         * 查询表结构 返回 Field      | Type         | Null | Key | Default           | Extra
         */
        var csql = "show columns from " + tableName.get("table_name")
        var sqlColumns = query<SqlColumn>(csql, SqlColumn::class.java);
        var table = Table()
        table.tablename = tableName.get("table_name").toString() // 获取表名
        for (sqlColumn in sqlColumns) {
            var column = Column();
            column.name = sqlColumn.field
            column.isNullable = sqlColumn.`null`?.toLowerCase() == "yes"
            column.key = sqlColumn.key?.toLowerCase()
            sqlColumn.type?.let {
                column.type = caclType(it) // 计算kotlin类型
                if (column.type == "String") { // 计算字段长度
                    var index1 = it.indexOf("(")
                    var index2 = it.indexOf(")")
                    var len = it.substring(index1 + 1, index2).toInt()
                    column.maxLength = len
                }
            }
            table.columns.add(column)
        }
        tables.add(table)
    }
    return tables
}

// 查询sql数据
fun <T> query(sql: String, clazz: Class<T>): MutableList<T> {
    var results = jdbcTemplate().query(sql, BeanPropertyRowMapper<T>(clazz))
    return results
}

fun underlineToHump(para: String, capitalize: Boolean): String? {
    if (!para.contains("_")) {
        if (capitalize) {
            return para[0].toUpperCase() + para.substring(1)
        }
        return para
    }
    val result = StringBuilder()
    val a = para.split("_".toRegex()).toTypedArray()
    for (s in a) {
        if (result.isEmpty() && !capitalize) {
            result.append(s)
        } else {
            result.append(s.substring(0, 1).toUpperCase())
            result.append(s.substring(1))
        }
    }
    return result.toString()
}

fun generate(out: PrintWriter, tableName: String, className: String, columns: MutableList<Column>) {
    out.println("package $packageName")
    out.println("")
    out.println("import javax.persistence.*")
    out.println("@Entity")
    out.println("@Table(name = \"${tableName}\")")
    out.println("open class ${className} : BaseEntity() {")
    out.println("")
    columns.forEach {
        var filter = arrayOf("id", "createdAt", "updatedAt")
        if (!filter.contains(it.name)) {
            if (it.annos != null && it.annos != "")
                out.println("${it.annos}")
            if (it.type == "Int") {
                out.println("    open var  ${it.name}:${it.type}" + "=0")
            } else {
                out.println("    open var  ${it.name}:${it.type}" + "?=null")
            }
            out.println("")
        }
    }
    out.println("")
    out.println("}")
    out.flush()
}

/**
 * 计算数据类型
 * @param sqlType sql数据类型
 * @return String kotlin数据类型
 */
fun caclType(sqlType: String): String {
    val typeMapping = mapOf(
            "^(int).*" to "Int",
            "^(bigint).*" to "Long",
            "^(float|double|decimal|real).*" to "Double",
            "^(datetime|timestamp).*" to "java.sql.Timestamp",
            "^(date).*" to "java.sql.Date",
            "^time$" to "java.sql.Time",
            "^(tinyint).*" to "Byte",
            "^(varchar|char).*" to "String"
    )
    var type: String = ""
    typeMapping.forEach {
        val r1 = Regex(it.key)
        if (r1.matches(sqlType.trim())) {
            type = it.value
        }
    }
    println("我是类型" + sqlType)
    if (type == "") throw SQLException()
    return type
}

/**
 * 计算注释
 */
fun annos(column: Column): String {
    var ann = StringBuilder()
    if (column.key == "pri") {
        ann.append("    @get:GeneratedValue(strategy = GenerationType.IDENTITY)\n")
        ann.append("    @get:Column(name = \"id\", nullable = false)\n")
        ann.append("    @get:Id")
        return ann.toString()
    }
    ann.append("    @get:Basic\n")
    ann.append("    @get:Column(name = \"" + column.name + "\", nullable = " + column.isNullable.toString())
    if (column.type == "String") {
        ann.append(", length = " + column.maxLength)
    }
    if (column.name == "created_at") {
        ann.append(",updatable = false")
    }
    when (column.type) {
        "Int" -> {

        }
        "double" -> {

        }
        "java.sql.Timestamp" -> {

        }
        "java.sql.Date" -> {

        }
        "java.sql.Time" -> {

        }
        "String" -> {

        }
    }
    ann.append(")")
    return ann.toString()
}

