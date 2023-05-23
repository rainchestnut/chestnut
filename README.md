## Chestnut: Quick Input Desktop Tool

Chestnut is a tool built with Compose for Desktop API, designed to facilitate swift input at the cursor's focus point by simulating keyboard operations. Due to some limitations, the tool currently does not support the macOS platform, as Compose's `focusable = false` property doesn't guarantee that the original window will maintain its focus when multiple windows are open.
### download: [chestnut-1.0.0.msi](release%2Fchestnut-1.0.0.msi)
### Key Features:
1. **Arbitrary Character Quick Input:** This feature supports non-command line interfaces and utilizes the clipboard for function implementation, since some command lines do not support the `CTRL + V` paste command.
2. **ASCII Code Quick Input:** Swiftly enter ASCII codes with ease.
3. **Quick Browser Navigation:** Swiftly open websites in your browser.
4. **Quick Current Time Output:** Display the current time in a specified format.
5. **Custom Keyboard Shortcut Operations:** The tool supports all keyboard shortcuts that can be defined through configurations.

These features can be combined in any order. For instance, you can open `baidu.com`, enter the current time, and hit enter to search, all using quick input shortcuts.

### Customization:

**Shortcut Key Configuration:** The tool allows for shortcut key customization through a JSON format configuration file. The configuration file path is `~/AppData/Local/chestnut/config/quick_input.json`.

**UI Shortcut Configuration:** Chestnut also supports configuring shortcuts via a user-friendly UI.
---
![edit.png](readme%2Fedit.png)![act.png](readme%2Fact.png)

**Special Key Configuration:** Chestnut offers initial support for basic keys. Configuring special keys must be done through a configuration file located at `~/AppData/Local/chestnut/config/key_event.json`.

---

I hope this revision helps to better represent your project. I will now translate it into English:

## Chestnut：快速输入桌面工具

Chestnut 是一个基于 Compose for Desktop API 的工具，通过模拟键盘操作在光标聚焦点提供快速输入。由于某些限制，该工具目前不支持 macOS 平台，因为在打开多个窗口时，Compose 的 `focusable = false` 属性不能保证原始窗口将保持焦点。

### 主要特性:
1. **任意字符快速输入:** 该功能支持非命令行界面，通过剪贴板实现功能，因为部分命令行不支持 `CTRL + V` 粘贴命令。
2. **ASCII码快速输入:** 轻松快速输入 ASCII 码。
3. **快速浏览器导航:** 在浏览器中快速打开网站。
4. **快速当前时间输出:** 以指定格式显示当前时间。
5. **自定义键盘快捷键操作:** 该工具支持可以通过配置定义的所有键盘快捷键。


### 自定义:

这些特性可以以任意顺序组合。例如，使用快速输入快捷键打开 `baidu.com`，输入当前时间，然后按回车进行搜索。

**快捷键配置:** 该工具允许通过 JSON 格式配置文件自定义快捷键。配置文件路径为 `~/AppData/Local/chestnut/config/quick_input.json`。

**UI 快捷键配置:** Chestnut 还提供了一个用户友好的界面进行快捷键配置。
![edit.png](readme%2Fedit.png)![act.png](readme%2Fact.png)

**特殊键位配置:** Chestnut 默认支持基础键位。特殊键位的配置需要通过配置文件完成，位于 `~/AppData/Local/chestnut/config/key_event.json`。
