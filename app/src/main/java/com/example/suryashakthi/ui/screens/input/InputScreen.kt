package com.example.suryashakthi.ui.screens.input

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.SolarPower
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.suryashakthi.ui.viewmodel.DashboardViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.ui.Alignment
import kotlin.random.Random

@Composable
fun InputScreen(viewModel: DashboardViewModel = hiltViewModel()) {
    var solarInput by remember { mutableStateOf("") }
    var consumptionInput by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 48.dp)
    ) {
        Text(
            text = "Data Entry",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Text(
            text = "Manual log or auto-simulate for today.",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Simulation Section (Requested Feature)
        Text(text = "Quick Simulation", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SimulationButton(
                label = "Sunny Day",
                icon = Icons.Default.WbSunny,
                onClick = {
                    val gen = Random.nextDouble(15.0, 25.0).toFloat()
                    val cons = Random.nextDouble(5.0, 10.0).toFloat()
                    viewModel.saveEnergyRecord(gen, cons)
                    Toast.makeText(context, "Simulated Sunny Day: ${"%.1f".format(gen)}kWh", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.weight(1f),
                color = Color(0xFFFFD600)
            )
            SimulationButton(
                label = "Cloudy Day",
                icon = Icons.Default.Cloud,
                onClick = {
                    val gen = Random.nextDouble(2.0, 8.0).toFloat()
                    val cons = Random.nextDouble(8.0, 12.0).toFloat()
                    viewModel.saveEnergyRecord(gen, cons)
                    Toast.makeText(context, "Simulated Cloudy Day: ${"%.1f".format(gen)}kWh", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.weight(1f),
                color = Color.LightGray
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
        HorizontalDivider(color = Color.LightGray)
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = solarInput,
            onValueChange = { solarInput = it },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            placeholder = { Text("Manual Solar Production (kWh)", color = Color.Gray) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.SolarPower,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = Color.DarkGray
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.LightGray
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = consumptionInput,
            onValueChange = { consumptionInput = it },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            placeholder = { Text("Manual Grid Consumption (kWh)", color = Color.Gray) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Bolt,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = Color.DarkGray
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.LightGray
            )
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = {
                val solar = solarInput.toFloatOrNull()
                val consumption = consumptionInput.toFloatOrNull()
                if (solar != null && consumption != null) {
                    viewModel.saveEnergyRecord(solar, consumption)
                    solarInput = ""
                    consumptionInput = ""
                    Toast.makeText(context, "Entry logged successfully!", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color(0xFFFFD600)
            ),
            enabled = solarInput.isNotBlank() && consumptionInput.isNotBlank()
        ) {
            Text(text = "Log Entry Manually", fontSize = 16.sp, fontWeight = FontWeight.Black)
        }
    }
}

@Composable
fun SimulationButton(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color, onClick: () -> Unit, modifier: Modifier) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(80.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black),
        border = androidx.compose.foundation.BorderStroke(2.dp, Color.Black)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(label, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}
