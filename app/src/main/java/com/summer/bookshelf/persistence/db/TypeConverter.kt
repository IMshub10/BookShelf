package com.summer.bookshelf.persistence.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.summer.bookshelf.utils.extensions.json
import com.summer.bookshelf.utils.extensions.fromJson

class TypeConverter {

    @TypeConverter
    fun stringToType(list: List<String>?): String? {
        return list?.json()
    }

    @TypeConverter
    fun typeToString(value: String?): List<String>? {
        if (value == null) {
            return null
        }
        if (value.isEmpty()) {
            return listOf()
        }
        return Gson().fromJson<ArrayList<String>>(value)
    }

    @TypeConverter
    fun intToType(list: List<Int>?): String? {
        return list?.json()
    }

    @TypeConverter
    fun typeToInt(value: String?): List<Int>? {
        if (value == null) {
            return null
        }
        if (value.isEmpty()) {
            return listOf()
        }
        return Gson().fromJson<ArrayList<Int>>(value)
    }

    @TypeConverter
    fun longToType(list: List<Long>?): String? {
        return list?.json()
    }

    @TypeConverter
    fun typeToLong(value: String?): List<Long>? {
        if (value == null) {
            return null
        }
        if (value.isEmpty()) {
            return listOf()
        }
        return Gson().fromJson<ArrayList<Long>>(value)
    }


}