package com.example.suryashakthi.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [EnergyEntity::class], version = 1, exportSchema = false)
abstract class EnergyDatabase : RoomDatabase() {
    abstract fun dao(): EnergyDao
}
