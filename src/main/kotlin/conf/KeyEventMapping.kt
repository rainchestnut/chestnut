package conf

import com.beust.klaxon.Klaxon
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

/**
 * 快捷输入相关配置描述
 */
data class KeyEventMapping(val key: String, val desc: String)

/**
 * 快捷输入相关配置及描述
 */
object KeyEventMappings {
    private val keyToDescription: MutableMap<String, String> = mutableMapOf(
        "VK_CONTROL" to "Ctrl",
        "VK_ENTER" to "Enter",
        "VK_ALT" to "Alt",
        "VK_SHIFT" to "Shift",
        "VK_META" to "Command (macOS)"
        // 其他键的映射...
    )

    val descriptionToKey: Map<String, String>

    /**
     * 在当前用户的AppData/Local/chestnut/config目录下创建key_event.json文件
     * 如果文件不存在，则从jar包中复制文件到该目录
     * 如果文件存在，则不做任何操作
     * key_event.json文件用于存储快捷输入配置列表的配置
     */
    init {
        // 读取键值映射的 JSON 文件
        val destPath = Paths.get(System.getProperty("user.home"), "AppData","Local","chestnut","config","key_event.json")
        val keyEventFile = File(destPath.toString())
        if (!keyEventFile.exists()) {
            Files.createDirectories(destPath.parent)

            val resource = ClassLoader.getSystemClassLoader()?.getResource("config/key_event.json")
            val resourceStream = resource?.openStream() ?: throw Exception("Resource not found")

            resourceStream.use { source ->
                keyEventFile.createNewFile()
                keyEventFile.outputStream().use { destination ->
                    source.copyTo(destination)
                }
            }
        }
        keyEventFile.inputStream().use { Klaxon().parseArray<KeyEventMapping>(it) }?.forEach { mapping ->
            keyToDescription[mapping.key] = mapping.desc
        }
        descriptionToKey = keyToDescription.entries.associate { (key, value) -> value to key }
    }
}