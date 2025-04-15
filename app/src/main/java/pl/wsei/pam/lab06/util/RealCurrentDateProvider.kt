package pl.wsei.pam.lab06.util

import java.time.LocalDate

class RealCurrentDateProvider : CurrentDateProvider {
    override val currentDate: LocalDate
        get() = LocalDate.now()
}
