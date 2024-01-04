package com.example.socketsslserver.core

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

data class MessageData(
    var message: String
)

object ClientMessage {
    var messages by mutableStateOf((MessageData("")))
}