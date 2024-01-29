package com.example.myapplication.presentation


import android.app.AlertDialog
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
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
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import okio.IOException
import org.json.JSONObject


class MainActivity : ComponentActivity() {
    private var receivedMessage by mutableStateOf("")
    private val channelId = "WebSocketChannel"
    private val notificationId = 1
    private var webSocket: WebSocket? = null // Make webSocket nullable


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showNotification("AirGuard", "hello")





        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()
                NavHost(navController, startDestination = "main") {
                    composable("main") {
                        MainScreen(navController)
                    }
                    composable("login") {
                        LoginScreen(navController)
                    }
                    composable("websocket_content") {
                        WebSocketContent(navController)
                    }
                    composable("air_quality/{quality}") { backStackEntry ->
                        AirQualityScreen(
                            quality = backStackEntry.arguments?.getString("quality") ?: "0",
                            navController = navController
                        )
                    }
                }
            }
        }



    }


    private fun initializeWebSocket(replacementDigits: String,username: String) {
        System.out.println("righhhhhhhht")
        val baseUrl = "ws://10.212.160."
        val portAndEndpoint = ":1880/ws/watch/"
        val finalUrl = baseUrl + replacementDigits + portAndEndpoint
        webSocket?.close(1000, "App closed") // Use appropriate close code and reason

        val client = OkHttpClient()
        val request = Request.Builder().url(finalUrl).build()
        val listener = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                MainScope().launch {
                    receivedMessage = "WebSocket opened successfully"

                    val jsonObject = JSONObject()
                    jsonObject.put("username", username)
                    jsonObject.put("type", "login")
                    val jsonMessage = jsonObject.toString()
                    webSocket.send(jsonMessage)
                    System.out.println("heeeeeeeere")
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
                    val jsonObject = JSONObject()
                    jsonObject.put("username", username)
                    jsonObject.put("type", "logout")
                    val jsonMessage = jsonObject.toString()
                    webSocket.send(jsonMessage)
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


    override fun onDestroy() {
        super.onDestroy()
        // Close the WebSocket connection when the activity is destroyed
        val jsonObject = JSONObject()
        jsonObject.put("username", "omar")
        jsonObject.put("type", "logout")
        val jsonMessage = jsonObject.toString()
        webSocket?.send(jsonMessage)
        webSocket?.close(1000, "App destroyed") // Use appropriate close code and reason
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun LoginScreen(navController: NavController) {
        // State variables for username and password
        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        val context = LocalContext.current

        // Use a scrollable Column in case the content does not fit on the screen
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()) // Add scrolling capability
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Username TextField
            TextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Password TextField
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(), // Obscures the password input
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Confirm Button
            Button(onClick = {
                val json = JSONObject()
                json.put("username", username)
                json.put("password", password)

                sendCreateAccountRequest(json, context)
                navController.navigate("air_quality/20")
            }) {
                Text("Confirm")
            }
            Spacer(modifier = Modifier.height(16.dp))
            // Navigate to WebSocket Screen Button
            Button(onClick = {
                navController.navigate("websocket_content")
            }) {
                Text("debug")
            }
        }
    }

    @Composable
    fun MainScreen(navController: NavController) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    // Navigate to Login Screen
                    navController.navigate("login")
                }
            ) {
                Text("Login")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    // Currently does nothing for SignUp
                }
            ) {
                Text("SignUp")
            }
        }
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

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
    @Composable
    fun WebSocketContent(navController: NavController) {
        var text by remember { mutableStateOf(TextFieldValue()) }
        val keyboardController = LocalSoftwareKeyboardController.current

        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // TextField for IP segment
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Enter new IP segment") },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                    modifier = Modifier.widthIn(max = 200.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                // Send Button
                Button(
                    onClick = {
                        val replacementDigits = text.text
                        initializeWebSocket(replacementDigits, "omar")
                        // text = TextFieldValue() // Comment out or remove this line to keep the text in the input field
                    },
                    enabled = text.text.isNotBlank(),
                    modifier = Modifier.size(50.dp)
                ) {
                    Icon(imageVector = Icons.Default.Send, contentDescription = "Send")
                }
            }
            // Return to Login Button
            Button(
                onClick = {
                    navController.popBackStack() // Use popBackStack to return to the previous screen
                },
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text("Back")
            }
        }
    }



    private fun sendCreateAccountRequest(json: JSONObject, context: Context) {
        val client = OkHttpClient()
        val requestBody = json.toString().toRequestBody("application/json; charset=utf-8".toMediaType())

        val request = Request.Builder()
            .url("http://10.0.2.2:1880/sign-in") // Use the correct IP for local testing
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle failure
                e.printStackTrace()
                MainScope().launch {
                    showPopUp("Request failed: ${e.message}", context)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                // Handle success
                MainScope().launch {
                    if (response.isSuccessful) {
                        // If needed, update UI based on response. Must be done on the main thread.
                        showPopUp("login successful", context)
                    } else {
                        // Status code 400, request failed
                        showPopUp("login failed", context)
                    }
                }
            }
        })
    }

    private fun showPopUp(message: String, context: Context) {
        // Since we are now on the main thread, we can create and show the AlertDialog
        AlertDialog.Builder(context).apply {
            setMessage(message)
            setCancelable(false)
            setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            show()
        }
    }



    @Composable
    fun AirQualityScreen(quality: String, navController: NavController) {
        val airQuality = quality.toIntOrNull() ?: 0
        val color = when {
            airQuality < 50 -> Color.Green
            airQuality < 100 -> Color.Yellow
            airQuality < 150 -> Color.Cyan
            else -> Color.Red
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Existing components for displaying air quality
            Canvas(modifier = Modifier.size(200.dp)) {
                drawCircle(color = color)
            }
            Text(
                text = "Air Quality: $quality",
                color = Color.White,
                modifier = Modifier.align(Alignment.Center)
            )

            // Logout Button
            Button(
                onClick = {
                    navController.navigate("main")
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            ) {
                Text("Logout")
            }
        }
    }











}