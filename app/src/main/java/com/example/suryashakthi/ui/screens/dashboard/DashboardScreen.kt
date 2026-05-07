package com.example.suryashakthi.ui.screens.dashboard

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.KeyboardDoubleArrowUp
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.suryashakthi.ui.viewmodel.DashboardViewModel

@Composable
fun DashboardScreen(viewModel: DashboardViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()

    // Calculate aggregated metrics from real data
    val totalSolar = state.records.sumOf { it.solarProduction.toDouble() }.toFloat()
    val totalGrid = state.records.sumOf { it.gridConsumption.toDouble() }.toFloat()
    val totalSavings = state.records.sumOf { it.savings.toDouble() }.toFloat()
    
    val avgEcoScore = if (state.records.isNotEmpty()) {
        state.records.map { it.efficiencyScore }.average().toInt()
    } else 0
    val netFlow = totalSolar - totalGrid

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(scrollState)
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        
        Text(
            text = "Surya Shakti",
            fontSize = 32.sp,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Personal Energy Budget",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        // High Contrast Sustainability Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            EcoScoreGauge(score = avgEcoScore)
            Spacer(modifier = Modifier.width(24.dp))
            Column {
                Text(
                    text = "Independence",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = if (state.records.isEmpty()) 
                        "Log data to see impact." 
                        else "Green Energy Score: $avgEcoScore%",
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Savings Report (30 Days)",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.Black),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier.padding(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Total Money Saved", color = Color.Gray, fontSize = 12.sp)
                    Text("₹${"%.0f".format(totalSavings)}", color = Color(0xFFFFD600), fontSize = 32.sp, fontWeight = FontWeight.Black)
                }
                Icon(Icons.Default.ElectricBolt, contentDescription = null, tint = Color(0xFFFFD600), modifier = Modifier.size(40.dp))
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Usage Metrics",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        
        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            MetricCard(
                title = "Generation",
                value = "%.1f".format(totalSolar),
                unit = "kWh",
                icon = Icons.Default.WbSunny,
                color = Color.Black,
                modifier = Modifier.weight(1f)
            )
            MetricCard(
                title = "Consumption",
                value = "%.1f".format(totalGrid),
                unit = "kWh",
                icon = Icons.Default.ElectricBolt,
                color = Color.Black,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        val flowPrefix = if (netFlow >= 0) "+" else ""
        NetEnergyCard(
            value = "$flowPrefix%.1f".format(netFlow),
            isPositive = netFlow >= 0
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun EcoScoreGauge(score: Int) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(120.dp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawArc(
                color = Color(0xFFEEEEEE),
                startAngle = 140f,
                sweepAngle = 260f,
                useCenter = false,
                style = Stroke(width = 14.dp.toPx(), cap = StrokeCap.Round)
            )
            drawArc(
                color = Color(0xFFFFD600), // High contrast yellow
                startAngle = 140f,
                sweepAngle = (260f * (score / 100f)),
                useCenter = false,
                style = Stroke(width = 14.dp.toPx(), cap = StrokeCap.Round)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "$score%", fontSize = 28.sp, fontWeight = FontWeight.Black, color = Color.Black)
            Text(text = "Solar Ratio", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun MetricCard(title: String, value: String, unit: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color, modifier: Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        border = androidx.compose.foundation.BorderStroke(2.dp, Color.Black)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(icon, contentDescription = null, tint = Color.Black, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text(title, fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
            Row(verticalAlignment = Alignment.Bottom) {
                Text(value, fontSize = 24.sp, fontWeight = FontWeight.Black, color = Color.Black)
                Text(" $unit", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(bottom = 4.dp))
            }
        }
    }
}

@Composable
fun NetEnergyCard(value: String, isPositive: Boolean) {
    val bgColor = if (isPositive) Color(0xFF00C853) else Color.Black
    val textColor = if (isPositive) Color.White else Color(0xFFFFD600)
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Net Energy Flow", fontSize = 12.sp, color = if (isPositive) Color.White.copy(alpha = 0.7f) else Color.Gray)
                Text(
                    text = if (isPositive) "$value kWh (Exporting)" else "$value kWh (Importing)",
                    fontSize = 24.sp, 
                    fontWeight = FontWeight.Black, 
                    color = textColor
                )
            }
            Icon(
                imageVector = if (isPositive) Icons.Default.KeyboardDoubleArrowUp else Icons.Default.ElectricBolt,
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(8.dp).padding(2.dp)) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(color)
            }
        }
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = label, fontSize = 10.sp, color = Color.Black)
    }
}
