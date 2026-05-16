package com.example.suryashakthi.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import com.example.suryashakthi.domain.model.EnergyRecord
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Utility to export energy records to a CSV file and share it.
 */
object CsvExporter {
    fun exportAndShare(context: Context, records: List<EnergyRecord>) {
        val fileName = "SuryaShakti_Export_${System.currentTimeMillis()}.csv"
        val file = File(context.cacheDir, fileName)
        
        try {
            val writer = FileWriter(file)
            writer.append("Timestamp,Solar(kWh),Grid(kWh),Weather,CO2_Saved(kg)\n")
            
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            
            records.forEach { record ->
                val dateStr = sdf.format(Date(record.timestamp))
                writer.append("$dateStr,${record.solarProduction},${record.gridConsumption},${record.weatherCondition},${record.carbonReduced}\n")
            }
            writer.flush()
            writer.close()

            shareFile(context, file)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun shareFile(context: Context, file: File) {
        val uri: Uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/csv"
            putExtra(Intent.EXTRA_SUBJECT, "Energy Data Export")
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        
        context.startActivity(Intent.createChooser(intent, "Share CSV"))
    }
}
