package app.grapheneos.camera.ui.composable.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupPositionProvider
import app.grapheneos.camera.ktx.toPx
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickTooltip(
    message: String,
    tooltipAnchorVerticalSpacing: Float = 16.dp.toPx(),
    content: @Composable () -> Unit,
) {

    TooltipBox (
        positionProvider = remember {
            object : PopupPositionProvider {
                override fun calculatePosition(
                    anchorBounds: IntRect,
                    windowSize: IntSize,
                    layoutDirection: LayoutDirection,
                    popupContentSize: IntSize
                ): IntOffset {

                    val anchorPopWidthDiff = anchorBounds.width - popupContentSize.width

                    val xOffset = if (anchorPopWidthDiff >= 0) {
                        anchorBounds.left + anchorPopWidthDiff / 2
                    } else if (
                        (windowSize.width - anchorBounds.right >= anchorPopWidthDiff.absoluteValue / 2)
                    ) {
                        anchorBounds.left - anchorPopWidthDiff.absoluteValue / 2
                    } else {
                        anchorBounds.right - popupContentSize.width
                    }.toInt()

                    // Keep the tooltip below the view
                    // (for use our case)
                    //
                    // Refer the source code of default implementation
                    // if top based vertical offset is needed
                    val yOffset = (anchorBounds.bottom + tooltipAnchorVerticalSpacing).toInt()

                    return IntOffset(xOffset, yOffset)
                }
            }
        },
        tooltip = {
            PlainTooltip {
                Text(
                    message,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(
                            vertical = 4.dp,
                            horizontal = 2.dp,
                        )
                )
            }

        },
        state = rememberTooltipState(),
        content = content
    )
}