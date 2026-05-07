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
            Text("Add more data to view trends", style = MaterialTheme.typography.bodySmall)
        }
        return
    }

    val maxVal = data.maxOf { maxOf(it.solarGenerated, it.consumption) }.coerceAtLeast(1f) * 1.2f

    Canvas(modifier = modifier.fillMaxWidth().height(200.dp)) {
        val width = size.width
        val height = size.height
        val spacing = width / (data.size - 1)

        // Draw Line Paths
        val solarPath = Path()
        val consPath = Path()

        data.forEachIndexed { index, energy ->
            val x = index * spacing
            val ySolar = height - (energy.solarGenerated / maxVal * height)
            val yCons = height - (energy.consumption / maxVal * height)

            if (index == 0) {
                solarPath.moveTo(x, ySolar)
                consPath.moveTo(x, yCons)
            } else {
                solarPath.lineTo(x, ySolar)
                consPath.lineTo(x, yCons)
            }
        }

        drawPath(
            path = solarPath,
            color = SolarOrange,
            style = Stroke(width = 3.dp.toPx())
        )
        drawPath(
            path = consPath,
            color = EnergyBlue,
            style = Stroke(width = 3.dp.toPx())
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
