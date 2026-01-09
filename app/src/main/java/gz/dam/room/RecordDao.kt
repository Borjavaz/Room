package gz.dam.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordDao {
    @Query("SELECT * FROM record_table ORDER BY ronda DESC LIMIT 1")
    suspend fun getHighScore(): Record?

    @Insert
    suspend fun insert(record: Record)

    @Query("DELETE FROM record_table")
    suspend fun deleteAll()

    // OPCIÓN MEJORADA: Usar Flow para observación reactiva
    @Query("SELECT * FROM record_table ORDER BY ronda DESC LIMIT 1")
    fun getHighScoreFlow(): Flow<Record?>
}