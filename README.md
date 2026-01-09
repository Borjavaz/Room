# 📚 Documentación: Implementación de Room Database en Simon Dice

## 🎯 Descripción General
Este proyecto implementa una base de datos local usando **Room Persistence Library** para almacenar los récords del juego **"Simón Dice"**. A continuación se explican las clases principales que componen la arquitectura Room.

---

## 📁 1. Record.kt - La Entidad (Entity)

### Propósito
Representa la tabla en la base de datos donde se almacenan los récords.

### CODIGO
```kotlin
@Entity(tableName = "record_table")
data class Record(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val ronda: Int,
    val timestamp: Long = System.currentTimeMillis()
)
```

### Anotaciones Importantes

- ```@Entity:``` Indica que esta clase es una tabla en la base de datos
- ```tableName:``` Nombre personalizado de la tabla (opcional)
- ```@PrimaryKey:``` Define la clave primaria
- ```autoGenerate = true:``` Room genera automáticamente el ID


## 📁 2. RecordDao.kt – El Objeto de Acceso a Datos (DAO)

### Propósito
Define las operaciones **CRUD** (*Create, Read, Update, Delete*) que se pueden realizar sobre la tabla `record_table`.

---

### CODIGO

```kotlin
@Dao
interface RecordDao {

    // Operaciones de consulta (Query)
    @Query("SELECT * FROM record_table ORDER BY ronda DESC LIMIT 1")
    suspend fun getHighScore(): Record?

    // Operaciones de inserción (Insert)
    @Insert
    suspend fun insert(record: Record)

    // Operaciones de eliminación (Delete)
    @Query("DELETE FROM record_table")
    suspend fun deleteAll()
}
```
## Consulta

```kotlin
SELECT * FROM record_table ORDER BY ronda DESC LIMIT 1
```

---

## 📁 3. AppDatabase.kt – La Base de Datos

###  Propósito
Clase abstracta que sirve como contenedor principal de la base de datos y provee acceso a los **DAOs**.


### CODIGO

```kotlin
@Database(
    entities = [Record::class],  // Tablas incluidas
    version = 1,                 // Versión del esquema
    exportSchema = false         // No exportar esquema (simplificación)
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recordDao(): RecordDao
}
```
### 🏷️ Anotaciones

- ```@Database:``` Marca la clase como base de datos Room
- ```entities:``` Lista de todas las entidades (tablas)
- ```version:``` Número de versión (incrementar al modificar el esquema)
- ```exportSchema:``` Controla si exportar el esquema para migraciones


### 🔒 Patrón Singleton

La instancia de ```AppDatabase``` debe ser única en toda la aplicación.
Se crea en ```MainActivity```.

---

## 🔄 Flujo de Datos Completo

### 1️⃣ Inicialización (`MainActivity.kt`)

```kotlin
// 1. Crear la base de datos
val db = Room.databaseBuilder(...).build()

// 2. Obtener el DAO
val recordDao = db.recordDao()

// 3. Pasar el DAO al ViewModel
val viewModel: VM = viewModel(
    factory = VMFactory(application, recordDao)
)
```

## 2️⃣ Operaciones en el ViewModel (`VM.kt`)

###  Cargar récord al iniciar

```kotlin
private fun cargarRecord() {
    viewModelScope.launch {
        val recordDB = recordDao.getHighScore()
        _record.value = recordDB ?: Record.empty()
    }
}
```

### Guardar nuevo récord

```kotlin
private fun secuenciaCorrecta() {
    if (_ronda.value > _record.value.ronda) {
        val nuevoRecord = Record(ronda = _ronda.value)
        _record.value = nuevoRecord
        recordDao.insert(nuevoRecord)  // ¡Persistencia!
    }
}
```








