package ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.lt.compose_views.text_field.BackgroundComposeWithTextField
import com.lt.compose_views.text_field.GoodTextField
import com.lt.compose_views.text_field.HintComposeWithTextField
import entity.Option

object Common {
    @Composable
    fun ListIcon(onclick: () -> Unit) {
        IconButton(onClick = onclick) {
            Icon(painterResource("icons/list.svg"), modifier = Modifier.size(20.dp), contentDescription = null)
        }
    }

    @Composable
    fun DeleteIcon(onclick: () -> Unit) {
        IconButton(onClick = onclick) {
            Icon(painterResource("icons/delete.svg"), modifier = Modifier.size(20.dp), contentDescription = null)
        }
    }
    @Composable
    fun AddIcon(modifier: Modifier = Modifier,onclick: () -> Unit) {
        IconButton(onClick = onclick,modifier = modifier) {
            Icon(painterResource("icons/add.svg"), modifier = Modifier.size(20.dp), contentDescription = null)
        }
    }
    @Composable
    fun EditIcon(onclick: () -> Unit) {
        IconButton(onClick = onclick) {
            Icon(painterResource("icons/write.svg"), modifier = Modifier.size(20.dp), contentDescription = null)
        }
    }

    @Composable
    fun <T> SingleSelectDropdown(options: List<Option<T>>,value :T? = null, onSelected: (Int) -> Unit) {
        var expanded by remember { mutableStateOf(false) }
        var selectedIndex by remember { mutableStateOf(if (value != null) options.indexOfFirst { it.value == value } else 0) }
        Box(modifier = Modifier.wrapContentSize(Alignment.TopStart)) {
            TextButton(onClick = { expanded = !expanded }) {
                Text(options[selectedIndex].label, modifier = Modifier.width(120.dp))
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Expand dropdown"
                )
            }

            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                options.forEachIndexed { index, option ->
                    DropdownMenuItem(onClick = {
                        selectedIndex = index
                        onSelected(index)
                        expanded = false
                    }) {
                        Text(option.label)
                    }
                }
            }
        }
    }
    @Composable
    fun <T> MultiSelectDropdown(modifier: Modifier = Modifier,options: List<Option<T>>,value :List<T>? = null, onSelected: (List<T>) -> Unit) {
        var expanded by remember { mutableStateOf(false) }
        val optionValueMap = options.mapIndexed { index, it -> it.value to index }.toMap()
        val values = (value ?: emptyList()).mapNotNull { optionValueMap[it] }
        val selectedOptionIndex: MutableList<Int> = remember { mutableStateListOf(*(values).toTypedArray()) }
        Box(modifier = modifier.fillMaxWidth().wrapContentSize(Alignment.TopStart)) {
            TextButton(onClick = { expanded = !expanded }) {
                Text(modifier = Modifier.fillMaxWidth().weight(1f), text = selectedOptionIndex.joinToString { options[it].label })
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Expand dropdown"
                )
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                options.forEach { option ->
                    DropdownMenuItem(onClick = {
                        val index = optionValueMap[option.value]!!
                        if (index in selectedOptionIndex) {
                            selectedOptionIndex.remove(index)
                        } else {
                            selectedOptionIndex.add(index)
                        }
                        onSelected(selectedOptionIndex.map { options[it].value })
                    }) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(checked = optionValueMap[option.value] in selectedOptionIndex, onCheckedChange = null)
                            Spacer(Modifier.width(8.dp))
                            Text(option.label)
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun SmallTextField(modifier: Modifier,fromValue:String?, onValueChange: (String) -> Unit){
        var value by remember { mutableStateOf(fromValue?:"") }
        GoodTextField(
            fontColor = MaterialTheme.colors.onBackground,
            modifier = Modifier.height(45.dp).padding(top = 5.dp).then(modifier),
            value = value,
            hint = HintComposeWithTextField.createTextHintCompose("..."),
            background = BackgroundComposeWithTextField.createBackgroundCompose(
                RoundedCornerShape(1.dp), Color.Transparent
            ),
            onValueChange = { value = it; onValueChange(it) }
        )
    }

    @Composable
    fun IntField(modifier: Modifier,fromValue:String?, onValueChange: (String) -> Unit){
        var value by remember { mutableStateOf(fromValue?:"") }
        GoodTextField(
            fontColor = MaterialTheme.colors.onBackground,
            modifier = Modifier.height(45.dp).padding(top = 5.dp).then(modifier),
            value = value,
            hint = HintComposeWithTextField.createTextHintCompose("1000"),
            background = BackgroundComposeWithTextField.createBackgroundCompose(
                RoundedCornerShape(1.dp), Color.Transparent
            ),
            onValueChange = { ite ->
                if (ite.toIntOrNull()?.let { it > 0 }?: ( ite == "")) {
                    value = ite
                    onValueChange(ite)
                }
            }
        )
    }
}