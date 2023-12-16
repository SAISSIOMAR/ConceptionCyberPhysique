package com.example.myapplication.presentation


import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import com.example.myapplication.presentation.theme.MyApplicationTheme
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString




class MainActivity : ComponentActivity() {
    private lateinit var webSocket: WebSocket
    private var receivedMessage by mutableStateOf("")
    private val channelId = "WebSocketChannel"
    private val notificationId = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()
                NavHost(navController, startDestination = "websocket_content") {
                    composable("websocket_content") {
                        WebSocketContent(navController)
                    }
                    composable("second_screen") { backStackEntry ->
                        val message = backStackEntry.arguments?.getString("message") ?: ""
                        SecondScreen(message)
                    }
                }
            }
        }



        initializeWebSocket()
    }


    private fun initializeWebSocket() {
        val client = OkHttpClient()
        val request = Request.Builder().url("ws://192.168.194.24:1880/ws/watch/").build()
        val listener = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                MainScope().launch {
                    receivedMessage = "WebSocket opened successfully"
                    System.out.println()
                }
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                MainScope().launch {
                    receivedMessage = text
                    showNotification("New Message", text)
                    System.out.println("text  : "+text)
                }
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                MainScope().launch {
                    receivedMessage = "Received binary message"
                }
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                MainScope().launch {
                    receivedMessage = "WebSocket closing: $reason"
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                MainScope().launch {
                    receivedMessage = "WebSocket failure: ${t.message}"
                }
            }
        }

        webSocket = client.newWebSocket(request, listener)
        client.dispatcher.executorService.shutdown()
    }

    private fun showNotification(title: String, content: String) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        System.out.println("ssssssss")

        // Create a notification channel for Android Oreo and higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "WebSocket Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = "WebSocket Notifications"
            channel.enableLights(true)
            channel.enableVibration(true)
            notificationManager.createNotificationChannel(channel)
        }

        // Create a notification builder
        val builder = Notification.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setAutoCancel(true)

        // Show the notification
        notificationManager.notify(notificationId, builder.build())
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun WebSocketContent(navController: NavController) {
        var messageToSend by remember { mutableStateOf("") }
        val context = LocalContext.current

        // WebSocket content
        Column(
            verticalArrangement = Arrangement.Center, // Align content in the center vertically
            horizontalAlignment = Alignment.CenterHorizontally // Align content in the center horizontally
        ) {
            // Input text field
            var isSendButtonEnabled by remember { mutableStateOf(false) }
            var text by remember { mutableStateOf(TextFieldValue()) }

            TextField(
                value = text,
                onValueChange = {
                    text = it
                    isSendButtonEnabled = it.text.isNotEmpty()
                },
                label = { Text("Atmotube ID") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (isSendButtonEnabled) {
                            // Send the message when "Done" is pressed
                            text = TextFieldValue()
                            isSendButtonEnabled = false
                        }
                    }
                ),
                modifier = Modifier
                    .padding(16.dp)
                    .width(200.dp) // Adjust the width as needed
            )

            // Send button
            Button(
                onClick = {
                    // Handle the button click action here
                    text = TextFieldValue()
                    isSendButtonEnabled = false
                    navController.navigate("second_screen")
                },
                enabled = isSendButtonEnabled,
                modifier = Modifier
                    .padding(10.dp)
                    .size(50.dp) // Adjust the size as needed
            ) {
                Icon(imageVector = Icons.Default.Send, contentDescription = null)
            }
        }
    }

    @Composable
    fun SecondScreen(message: String) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Display the received message

            // Display the question
            Spacer(modifier = Modifier.height(16.dp)) // Add some spacing
            Text(text = "Who won the World Cup?")

            // Display answer options with checkboxes
            AnswerOption("Brazil")
            AnswerOption("Germany")
            AnswerOption("Italy")
            AnswerOption("Argentina")
        }
    }

    @Composable
    fun AnswerOption(answer: String) {
        var isChecked by remember { mutableStateOf(false) }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = { isChecked = it }
            )
            Spacer(modifier = Modifier.width(8.dp)) // Add spacing between checkbox and text
            Text(text = answer)
        }
    }

}