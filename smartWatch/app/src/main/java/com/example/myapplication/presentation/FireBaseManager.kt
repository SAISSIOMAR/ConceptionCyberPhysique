package com.example.myapplication.presentation

import com.google.firebase.database.FirebaseDatabase

// Replace "your-firebase-database-url" with your Firebase Realtime Database URL
private const val DATABASE_URL = "https://airguard-31380-default-rtdb.europe-west1.firebasedatabase.app/com.example.myapplication"

data class AllergyData(val atmotubeId: String, val allergies: List<String>)

class FirebaseManager {
    private val database = FirebaseDatabase.getInstance(DATABASE_URL).reference

    fun saveAllergyData(allergyData: AllergyData) {

        // Generate a unique key for each entry
        val key = database.child("allergyData").push().key
        key?.let {
            // Save the data under the generated key
            database.child("allergyData").child(it).setValue(allergyData)
        }
    }
    fun writeHelloWorld() {
        database.child("messages").setValue("Hello, World!")
    }
}


// Example usage:
fun main() {
    val firebaseManager = FirebaseManager()


    // Example AllergyData
    val allergyData = AllergyData(
        atmotubeId = "your_atmotube_id",
        allergies = listOf("Pollen", "Dust", "Pet Dander")
    )

    // Save data to Firebase
    firebaseManager.saveAllergyData(allergyData)
    firebaseManager.writeHelloWorld()

    // Add a delay to ensure the write operation completes before exiting (for testing purposes)
    Thread.sleep(5000)
}
