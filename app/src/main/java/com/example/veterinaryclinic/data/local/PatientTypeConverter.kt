package com.example.veterinaryclinic.data.local

import androidx.room.TypeConverter
import com.example.veterinaryclinic.domain.domainModel.PatientType

/**
 * Room-конвертер для [PatientType].
 *
 * Room не умеет напрямую хранить enum-ы, поэтому мы сохраняем [PatientType] как строку и
 * восстанавливаем его обратно при чтении из БД.
 *
 * Формат хранения: `type.name` (например, `"CAT"`, `"DOG"`).
 */
class PatientTypeConverter {
    /** Сериализует [PatientType] в строку для хранения в SQLite. */
    @TypeConverter
    fun fromType(type: PatientType): String {
        return type.name
    }


    /** Десериализует строку из SQLite обратно в [PatientType].
     * Важно: [PatientType.valueOf] требует точного совпадения имени enum-константы (регистр важен)
     * и выбрасывает исключение, если строка некорректна. Поэтому при изменении названий enum-констант
     * потребуется миграция БД.*/
    @TypeConverter
    fun toType(typeName: String): PatientType {
        return PatientType.valueOf(typeName)
    }
}