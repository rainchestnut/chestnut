package conf

import com.beust.klaxon.Klaxon
import entity.InputConfig
import entity.Option
import java.io.File
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Paths

/**
 * 快捷输入相关配置
 */
object ChestnutConf {
    val inputType = listOf(
        Option("DEFAULT", ""),
        Option("ASCII", "@ascii"),
        Option("BROWSER", "@browser"),
        Option("SPECIAL KEY", "@keyEvent"),
        Option("DELAY", "@delay"),
        Option("NOW", "@now"),
    )
    val quickInputData: MutableList<InputConfig>

    /**
     * 在当前用户的AppData/Local/chestnut/config目录下创建quick_input.json文件
     * 如果文件不存在，则从jar包中复制文件到该目录
     * 如果文件存在，则不做任何操作
     * quick_input.json文件用于存储快捷输入的配置
     */
    init {
        val destPath = Paths.get(System.getProperty("user.home"), "AppData","Local","chestnut","config","quick_input.json")
        val quickInputFile = File(destPath.toString())

        if (!quickInputFile.exists()) {
            Files.createDirectories(destPath.parent)
            val resource = ClassLoader.getSystemClassLoader()?.getResource("config/quick_input.json")
            val resourceStream = resource?.openStream() ?: throw FileNotFoundException("Resource not found")

            resourceStream.use { source ->
                quickInputFile.createNewFile()
                quickInputFile.outputStream().use { destination ->
                    source.copyTo(destination)
                }
            }
        }
        quickInputData = quickInputFile.inputStream().use { Klaxon().parseArray<InputConfig>(it)?.toMutableList()?: mutableListOf() }
    }

    /**
     * 重新加载快捷输入的配置,用于在程序运行时修改配置文件后重新加载
     */
    fun reloadQuickInputData() {
        val destPath = Paths.get(System.getProperty("user.home"), "AppData","Local","chestnut","config","quick_input.json")
        val quickInputFile = File(destPath.toString())
        quickInputFile.outputStream().use { Klaxon().toJsonString(quickInputData).byteInputStream().copyTo(it) }
    }
}