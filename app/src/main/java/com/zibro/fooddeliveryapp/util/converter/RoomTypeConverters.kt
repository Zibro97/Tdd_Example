package com.zibro.fooddeliveryapp.util.converter

import androidx.room.TypeConverter

//Room Entity에서 Pair<Int,Int>와 같은 타입은 읽지 못하기 때문에 변환해주는 유틸 클래스
object RoomTypeConverters {
    @TypeConverter
    @JvmStatic
    fun toString(pair: Pair<Int, Int>): String {
        return "${pair.first},${pair.second}"
    }

    @TypeConverter
    @JvmStatic
    fun toIntPair(str: String): Pair<Int, Int> {
        val splitedStr = str.split(",")
        return Pair(Integer.parseInt(splitedStr[0]), Integer.parseInt(splitedStr[1]))
    }
}