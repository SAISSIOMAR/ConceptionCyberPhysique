package com.example.myapplication.presentation

class BluetoothListenerService  {
    /*
    private val uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB") // Same UUID as in the smartwatch

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val serverThread = Thread(ServerRunnable())
        serverThread.start()
        return START_NOT_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    private inner class ServerRunnable : Runnable {
        override fun run() {
            val bluetoothServerSocket: BluetoothServerSocket? = BluetoothAdapter.getDefaultAdapter()
                ?.listenUsingRfcommWithServiceRecord("MyBluetoothService", uuid)

            var shouldLoop = true
            while (shouldLoop) {
                try {
                    val socket = bluetoothServerSocket?.accept()
                    val inputStream = socket?.inputStream
                    val buffer = ByteArray(1024)
                    val bytes: Int = inputStream?.read(buffer) ?: 0
                    val incomingMessage = String(buffer, 0, bytes)

                    if (incomingMessage == "OPEN_SIGNUP_LINK") {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.yoursignuplink.com"))
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        shouldLoop = false
                    }

                    socket?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                    shouldLoop = false
                }
            }
        }
    }

    // Other necessary service methods...
    :*
     */
}
