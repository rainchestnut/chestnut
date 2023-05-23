package conf

import java.util.*

enum class Env {
    MAC,WINDOWS,LINUX,UNKNOWN;
}
object SystemConf {
    val env = getOperatingSystem()
}

/**
 * 获取当前操作系统
 */
fun getOperatingSystem(): Env {
    val os: String = System.getProperty("os.name").lowercase(Locale.getDefault())
    return when {
        os.contains("win") -> Env.WINDOWS
        os.contains("mac") -> Env.MAC
        os.contains("nix") || os.contains("nux") || os.contains("aix") -> Env.LINUX
        else -> Env.UNKNOWN
    }
}