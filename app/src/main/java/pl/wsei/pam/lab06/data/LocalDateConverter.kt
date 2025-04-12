import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class LocalDateConverter {
    @TypeConverter
    fun fromDateTime(date: LocalDate): String {
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    }

    @TypeConverter
    fun fromDateTime(str: String): LocalDate {
        return LocalDate.parse(str, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    }
}
