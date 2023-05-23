import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import action.Window.windowMoveAct
import conf.ChestnutConf
import entity.InputConfig
import kotlinx.coroutines.launch
import ui.Chestnut
import ui.Common
import ui.WindowIcon
import util.AwtUtils
import java.lang.Integer.max


fun main() = application {
    val state by remember {
        mutableStateOf(
            WindowState(
                width = 320.dp,
                height = 600.dp,
                placement = WindowPlacement.Floating,
                position = WindowPosition(Alignment.Center)
            )
        )
    }
    val scaffoldState = rememberScaffoldState()
    val content:MutableList<InputConfig> = remember { mutableStateListOf(*ChestnutConf.quickInputData.toTypedArray()) }
    var quickInputOnTop by remember {
        mutableStateOf(true)
    }
    Window(
        transparent = true,
        focusable = false,
        alwaysOnTop = quickInputOnTop,
        state = state,
        icon = painterResource("chestnut-alpha.svg"),
        undecorated = true,
        title = "chestnut",
        onCloseRequest = ::exitApplication
    ) {
        MaterialTheme(
            colors = darkColors()
        ) {
            Scaffold(
                modifier = Modifier.border(1.dp, MaterialTheme.colors.background, RoundedCornerShape(4.dp)).clip(RoundedCornerShape(6.dp)),
                scaffoldState = scaffoldState,
                topBar = {
                    TopAppBar(
                        modifier = Modifier.height(40.dp).windowMoveAct(state),
                        title = {},
                        navigationIcon = {
                            WindowIcon.WindowAboutButton(quickInputOnTop) { quickInputOnTop = !quickInputOnTop }
                        },
                        actions = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                var addDialogState by remember { mutableStateOf(false) }
                                var listDialogState by remember { mutableStateOf(false) }
                                Common.AddIcon { addDialogState = true }
                                Common.ListIcon { listDialogState = true }
                                if (addDialogState) {
                                    Chestnut.EditDialog(onSubmitRequest = {content.add(it);content.sortedBy { ite->ite.seq }}) { addDialogState = !addDialogState }
                                }
                                if (listDialogState) {
                                    Chestnut.ListDialog(content) { listDialogState = !listDialogState }
                                }
                                WindowIcon.WindowMinButton(state)
                                WindowIcon.WindowCloseButton(::exitApplication)
                            }
                        }
                    )
                }
            ) { innerPadding ->
                var contentSize by remember { mutableStateOf(IntSize.Zero) }
                Surface(
                    elevation = 2.dp,
                    modifier = Modifier.padding(innerPadding).fillMaxWidth().fillMaxHeight()
                        .onSizeChanged { contentSize = it }
                ) {
                    QuickInputContent(content,contentSize)
                }
            }
        }
    }
}

@Composable
fun QuickInputButton(desc: String, values: List<String>) {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    Button(
        modifier = Modifier.padding(end = 5.dp).width(100.dp),
        elevation = ButtonDefaults.elevation(5.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.surface),
        onClick = { try {
            AwtUtils.writeToFocus(values)
        } catch (e: Exception) {
            scope.launch {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = e.message ?: "error",
                    actionLabel = "error"
                )
            }
            e.printStackTrace()
        } }) {
        Text(text = desc)
    }
}

@Composable
fun QuickInputContent(content:MutableList<InputConfig>, contentSize: IntSize) {
    val rowSize = max(contentSize.width / 100, 1)
    val arrs = content.sortedBy { it.seq }.chunked(rowSize)
    LazyColumn {
        items(arrs.size) {
            LazyRow(modifier = Modifier.padding(start = 5.dp, top = 0.dp, end = 5.dp, bottom = 0.dp)) {
                itemsIndexed(arrs[it]) { _, item ->
                    QuickInputButton(item.desc, item.values)
                }
            }
        }
    }

}