package com.example.happybaby.utils.kts

import java.io.File
import java.io.PrintWriter
import java.util.*
val database = "happybaby"
val PACKAGENAME = "com.example.${database}.dao"
val ENTITYPACKAGE = "com.example.${database}.entity"
main()
fun main() {
    var start = Date().time
    println("开始生成 ")
    // 项目目录
    val BASEDIR1 = System.getProperty("user.dir")
    println("开始生成1 "+BASEDIR1)
    val BASEDIR = BASEDIR1.replace("com\\example\\happybaby\\utils\\kts", "")
    println("开始生成 2"+BASEDIR)
    // 要生成的包目录
    val outDir = BASEDIR + PACKAGENAME.replace(".", "/") + "/"
    val entDir = BASEDIR + ENTITYPACKAGE.replace(".", "/") + "/"
    println("开始生成 3"+outDir+"-----------"+entDir)
    var filesNames = getFileName(entDir)
    filesNames?.forEach {
        var className = it + "Dao"
        println("正在转换表: " + className + ".kt")
        var entityName = it
        var out = File(outDir, "$className.kt").printWriter()
        generate(out, className, entityName)
    }
    var end = Date().time
    var time = end - start
    var hour = time / (1000 * 60 * 60)
    var minute = (time - hour * (1000 * 60 * 60)) / (1000 * 60)
    var second = (time - hour * (1000 * 60 * 60) - minute * (1000 * 60)) / 1000
    println("转换完成,共完成" + filesNames?.size + "个类, 用时" + hour + "时" + minute + "分" + second + "秒")
    System.exit(0)
}


fun HumpToUnderline(para: String): String {
    var sb = StringBuilder(para);
    var temp = 0;//定位
    if (!para.contains("_")) {
        var i = 0;
        para.forEach {
            if (Character.isUpperCase(it) && i != 0) {
                sb.insert(i + temp, "_");
                temp += 1;
            }
            i++
        }
    }
    return sb.toString()
}


fun generate(out: PrintWriter, className: String, entityName: String) {
    out.println("package $PACKAGENAME")
    out.println("")
    out.println("import org.springframework.stereotype.Repository")
    out.println("import org.hibernate.SessionFactory")
    out.println("import com.example.${database}.entity.$entityName;")
    out.println("")
    out.println("@Repository(\"" + className + "\")")
    out.println("open class ${className}(sessionFactory: SessionFactory) :BaseDao<${entityName}>(sessionFactory){")
    out.println("")
    out.println("   override fun getTable(): Class<${entityName}> {")
    out.println("       return ${entityName}::class.java")
    out.println("    }")
    out.println("")
   /*
    var filter = arrayOf("OrderAnnex", "CollectionRecords",
            "OrderTask", "OrderTeam", "UserDepartment", "MenuPermission", "Message")
    if (!filter.contains(entityName)) {
        out.println("   fun findByName(name: String): ${entityName}")
        out.println("   fun deleteByName(name: String): Boolean")
    }*/
    out.println("}")
    out.flush()
}

fun getFileName(path: String): List<String>? {
    var file = File(path)
    var files: Array<File>? = file.listFiles() ?: return null
    var fileNames = files!!.filter { it.isFile && ((it.name.indexOf(".kt") != -1 || it.name.indexOf(".java") != -1)) }
            .flatMap { listOf(it.name.replace(".kt", "").replace(".java", "")) }
    return fileNames.filter {
        it != "BaseEntity"
    }

}
