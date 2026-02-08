package com.example.veterinaryclinic.data.local

import androidx.room.TypeConverter
import com.example.veterinaryclinic.domain.domainModel.Sex

/**
 * Room-конвертер для [Sex].
 *
 * Room не умеет напрямую хранить enum-ы, поэтому мы сохраняем [Sex] как строку и
 * восстанавливаем его обратно при чтении из БД.
 *
 * Формат хранения: `sex.name` (например, `"MALE"`, `"FEMALE"`, `"UNKNOWN"`).
 */
class PatientSexConverter {
    /** Сериализует [Sex] в строку для хранения в SQLite. */
    @TypeConverter
    fun fromType(type: Sex): String {
        return type.name
    }

    /** Десериализует строку из SQLite обратно в [Sex].
     * [Sex.valueOf] требует точного совпадения имени enum-константы (регистр важен)
     * и выбрасывает исключение, если строка некорректна. Поэтому при изменении названий enum-констант
     * потребуется миграция данных/версии БД.*/
    @TypeConverter
    fun toType(typeName: String): Sex {
        return Sex.valueOf(typeName)
    }
}