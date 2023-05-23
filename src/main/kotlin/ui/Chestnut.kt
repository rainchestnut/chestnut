package ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogState
import conf.ChestnutConf
import conf.KeyEventMappings
import entity.InputConfig
import entity.Option
import java.util.*

object Chestnut {
    @Composable
    fun EditDialog(
        origin: InputConfig? = null,
        title: String = "Edit",
        onSubmitRequest: (InputConfig) -> Unit = {},
        onCloseRequest: () -> Unit
    ) {
        Dialog(state = DialogState(size = DpSize(500.dp, 450.dp)), title = title, onCloseRequest = onCloseRequest) {
            MaterialTheme(
                colors = darkColors()
            ) {
                Surface(
                    color = MaterialTheme.colors.background, modifier = Modifier.fillMaxSize(), elevation = 10.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        var desc by remember { mutableStateOf(origin?.desc ?: "") }
                        var seq: String by remember { mutableStateOf((origin?.seq ?: 0).toString()) }
                        val events = remember {
                            mutableStateListOf(
                                *(origin?.let { formatValues(it.values).toTypedArray() } ?: arrayOf(
                                    QuickKeyEvent("", "")
                                ))
                            )
                        }
                        Row(modifier = Modifier.padding(5.dp)) {
                            OutlinedTextField(
                                label = { Text("Desc", fontSize = TextUnit(12f, TextUnitType.Sp)) },
                                value = desc,
                                textStyle = TextStyle(fontSize = TextUnit(12f, TextUnitType.Sp)),
                                onValueChange = { desc = it },
                                modifier = Modifier.weight(0.7f)
                            )
                            Spacer(modifier = Modifier.weight(0.05f))
                            OutlinedTextField(
                                label = { Text("Seq", fontSize = TextUnit(12f, TextUnitType.Sp)) },
                                value = seq,
                                onValueChange = { newValue ->
                                    if (newValue.toIntOrNull() in 0 until 1000 || newValue == "") {
                                        seq = newValue
                                    }
                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(0.25f)
                            )
                        }
                        QuickEventList(modifier = Modifier.align(Alignment.CenterHorizontally), events)

                        Row(modifier = Modifier.padding(5.dp).align(Alignment.End)) {
                            Button(onClick = {
                                val item = buildInputConfig(
                                    desc,
                                    seq.toInt(),
                                    events,
                                    origin?.id ?: UUID.randomUUID().toString()
                                )
                                reloadQuickInputData(item)
                                onSubmitRequest(item)
                                onCloseRequest()
                            }) {
                                Text("confirm")
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun ListDialog(content: MutableList<InputConfig>, onCloseRequest: () -> Unit) {
        Dialog(state = DialogState(size = DpSize(400.dp, 450.dp)), title = "List", onCloseRequest = onCloseRequest) {
            MaterialTheme(
                colors = darkColors()
            ) {
                Surface(
                    color = MaterialTheme.colors.background, modifier = Modifier.fillMaxSize(), elevation = 10.dp
                ) {
                    Column {
                        Row(modifier = Modifier.padding(top = 5.dp, start = 16.dp, end = 16.dp, bottom = 0.dp)) {
                            Text("SEQ", modifier = Modifier.weight(0.2f).padding(top = 5.dp))
                            Text("DESC", modifier = Modifier.weight(0.6f).height(45.dp).padding(top = 5.dp))
                            Spacer(modifier = Modifier.weight(0.2f))
                        }
                        Divider(color = MaterialTheme.colors.onBackground, thickness = 1.dp)
                        LazyColumn(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {
                            itemsIndexed(content.sortedBy { it.seq }, key = { _, item -> item.id }) { _, row ->
                                Row(modifier = Modifier.padding(top = 5.dp)) {
                                    Text(row.seq.toString(), modifier = Modifier.weight(0.2f).padding(top = 10.dp))
                                    Text(row.desc, modifier = Modifier.weight(0.6f).height(45.dp).padding(top = 10.dp))
                                    var isEdit by remember { mutableStateOf(false) }
                                    Common.EditIcon { isEdit = !isEdit }
                                    Common.DeleteIcon {
                                        content.removeIf { item -> item.id == row.id }
                                        ChestnutConf.quickInputData.removeIf { item -> item.id == row.id }
                                        ChestnutConf.reloadQuickInputData()
                                    }
                                    if (isEdit) EditDialog(row, onSubmitRequest = {
                                        content.removeIf { item -> item.id == it.id }
                                        content.add(it)
                                        content.sortBy { ite -> ite.seq }
                                    }) { isEdit = false }
                                }
                                Divider(color = MaterialTheme.colors.onBackground, thickness = 1.dp)
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun QuickEventList(modifier: Modifier = Modifier, events: MutableList<QuickKeyEvent>) {
        val options = ChestnutConf.inputType
        LazyColumn(modifier = Modifier.height(240.dp)) {
            itemsIndexed(events, key = { _, event -> event.id }) { index, row ->
                Row(modifier = Modifier.padding(top = 5.dp).then(modifier)) {
                    var rowType by remember { mutableStateOf(row.type) }
                    Common.SingleSelectDropdown(options, row.type) {
                        row.type = options[it].value;rowType = options[it].value
                    }
                    when (rowType.trim()) {
                        "@keyEvent" -> Common.MultiSelectDropdown(
                            modifier = Modifier.fillMaxWidth().height(45.dp)
                                .weight(1f),
                            KeyEventMappings.descriptionToKey.map { Option(it.key, it.value) }, row.value.split("|")
                        ) { row.value = it.joinToString("|") }

                        "@delay" -> Common.IntField(
                            modifier = Modifier.fillMaxWidth().height(45.dp).weight(1f),
                            row.value
                        ) { if (((it.toIntOrNull() != null) && it.toInt() > 0) || it == "") row.value = it }

                        else -> Common.SmallTextField(
                            modifier = Modifier.fillMaxWidth().height(45.dp).weight(1f), row.value
                        ) { row.value = it }
                    }
                    Common.AddIcon(modifier = Modifier) { events.add(QuickKeyEvent("", "")) }
                    Common.DeleteIcon { events.removeAt(index) }
                }
                Divider(color = MaterialTheme.colors.onBackground, thickness = 1.dp)
            }
        }
    }

    data class QuickKeyEvent(var type: String, var value: String, var id: UUID = UUID.randomUUID())

    private fun formatValues(values: List<String>): List<QuickKeyEvent> {
        return values.map {
            val splitIndex = it.indexOf(" ")
            val type = if (splitIndex != -1) it.substring(0, splitIndex) else ""
            val value = if (splitIndex != -1) it.substring(splitIndex + 1) else it
            QuickKeyEvent(type, value)
        }
    }

    private fun buildInputConfig(
        desc: String,
        seq: Int,
        events: List<QuickKeyEvent>,
        id: String = UUID.randomUUID().toString()
    ): InputConfig {
        return InputConfig(
            desc,
            seq,
            events.map { (if (it.type.isNotEmpty()) it.type + " " else "") + it.value },
            id = id
        )
    }

    private fun reloadQuickInputData(item: InputConfig) {
        ChestnutConf.quickInputData.removeIf { it.id == item.id }
        ChestnutConf.quickInputData.add(item)
        ChestnutConf.quickInputData.sortBy { it.seq }
        ChestnutConf.reloadQuickInputData()
    }
}