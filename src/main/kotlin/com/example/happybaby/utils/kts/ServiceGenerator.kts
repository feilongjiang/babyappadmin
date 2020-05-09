package com.example.happybaby.utils.kts

import java.io.File
import java.io.PrintWriter
import java.util.*

val database = "happybaby"
val PACKAGENAME = "com.example.${database}.service"
val ENTITYPACKAGE = "com.example.${database}.entity"
main()
fun main() {
    var start = Date().time
    println("开始生成 ")
    // 项目目录
    val BASEDIR = System.getProperty("user.dir").replace("com\\example\\${database}\\utils\\kts", "")
    // 要生成的包目录
    val outDir = BASEDIR + PACKAGENAME.replace(".", "/") + "/"
    val entDir = BASEDIR + ENTITYPACKAGE.replace(".", "/") + "/"
    var filesNames = getFileName(entDir)
    filesNames?.forEach {
        var className = it + "Service"
        var daoName = it + "Dao"
        var repoName = it + "Repo"
        println("正在转换表: " + className + ".kt")
        var entityName = it
        var out = File(outDir, "$className.kt").printWriter()
        generate(out, className, daoName, repoName, entityName)
    }
    var end = Date().time
    var time = end - start
    var hour = time / (1000 * 60 * 60)
    var minute = (time - hour * (1000 * 60 * 60)) / (1000 * 60)
    var second = (time - hour * (1000 * 60 * 60) - minute * (1000 * 60)) / 1000
    println("转换完成,共完成" + filesNames?.size + "个类, 用时" + hour + "时" + minute + "分" + second + "秒")
    System.exit(0)
}

fun generate(out: PrintWriter, className: String, daoName: String, repoName: String, entityName: String) {
    out.println("package $PACKAGENAME")
    out.println("")
    out.println("import org.springframework.beans.factory.annotation.Autowired")
    out.println("import org.springframework.stereotype.Service")
    out.println("import org.springframework.transaction.annotation.Transactional")
    out.println("import org.springframework.transaction.annotation.Propagation")
    out.println("import com.example.${database}.entity.$entityName;")
    out.println("import com.example.${database}.dao.$daoName;")
    out.println("import com.example.${database}.repo.$repoName;")
    out.println("")
    out.println("@Service")
    out.println("@Transactional(propagation = Propagation.REQUIRED, readOnly = false,rollbackFor = [Exception::class])")
    out.println("open class $className : BaseService<${entityName},${daoName},${repoName}>() {")
    out.println("   @Autowired")
    out.println("   open val " + headerTolowercase(daoName) + ": ${daoName}? = null")
    out.println("   @Autowired")
    out.println("   open val " + headerTolowercase(repoName) + ": ${repoName}? = null")
    var filter = arrayOf("OrderAnnex", "CollectionRecords",
            "OrderTask", "OrderTeam", "UserDepartment", "MenuPermission", "Message")
    /* if (!filter.contains(entityName)) {
         out.println("    open fun findByName(name: String) {")
         out.println("     this.repo().findByName(name)")
         out.println("   }")
     }*/
    out.println("   override fun dao(): $daoName {")
    out.println("       return " + headerTolowercase(daoName) + "!!")
    out.println("   }")
    out.println("   override fun repo(): $repoName {")
    out.println("       return " + headerTolowercase(repoName) + "!!")
    out.println("   }")
    out.println("}")
    out.flush()
}

/***
 * 首字母转小写
 *
 * @param para
 */
fun headerTolowercase(para: String): String? {
    return para[0].toLowerCase() + para.substring(1)
}

fun getFileName(path: String): List<String>? {
    val filterName = arrayOf("User", "Menu", "MenuPermission")
    var file = File(path)
    var files: Array<File>? = file.listFiles() ?: return null
    var fileNames = files!!.filter {
        ((it.name.indexOf(".kt") != -1 || it.name.indexOf(".java") != -1))
                && it.isFile &&
                !filterName.contains(it.name.replace(".kt", "").replace(".java", ""))
    }
            .flatMap { listOf(it.name.replace(".kt", "").replace(".java", "")) }
    var filter = arrayOf("User", "Menu", "Orders", "MenuPermission", "BaseEntity")
    return fileNames.filter {
        !filter.contains(it)
    }

}
