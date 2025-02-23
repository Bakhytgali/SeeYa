import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun DashedBorderBox(
    modifier: Modifier = Modifier,
    borderColor: Color,
    borderWidth: Float = 5f,
    dashWidth: Float = 15f,
    gapWidth: Float = 10f,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier.drawWithContent {
            drawContent()
            drawRoundRect(
                color = borderColor,
                style = Stroke(
                    width = borderWidth,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(dashWidth, gapWidth), 0f)
                )
            )
        }
    ) {
        content()
    }
}
