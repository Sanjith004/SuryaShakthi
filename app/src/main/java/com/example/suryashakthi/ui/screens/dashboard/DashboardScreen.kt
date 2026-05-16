package com.example.suryashakthi.ui.screens.dashboard

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.KeyboardDoubleArrowUp
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.suryashakthi.domain.model.EnergyData
import com.example.suryashakthi.ui.components.ChartLegend
import com.example.suryashakthi.ui.components.EnergyTrendChart
import com.example.suryashakthi.ui.theme.EcoGreen
import com.example.suryashakthi.ui.theme.SolarOrange
import com.example.suryashakthi.ui.viewmodel.DashboardViewModel
import com.example.suryashakthi.utils.CsvExporter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DashboardScreen(viewModel: DashboardViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    
    var recordToDelete by remember { mutableStateOf<com.example.suryashakthi.domain.model.EnergyRecord?>(null) }

    if (recordToDelete != null) {
        AlertDialog(
            onDismissRequest = { recordToDelete = null },
            title = { Text("Delete Record") },
            text = { Text("Are you sure you want to delete this energy log? This cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteEnergyRecord(recordToDelete!!)
                    recordToDelete = null
                }) {
                    Text("Delete", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { recordToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Calculate aggregated metrics from real data
    val totalSolar = state.records.sumOf { it.solarProduction.toDouble() }.toFloat()
    val totalGrid = state.records.sumOf { it.gridConsumption.toDouble() }.toFloat()
    val totalSavings = state.records.sumOf { it.savings.toDouble() }.toFloat()
    val totalCo2 = state.records.sumOf { it.carbonReduced.toDouble() }.toFloat()
    
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
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Branded Modern Icon
            Surface(
                modifier = Modifier.size(56.dp),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.onBackground
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.ElectricBolt,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
            
            Column {
                Text(
                    text = "Surya Shakti",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-1).sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Sustainable Energy Intelligence",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            }
        }

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
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = if (state.records.isEmpty()) 
                        "Log data to see impact." 
                        else "Green Energy Score: $avgEcoScore%",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Savings Report (30 Days)",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onBackground),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier.padding(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Total Money Saved", color = MaterialTheme.colorScheme.background, fontSize = 12.sp)
                    Text("₹${"%.0f".format(totalSavings)}", color = MaterialTheme.colorScheme.primary, fontSize = 32.sp, fontWeight = FontWeight.Black)
                }
                Icon(Icons.Default.ElectricBolt, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(40.dp))
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Usage Metrics",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        
        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            MetricCard(
                title = "Generation",
                value = "%.1f".format(totalSolar),
                unit = "kWh",
                icon = Icons.Default.WbSunny,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f)
            )
            MetricCard(
                title = "Consumption",
                value = "%.1f".format(totalGrid),
                unit = "kWh",
                icon = Icons.Default.ElectricBolt,
                color = MaterialTheme.colorScheme.onBackground,
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

        // New Feature: Environmental Impact
        Text(
            text = "Environmental Impact",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(16.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = if (isSystemInDarkTheme()) Color(0xFF1B5E20) else Color(0xFFE8F5E9)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier.padding(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("CO₂ Offset (Estimated)", color = if (isSystemInDarkTheme()) Color.White.copy(alpha = 0.7f) else Color(0xFF2E7D32), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("%.1f kg".format(totalCo2), color = if (isSystemInDarkTheme()) Color.White else Color(0xFF1B5E20), fontSize = 32.sp, fontWeight = FontWeight.Black)
                }
                Text("🌳", fontSize = 40.sp)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // New Feature: Interactive Trend Chart
        Text(
            text = "Energy Trends",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(16.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                EnergyTrendChart(
                    data = state.records.takeLast(7).map { 
                        EnergyData(0, "", it.solarProduction, it.gridConsumption)
                    },
                    modifier = Modifier.height(150.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                ChartLegend()
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // New Feature: Recent Activity Log
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Recent Activity",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            IconButton(onClick = { CsvExporter.exportAndShare(context, state.records) }) {
                Icon(Icons.Default.Share, contentDescription = "Export Data", tint = MaterialTheme.colorScheme.onBackground)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        
        if (state.records.isEmpty()) {
            Text(
                "No records yet. Start by adding data!", 
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f), 
                fontSize = 14.sp
            )
        } else {
            state.records.reversed().take(5).forEach { record ->
                ActivityItem(record, onLongClick = { recordToDelete = record })
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        Spacer(modifier = Modifier.height(48.dp))
    }
}

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun ActivityItem(record: com.example.suryashakthi.domain.model.EnergyRecord, onLongClick: () -> Unit) {
    val sdf = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
    val dateString = sdf.format(Date(record.timestamp))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {},
                onLongClick = onLongClick
            ),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (record.solarProduction > record.gridConsumption) Icons.Default.WbSunny else Icons.Default.ElectricBolt,
                contentDescription = null,
                tint = if (record.solarProduction > record.gridConsumption) SolarOrange else MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = dateString, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f))
                Text(
                    text = "${if (record.isExportingToGrid) "Exported" else "Imported"} Energy",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(text = "%.1f kWh".format(record.solarProduction), color = SolarOrange, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(text = "%.1f kWh".format(record.gridConsumption), color = MaterialTheme.colorScheme.onSurface, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun EcoScoreGauge(score: Int) {
    val trackColor = MaterialTheme.colorScheme.surfaceVariant
    val progressColor = MaterialTheme.colorScheme.primary
    val textColor = MaterialTheme.colorScheme.onBackground
    
    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(120.dp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawArc(
                color = trackColor,
                startAngle = 140f,
                sweepAngle = 260f,
                useCenter = false,
                style = Stroke(width = 14.dp.toPx(), cap = StrokeCap.Round)
            )
            drawArc(
                color = progressColor, 
                startAngle = 140f,
                sweepAngle = (260f * (score / 100f)),
                useCenter = false,
                style = Stroke(width = 14.dp.toPx(), cap = StrokeCap.Round)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "$score%", fontSize = 28.sp, fontWeight = FontWeight.Black, color = textColor)
            Text(text = "Solar Ratio", fontSize = 10.sp, color = textColor.copy(alpha = 0.6f), fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun MetricCard(title: String, value: String, unit: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color, modifier: Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.onBackground)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onBackground, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text(title, fontSize = 12.sp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f), fontWeight = FontWeight.Bold)
            Row(verticalAlignment = Alignment.Bottom) {
                Text(value, fontSize = 24.sp, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onBackground)
                Text(" $unit", fontSize = 12.sp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f), modifier = Modifier.padding(bottom = 4.dp))
            }
        }
    }
}

@Composable
fun NetEnergyCard(value: String, isPositive: Boolean) {
    val bgColor = if (isPositive) EcoGreen else MaterialTheme.colorScheme.onBackground
    val textColor = if (isPositive) Color.White else MaterialTheme.colorScheme.primary
    
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
                Text("Net Energy Flow", fontSize = 12.sp, color = textColor.copy(alpha = 0.7f))
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
        Text(text = label, fontSize = 10.sp, color = MaterialTheme.colorScheme.onBackground)
    }
}
