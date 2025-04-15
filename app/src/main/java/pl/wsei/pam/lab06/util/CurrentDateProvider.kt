package pl.wsei.pam.lab06.util

import java.time.LocalDate

interface CurrentDateProvider {
    val currentDate: LocalDate
}

