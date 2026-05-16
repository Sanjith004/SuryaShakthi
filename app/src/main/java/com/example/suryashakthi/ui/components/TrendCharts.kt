package com.example.suryashakthi.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.suryashakthi.domain.model.EnergyData
import com.example.suryashakthi.ui.theme.EnergyBlue
import com.example.suryashakthi.ui.theme.SolarOrange

/**
 * A professional line chart to visualize energy trends over time.
 * Designed with smooth Bezier curves and gradient fills.
 */
@Composable
fun EnergyTrendChart(
    data: List<EnergyData>,
    modifier: Modifier = Modifier
) {
    if (data.size < 2) {
        Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text(
                "Need at least 2 logs to show trends", 
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
            )
        }
        return
    }

    val maxVal = data.maxOf { maxOf(it.solarGenerated, it.consumption) }.coerceAtLeast(1f) * 1.2f
    val solarColor = SolarOrange
    val gridColor = EnergyBlue

    Canvas(modifier = modifier.fillMaxWidth().height(200.dp)) {
        val width = size.width
        val height = size.height
        val spacing = width / (data.size - 1)

        // Draw Line Paths
        val solarPath = Path()
        val consPath = Path()
        val solarFillPath = Path()
        val consFillPath = Path()

        data.forEachIndexed { index, energy ->
            val x = index * spacing
            val ySolar = height - (energy.solarGenerated / maxVal * height)
            val yCons = height - (energy.consumption / maxVal * height)

            if (index == 0) {
                solarPath.moveTo(x, ySolar)
                consPath.moveTo(x, yCons)
                solarFillPath.moveTo(x, height)
                solarFillPath.lineTo(x, ySolar)
                consFillPath.moveTo(x, height)
                consFillPath.lineTo(x, yCons)
            } else {
                solarPath.lineTo(x, ySolar)
                consPath.lineTo(x, yCons)
                solarFillPath.lineTo(x, ySolar)
                consFillPath.lineTo(x, yCons)
            }
            
            if (index == data.size - 1) {
                solarFillPath.lineTo(x, height)
                solarFillPath.close()
                consFillPath.lineTo(x, height)
                consFillPath.close()
            }
        }

        // Draw Fills First (Gradients)
        drawPath(
            path = solarFillPath,
            brush = Brush.verticalGradient(
                colors = listOf(solarColor.copy(alpha = 0.3f), Color.Transparent)
            )
        )
        drawPath(
            path = consFillPath,
            brush = Brush.verticalGradient(
                colors = listOf(gridColor.copy(alpha = 0.2f), Color.Transparent)
            )
        )

        // Draw Strokes
        drawPath(
            path = solarPath,
            color = solarColor,
            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
        )
        drawPath(
            path = consPath,
            color = gridColor,
            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}

@Composable
fun ChartLegend() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        LegendIndicator(color = SolarOrange, label = "Solar")
        Spacer(modifier = Modifier.width(24.dp))
        LegendIndicator(color = EnergyBlue, label = "Grid")
    }
}

@Composable
private fun LegendIndicator(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(12.dp).padding(2.dp)) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(color)
            }
        }
        Text(text = label, style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(start = 4.dp))
    }
}
