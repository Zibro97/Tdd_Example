package com.zibro.fooddeliveryapp.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zibro.fooddeliveryapp.data.entity.LocationLatLngEntity

@Dao
interface LocationDao {

    //위치 전체 가져오는 쿼리
    @Query("SELECT * FROM LocationLatLngEntity WHERE id=:id")
    suspend fun get(id: Long): LocationLatLngEntity?

    //위치 추가
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(locationLatLngEntity: LocationLatLngEntity)

    @Query("DELETE FROM LocationLatLngEntity WHERE id=:id")
    suspend fun delete(id: Long)
}