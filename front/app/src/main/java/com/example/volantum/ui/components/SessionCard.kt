package com.example.volantum.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.volantum.data.model.Car
import com.example.volantum.data.model.DrivingSession
import com.example.volantum.ui.theme.VolantumTheme
import com.example.volantum.utils.formatDateTime
import java.time.LocalDateTime

@Composable
fun SessionCard(
    session: DrivingSession,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth().clickable {
            navController.navigate("sessions/${session.id}")
        },
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        text = session?.description.orEmpty(),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = session?.startTime
                            ?.let { formatDateTime(LocalDateTime.parse(it)) }
                            .orEmpty(),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Text(
                    text = "${session?.score ?: 0} / 5 ⭐",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier
                        .background(
                            color = when {
                                (session.score ?: 0f) >= 4f ->
                                    MaterialTheme.colorScheme.secondary
                                else ->
                                    MaterialTheme.colorScheme.tertiary
                            },
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Metric(title = "Duración", value = session?.duration ?: "00:00:00")
                Metric(title = "Distancia", value = "${session?.distance ?: 0.0} km")
                Metric(title = "Velocidad media", value = "${session?.averageSpeed ?: 0.0} km/h")
            }
        }
    }
}

@Composable
private fun Metric(title: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            title,
            style = MaterialTheme.typography.labelMedium,
            color = Color.Gray
        )
        Text(
            value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SessionCardPreview() {
    VolantumTheme {
        SessionCard(
            session = DrivingSession(
                id = 1,
                description = "Trayecto al trabajo",
                startTime = LocalDateTime.now().minusHours(2).toString(),
                endTime = LocalDateTime.now().toString(),
                duration = "00:45:12",
                distance = 23.4f,
                averageSpeed = 31.2f,
                score = 5f,
                car = Car(
                    id = 4,
                    plate = "343XRF",
                    brand = "Ferrari",
                    model = "f340",
                    yearModel = 2005,
                    image = "",
                    mileage = 0f
                )
            ),
            modifier = Modifier.padding(16.dp),
            navController = TODO()
        )
    }
} 