package com.example.myapplication.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import okhttp3.*
import okio.ByteString

class WebSocketViewModel : ViewModel() {

    private val socketUrl = "wss://your.websocket.url" // Replace with your WebSocket URL
    private val webSocketClient = OkHttpClient.Builder().build()
    private val messageChannel = Channel<String>()

    val receivedMessage: Channel<String> get() = messageChannel

    init {
        connectWebSocket()
    }

    private fun connectWebSocket() {
        val request = Request.Builder().url(socketUrl).build()

        webSocketClient.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                // Handle onOpen event if needed
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                viewModelScope.launch(Dispatchers.Main) {
                    messageChannel.send(text)
                }
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                // Handle binary message if needed
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                // Handle onClosing event if needed
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                // Handle onClosed event if needed
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                // Handle onFailure event if needed
            }
        })
    }

    override fun onCleared() {
        super.onCleared()
        webSocketClient.dispatcher.executorService.shutdown()
        webSocketClient.connectionPool.evictAll()
    }

    fun sendMessage(message: String) {
        webSocketClient.newWebSocket(Request.Builder().url(socketUrl).build(), object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                webSocket.send(message)
            }
        })
    }
}
