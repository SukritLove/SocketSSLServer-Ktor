package com.example.socketsslserver

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.socketsslserver.core.ClientMessage
import com.example.socketsslserver.core.PlatformNetworkingUtils
import com.example.socketsslserver.core.startServer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun AppScreen(networkUtils: PlatformNetworkingUtils) {
    var deviceIpAddress by remember { mutableStateOf<String?>("") }
    val message = ClientMessage.messages.message

    LaunchedEffect(Unit) {
        deviceIpAddress = networkUtils.getCurrentWifiIpAddress()
    }

    LaunchedEffect(deviceIpAddress) {
        launch {
            withContext(Dispatchers.IO) {
                startServer(deviceIpAddress.orEmpty())
            }
        }
    }
    Column(
        Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        deviceIpAddress?.let { Text("Server is listening on $it :: 4001") }

        Spacer(modifier = Modifier.padding(10.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .border(5.dp, Color.Black)
        ) {
            Text(
                text = message,
                style = TextStyle(Color.Black),
                modifier = Modifier.padding(10.dp)
            )
        }

    }
}