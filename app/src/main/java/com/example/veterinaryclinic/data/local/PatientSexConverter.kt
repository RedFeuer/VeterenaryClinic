package com.example.veterinaryclinic.data.local

import androidx.room.TypeConverter
import com.example.veterinaryclinic.domain.domainModel.Sex

class PatientSexConverter {
    @TypeConverter
    fun fromType(type: Sex): String {
        return type.name
    }

    // возврат Type по названию типа животного
    // valueOf смотрит ТОЧНО соотвествие(регистр влияет)
    // выбрасывает ошибки
    @TypeConverter
    fun toType(typeName: String): Sex {
        return Sex.valueOf(typeName)
    }
}