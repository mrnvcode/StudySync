package com.example.studysync

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class LoginSupportActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loginsupport)

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance()

        // Initialize the "Go Back to Main Activity" button
        val btnGoBack = findViewById<Button>(R.id.btnGoBack)

        // Set click listener for the "Go Back to Main Activity" button
        btnGoBack.setOnClickListener {
            // Navigate back to the main activity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // close the current activity
        }

        // Set click listener for the "Submit" button
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        btnSubmit.setOnClickListener {
            // Call method to submit the issue to Firestore
            submitIssue()
        }
    }

    private fun submitIssue() {
        // Get issue description and contact information from EditText fields
        val issueDescription = findViewById<EditText>(R.id.editIssue).text.toString()
        val contactInfo = findViewById<EditText>(R.id.editContact).text.toString()

        // Create a new document in the "issues" collection in Firestore
        val issuesCollectionRef = firestore.collection("issues")
        val newIssueDocRef = issuesCollectionRef.document()

        // Create a map with the issue data
        val issueData = hashMapOf(
            "description" to issueDescription,
            "contact" to contactInfo
        )

        // Set the issue data in the Firestore document
        newIssueDocRef.set(issueData)
            .addOnSuccessListener {
                // Display a message to the user indicating successful submission
                Toast.makeText(this, "Issue submitted successfully!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                // Display an error message if submission fails
                Toast.makeText(this, "Failed to submit issue: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
