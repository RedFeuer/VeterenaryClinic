package com.example.veterinaryclinic.data.local

import androidx.room.TypeConverter
import com.example.veterinaryclinic.domain.domainModel.PatientType

class PatientTypeConverter {
    @TypeConverter
    fun fromType(type: PatientType): String {
        return type.name
    }

    // возврат Type по названию типа животного
    // valueOf смотрит ТОЧНО соотвествие(регистр влияет)
    // выбрасывает ошибки
    @TypeConverter
    fun toType(typeName: String): PatientType {
        return PatientType.valueOf(typeName)
    }
}