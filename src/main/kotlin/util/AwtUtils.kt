package util

import conf.Env
import conf.SystemConf
import java.awt.Desktop
import java.awt.Robot
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.awt.event.KeyEvent
import java.net.URI
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


/**
 * 快捷输入相关工具类
 */
object AwtUtils {
    private var robot: Robot = Robot()

    /**
     * 通过Robot类模拟键盘输入到焦点处，支持特殊键位，如Ctrl、Shift、Alt、Command等
     * 代码待优化
     */
    fun writeToFocus(text: List<String>) {
        val clipboard = Toolkit.getDefaultToolkit().systemClipboard
        val originalContents = clipboard.getContents(null)
        for (item in text) {
            when {
                item.startsWith("@keyEvent ") -> { keyboardSpecialInput(robot, item.removePrefix("@keyEvent ")) }
                item.startsWith("@ascii ") -> { keyboardAsciiInput(robot, item.removePrefix("@ascii ")) }
                item.startsWith("@browser ") -> { openBrowser(item.removePrefix("@browser ").trim()) }
                item.startsWith("@delay ") -> { robot.delay(item.removePrefix("@delay ").toInt()) }
                item.startsWith("@now ") -> { keyboardAsciiInput(robot,LocalDateTime.now().format(DateTimeFormatter.ofPattern(item.removePrefix("@now ")))) }
                else -> {
                    clipboard.setContents(StringSelection(item), null)
                    pasteAction(robot)
                    robot.delay(100)
                    clipboard.setContents(StringSelection(null), null)
                }
            }
        }
        clipboard.setContents(originalContents, null)
    }
}
private fun keyboardSpecialInput(robot: Robot,item:String){
    val v = item.split("|").map {
        val field = KeyEvent::class.java.getField(it)
        field.getInt(null)
    }
    for (i in v) {
        robot.keyPress(i)
    }
    for (i in v) {
        robot.keyRelease(i)
    }
}
private fun pasteAction(robot: Robot) {
    when (SystemConf.env) {
        Env.WINDOWS, Env.LINUX -> {
            robot.keyPress(KeyEvent.VK_CONTROL)
            robot.keyPress(KeyEvent.VK_V)
            robot.keyRelease(KeyEvent.VK_V)
            robot.keyRelease(KeyEvent.VK_CONTROL)
        }

        Env.MAC -> {
            robot.keyPress(KeyEvent.VK_META)
            robot.keyPress(KeyEvent.VK_V)
            robot.keyRelease(KeyEvent.VK_V)
            robot.keyRelease(KeyEvent.VK_META)
        }

        else -> {
            throw Exception("unknown system")
        }
    }
}
private fun openBrowser(url: String) {
    Desktop.getDesktop().browse(URI(url))
}

private fun keyboardAsciiInput(robot: Robot, str: String) {
    // 判断是否存在非ascii字符
    str.any { it.code > 127 }.apply { if (this) throw Exception("not support non-ascii char") }
    for (char in str) {
        val arr = KeyMap.get(char);
        for (i in arr) {
            robot.keyPress(i)
        }
        for (i in arr) {
            robot.keyRelease(i)
        }
    }
}

object KeyMap {
    fun get(ch: Char): List<Int> {
        return keyMapping[ch] ?: throw Exception("not found key mapping for $ch")
    }

    private val keyMapping: Map<Char, List<Int>>

    init {
        keyMapping = mutableMapOf()

// 添加小写字母映射
        ('a'..'z').forEach {
            keyMapping[it] = listOf(KeyEvent.getExtendedKeyCodeForChar(it.code))
        }

// 添加大写字母映射
        ('A'..'Z').forEach {
            keyMapping[it] = listOf(KeyEvent.VK_SHIFT, KeyEvent.getExtendedKeyCodeForChar(it.code))
        }

// 添加数字映射
        ('0'..'9').forEach {
            keyMapping[it] = listOf(KeyEvent.getExtendedKeyCodeForChar(it.code))
        }

// 添加特殊字符映射
        val specialCharMapping = mapOf(
            ' ' to listOf(KeyEvent.VK_SPACE),
            '.' to listOf(KeyEvent.VK_PERIOD),
            ',' to listOf(KeyEvent.VK_COMMA),
            ';' to listOf(KeyEvent.VK_SEMICOLON),
            ':' to listOf(KeyEvent.VK_COLON),
            '?' to listOf(KeyEvent.VK_SHIFT, KeyEvent.VK_SLASH),
            '!' to listOf(KeyEvent.VK_SHIFT, KeyEvent.VK_1),
            '@' to listOf(KeyEvent.VK_SHIFT, KeyEvent.VK_2),
            '#' to listOf(KeyEvent.VK_SHIFT, KeyEvent.VK_3),
            '$' to listOf(KeyEvent.VK_SHIFT, KeyEvent.VK_4),
            '%' to listOf(KeyEvent.VK_SHIFT, KeyEvent.VK_5),
            '^' to listOf(KeyEvent.VK_SHIFT, KeyEvent.VK_6),
            '&' to listOf(KeyEvent.VK_SHIFT, KeyEvent.VK_7),
            '*' to listOf(KeyEvent.VK_SHIFT, KeyEvent.VK_8),
            '(' to listOf(KeyEvent.VK_SHIFT, KeyEvent.VK_9),
            ')' to listOf(KeyEvent.VK_SHIFT, KeyEvent.VK_0),
            '-' to listOf(KeyEvent.VK_MINUS),
            '_' to listOf(KeyEvent.VK_SHIFT, KeyEvent.VK_MINUS),
            '=' to listOf(KeyEvent.VK_EQUALS),
            '+' to listOf(KeyEvent.VK_SHIFT, KeyEvent.VK_EQUALS),
            '[' to listOf(KeyEvent.VK_OPEN_BRACKET),
            '{' to listOf(KeyEvent.VK_SHIFT, KeyEvent.VK_OPEN_BRACKET),
            ']' to listOf(KeyEvent.VK_CLOSE_BRACKET),
            '}' to listOf(KeyEvent.VK_SHIFT, KeyEvent.VK_CLOSE_BRACKET),
            '\\' to listOf(KeyEvent.VK_BACK_SLASH),
            '|' to listOf(KeyEvent.VK_SHIFT, KeyEvent.VK_BACK_SLASH),
            '/' to listOf(KeyEvent.VK_SLASH),
            '\'' to listOf(KeyEvent.VK_QUOTE),
            '\"' to listOf(KeyEvent.VK_SHIFT, KeyEvent.VK_QUOTE),
            '`' to listOf(KeyEvent.VK_BACK_QUOTE),
            '~' to listOf(KeyEvent.VK_SHIFT, KeyEvent.VK_BACK_QUOTE),
            '<' to listOf(KeyEvent.VK_SHIFT, KeyEvent.VK_COMMA),
            '>' to listOf(KeyEvent.VK_SHIFT, KeyEvent.VK_PERIOD)
        )
        keyMapping.putAll(specialCharMapping)
    }
}