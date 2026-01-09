package gz.dam.room


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import gz.dam.room.ui.theme.SimonDiceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicialización de Room
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "simon-dice-db"
        )
            .allowMainThreadQueries() // Solo para este ejercicio
            .build()

        val recordDao = db.recordDao()

        setContent {
            SimonDiceTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    // Pasamos el recordDao al Factory
                    val viewModel: VM = viewModel(
                        factory = VMFactory(application, recordDao)
                    )
                    SimonDiceUI(viewModel = viewModel)
                }
            }
        }
    }
}