package ru.aminovniaz.firstapp.data

import java.util.UUID

data class Task(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val status: Status = Status.ACTIVE
) {

    enum class Status {
        ACTIVE, DISABLED
    }
}
