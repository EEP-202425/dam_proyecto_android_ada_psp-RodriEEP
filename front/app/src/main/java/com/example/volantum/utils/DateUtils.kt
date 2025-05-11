// DateUtils.kt
package com.example.volantum.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun formatDateTime(dateTime: LocalDateTime): String {
    val today = LocalDate.now()
    val yesterday = today.minusDays(1)
    val datePart = dateTime.toLocalDate()

    val timeFormatter = DateTimeFormatter.ofPattern("h:mm a", Locale.getDefault())
    val fullDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yy")

    return when (datePart) {
        today -> "Hoy, ${dateTime.format(timeFormatter)}"
        yesterday -> "Ayer, ${dateTime.format(timeFormatter)}"
        else -> dateTime.format(fullDateFormatter)
    }
}
