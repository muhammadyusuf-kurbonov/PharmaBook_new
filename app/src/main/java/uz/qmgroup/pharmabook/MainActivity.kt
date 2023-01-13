package uz.qmgroup.pharmabook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import uz.qmgroup.pharmabook.screens.app.AppScreen
import uz.qmgroup.pharmabook.ui.theme.PharmaBookTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PharmaBookTheme {
                // A surface container using the 'background' color from the theme
                AppScreen(modifier = Modifier.fillMaxSize())
            }
        }
    }
}