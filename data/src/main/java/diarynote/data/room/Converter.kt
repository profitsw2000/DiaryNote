package diarynote.data.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class Converter {
    @TypeConverter
    fun fromTags(tags: List<String>): String {
        val gson = Gson()
        return gson.toJson(tags)
    }

    @TypeConverter
    fun fromTagString(tag: String): List<String> {
        val tagsList: Type = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(tag, tagsList)
    }
}