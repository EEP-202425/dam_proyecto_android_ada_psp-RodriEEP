package com.example.volantum

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.volantum.ui.navigation.App
import com.example.volantum.ui.theme.VolantumTheme
import com.mikepenz.iconics.Iconics
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Iconics.registerFont(FontAwesome)
        enableEdgeToEdge()
        setContent {
            VolantumTheme {
               App()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    VolantumTheme {
        App()
    }
}
