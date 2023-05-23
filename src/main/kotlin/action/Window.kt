package action

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChanged
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState


/**
 * 窗口动作
 */
object Window {
    /**
     * 窗口移动
     */
    fun Modifier.windowMoveAct(state: WindowState) = this.then(
        this.pointerInput(Unit) {
            var offset = Offset.Zero
            detectDragGestures(
                onDragStart = {
                    offset = it
                },
                onDrag = { change, _ ->
                    if (change.positionChanged()) change.consume()
                    state.position = WindowPosition(
                        state.position.x + (change.position.x - offset.x).dp,
                        state.position.y + (change.position.y - offset.y).dp
                    )
                }
            )
        }
    )
}