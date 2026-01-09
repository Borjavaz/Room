package gz.dam.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

/**
 * Entidad que representa un récord en la base de datos Room.
 */
@Entity(tableName = "record_table")
data class Record(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, // Valor por defecto 0 para que Room lo autogenere
    val ronda: Int,
    val timestamp: Long = System.currentTimeMillis()
) {
    /**
     * Obtiene la fecha formateada del récord.
     */
    fun getFechaFormateada(): String {
        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return formatter.format(Date(timestamp))
    }

    companion object {
        /**
         * Crea un Record vacío.
         * Usamos argumentos con nombre para evitar confusiones con el ID.
         */
        fun empty(): Record = Record(ronda = 0, timestamp = System.currentTimeMillis())
    }
}