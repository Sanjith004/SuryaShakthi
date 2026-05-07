package com.example.suryashakthi.utils

import android.content.Context
import android.os.Environment
import com.example.suryashakthi.data.local.EnergyDataEntity
import java.io.File
import java.io.FileWriter

object CsvExporter {
    fun exportToCsv(context: Context, data: List<EnergyDataEntity>): String? {
        val fileName = "energy_data_export.csv"
        val folder = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        if (folder == null || !folder.exists()) {
            folder?.mkdirs()
        }
        
        val file = File(folder, fileName)
        return try {
            val writer = FileWriter(file)
            writer.append("Date,SolarGenerated(kWh),Consumption(kWh),AIInsight\n")
            data.forEach {
                writer.append("${it.date},${it.solarGenerated},${it.consumption},\"${it.aiInsight ?: ""}\"\n")
            }
            writer.flush()
            writer.close()
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
