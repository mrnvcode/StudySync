package com.example.studysync

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class ProfileInformationActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    private lateinit var nameEditText: EditText
    private lateinit var phoneNumberEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var errorMessageTextView: TextView
    private lateinit var dashboardIcon: ImageButton
    private lateinit var plusIcon: ImageButton
    private lateinit var profileIcon: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_information)

        // Initialize Firebase Authentication
        auth = FirebaseAuth.getInstance()

        // Find views
        nameEditText = findViewById(R.id.nameEditText)
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText)
        emailEditText = findViewById(R.id.emailEditText)
        saveButton = findViewById(R.id.saveButton)
        errorMessageTextView = findViewById(R.id.errorMessageTextView)
        dashboardIcon = findViewById(R.id.dashboardIcon)
        plusIcon = findViewById(R.id.plusIcon)
        profileIcon = findViewById(R.id.profileIcon)

        // Footer icons navigation click listeners
        dashboardIcon.setOnClickListener {
            // Start DashboardActivity
            startActivity(Intent(this, DashboardActivity::class.java))
        }

        plusIcon.setOnClickListener {
            // Start CreateGroupActivity
            startActivity(Intent(this, CreateGroupActivity::class.java))
        }

        profileIcon.setOnClickListener {
            // Start User Profile Activity
            startActivity(Intent(this, UserProfileActivity::class.java))
        }

        // Handle save button click
        saveButton.setOnClickListener {
            // Get user input
            val name = nameEditText.text.toString().trim()
            val phoneNumber = phoneNumberEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()

            // Validate user input
            if (name.isEmpty() || phoneNumber.isEmpty() || email.isEmpty()) {
                // Show error message if any field is empty
                errorMessageTextView.text = "Please fill in all fields"
            } else {
                // Save user information
                saveUserData(name, phoneNumber, email)
            }
        }

        // Load user data if available
        loadUserData()
    }

    private fun loadUserData() {
        // Get current user's ID
        val userId = auth.currentUser?.uid

        // Check if the user is authenticated
        if (userId != null) {
            // Get reference to the user's document in Firestore
            val userRef = FirebaseFirestore.getInstance().collection("users").document(userId)

            // Retrieve user data
            userRef.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        // Document exists, populate EditText fields with user data
                        val userData = document.data
                        nameEditText.setText(userData?.get("name").toString())
                        phoneNumberEditText.setText(userData?.get("phoneNumber").toString())
                        emailEditText.setText(userData?.get("email").toString())
                    } else {
                        // Document doesn't exist
                        errorMessageTextView.text = "User data not found"
                    }
                }
                .addOnFailureListener { e ->
                    // Failed to fetch user data
                    errorMessageTextView.text = "Failed to fetch user data: ${e.message}"
                }
        } else {
            // User is not authenticated, show error message
            errorMessageTextView.text = "User not authenticated"
        }
    }


    private fun saveUserData(name: String, phoneNumber: String, email: String) {
        // Get current user's ID
        val userId = auth.currentUser?.uid

        // Check if the user is authenticated
        if (userId != null) {
            // Get reference to the user's document in Firestore
            val userRef = FirebaseFirestore.getInstance().collection("users").document(userId)

            // Create a map to store user data
            val userData = hashMapOf(
                "name" to name,
                "phoneNumber" to phoneNumber,
                "email" to email
            )

            // Set user data in Firestore
            userRef.set(userData)
                .addOnSuccessListener {
                    // Data saved successfully
                    // Finish the activity
                    finish()
                }
                .addOnFailureListener { e ->
                    // Failed to save data
                    // Show error message
                    errorMessageTextView.text = "Failed to save data: ${e.message}"
                }
        } else {
            // User is not authenticated, show error message
            errorMessageTextView.text = "User not authenticated"
        }
    }
}
