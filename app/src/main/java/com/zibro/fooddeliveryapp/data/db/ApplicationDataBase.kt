package com.zibro.fooddeliveryapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zibro.fooddeliveryapp.data.db.dao.LocationDao
import com.zibro.fooddeliveryapp.data.entity.LocationLatLngEntity

@Database(
    entities = [LocationLatLngEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ApplicationDataBase : RoomDatabase() {
    companion object{
        const val DB_NAME = "ApplicationDatabase.db"
    }

    abstract fun locationDao() : LocationDao
}