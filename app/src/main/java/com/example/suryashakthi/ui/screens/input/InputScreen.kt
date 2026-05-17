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
import com.example.suryashakthi.ui.theme.SolarAmber
import com.example.suryashakthi.ui.viewmodel.DashboardViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
    var selectedWeather by remember { mutableStateOf("Sunny") }
    
    val weatherOptions = listOf("Sunny", "Cloudy", "Rainy")
    
    // Derived state for validation
    val isSolarValid = solarInput.isEmpty() || solarInput.toFloatOrNull() != null
    val isConsumptionValid = consumptionInput.isEmpty() || consumptionInput.toFloatOrNull() != null
    val isSubmitEnabled = solarInput.isNotBlank() && consumptionInput.isNotBlank() && isSolarValid && isConsumptionValid

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        Text(
            text = "Energy Logging",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Record your metrics to train your AI advisor.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            modifier = Modifier.padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Simulation Section
        Text(
            text = "Quick Presets",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SimulationButton(
                label = "Sunny Day",
                icon = Icons.Default.WbSunny,
                onClick = {
                    val gen = Random.nextDouble(18.0, 26.0).toFloat()
                    val cons = Random.nextDouble(4.0, 8.0).toFloat()
                    viewModel.saveEnergyRecord(gen, cons, "Sunny")
                    Toast.makeText(context, "Logged: Optimal Solar Day", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.weight(1f),
                color = SolarAmber
            )
            SimulationButton(
                label = "Cloudy Day",
                icon = Icons.Default.Cloud,
                onClick = {
                    val gen = Random.nextDouble(3.0, 9.0).toFloat()
                    val cons = Random.nextDouble(9.0, 14.0).toFloat()
                    viewModel.saveEnergyRecord(gen, cons, "Cloudy")
                    Toast.makeText(context, "Logged: Low Solar Day", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.secondary
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Manual Input",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Weather Selection
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            weatherOptions.forEach { weather ->
                WeatherChip(
                    label = weather,
                    isSelected = selectedWeather == weather,
                    onSelect = { selectedWeather = weather },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = solarInput,
            onValueChange = { 
                if (it.isEmpty() || it.toDoubleOrNull() != null || it == ".") {
                    solarInput = it
                }
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Solar Production (kWh)") },
            shape = RoundedCornerShape(20.dp),
            supportingText = {
                if (!isSolarValid) {
                    Text("Enter a numeric value", color = MaterialTheme.colorScheme.error)
                }
            },
            isError = !isSolarValid,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.SolarPower,
                    contentDescription = null,
                    tint = if (isSolarValid) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                errorBorderColor = MaterialTheme.colorScheme.error,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = consumptionInput,
            onValueChange = { 
                if (it.isEmpty() || it.toDoubleOrNull() != null || it == ".") {
                    consumptionInput = it
                }
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Grid Consumption (kWh)") },
            shape = RoundedCornerShape(20.dp),
            supportingText = {
                if (!isConsumptionValid) {
                    Text("Enter a numeric value", color = MaterialTheme.colorScheme.error)
                }
            },
            isError = !isConsumptionValid,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Bolt,
                    contentDescription = null,
                    tint = if (isConsumptionValid) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.error
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                errorBorderColor = MaterialTheme.colorScheme.error,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                val solar = solarInput.toFloatOrNull()
                val consumption = consumptionInput.toFloatOrNull()
                if (solar != null && consumption != null) {
                    viewModel.saveEnergyRecord(solar, consumption, selectedWeather)
                    solarInput = ""
                    consumptionInput = ""
                    Toast.makeText(context, "Data stored securely", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f)
            ),
            enabled = isSubmitEnabled,
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp)
        ) {
            Text(text = "Log Entry", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
        }
        
        Spacer(modifier = Modifier.height(48.dp))
    }
}

@Composable
fun WeatherChip(label: String, isSelected: Boolean, onSelect: () -> Unit, modifier: Modifier) {
    val bgColor = if (isSelected) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.surfaceVariant
    val contentColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
    
    Button(
        onClick = onSelect,
        modifier = modifier.height(40.dp),
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(containerColor = bgColor, contentColor = contentColor),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(text = label, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun SimulationButton(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color, onClick: () -> Unit, modifier: Modifier) {
    val isDark = isSystemInDarkTheme()
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(80.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.onBackground),
        border = androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.onBackground)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = icon, 
                contentDescription = null, 
                tint = if (isDark && color == Color.LightGray) MaterialTheme.colorScheme.onBackground else color, 
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(label, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
        }
    }
}
