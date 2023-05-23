package ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object WindowIcon {
    @Composable
    fun WindowMinButton(state: WindowState) {
        val scope = rememberCoroutineScope()
        IconButton(onClick = {
            val originHeight = state.size.height
            state.placement = WindowPlacement.Floating
            scope.launch {
                for (i in 1..10) {
                    state.size = DpSize(state.size.width, height = originHeight * (10 - i) / 10)
                    delay(20)
                }
                state.isMinimized = true
                state.size = DpSize(state.size.width, height = originHeight)
            }
        }) {
            Icon(painterResource("icons/windowMin.svg"), modifier = Modifier.size(20.dp), contentDescription = null)
        }
    }

    @Composable
    fun WindowCloseButton(exit: () -> Unit) {
        IconButton(onClick = exit) {
            Icon(painterResource("icons/windowClose.svg"), modifier = Modifier.size(20.dp), contentDescription = null)
        }
    }


    @Composable
    @Preview
    fun ChestnutDescTip(version: String = "1.0.0") {
        Surface(
            shape = RoundedCornerShape(4.dp),
            elevation = 4.dp,
            modifier = Modifier.padding(8.dp).shadow(1.dp, RoundedCornerShape(1.dp))
        ) {
            Column {
                Text("chestnut", modifier = Modifier.padding(8.dp))
                Text("version：$version", modifier = Modifier.padding(8.dp), style = MaterialTheme.typography.caption)
            }
        }
    }
    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun WindowAboutButton(isOnTop:Boolean ,onclick: () -> Unit) {
        TooltipArea(
            tooltip = {
                ChestnutDescTip()
            }
        ) {
            Surface(
                shape = RoundedCornerShape(5.dp),
                color = if (isOnTop) Color.Cyan else MaterialTheme.colors.background,
            ) {
                IconButton(
                    modifier = Modifier.size(28.dp),
                    onClick = onclick) {
                    Image(
                        painter = painterResource("chestnut-alpha.svg"),
                        contentDescription = "chestnut",
                        modifier = Modifier.clip(CircleShape)
                    )
                }
            }
        }
    }
    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    @Preview
    fun WindowMenuTip() {
        TooltipArea(
            tooltip = {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    elevation = 4.dp,
                    modifier = Modifier.padding(8.dp)
                        .shadow(1.dp, RoundedCornerShape(1.dp)).width(200.dp)
                ) {
                    Column {
                        Text(
                            "我还没想好这个按钮做什么",
                            modifier = Modifier.padding(8.dp),
                            style = MaterialTheme.typography.caption
                        )
                    }
                }
            }
        ) {
            IconButton(
                modifier = Modifier.size(20.dp),
                onClick = { /* doSomething() */ }) {
                Icon(Icons.Default.Menu, contentDescription = "Menu")
            }
        }
    }
}
