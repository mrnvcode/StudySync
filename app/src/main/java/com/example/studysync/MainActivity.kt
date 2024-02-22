package com.example.studysync

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.studysync.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        // Check if the user is signed in
        val user = auth.currentUser

        if (user != null) {
            // User is signed in
            binding.textViewStatus.text = "User is signed in"

            // Set up sign-out button
            binding.signOutButton.setOnClickListener {
                signOut()
            }

        } else {
            // User is not signed in
            binding.textViewStatus.text = "User is not signed in"

            // Add links to LoginActivity and RegistrationActivity
            binding.loginLink.setOnClickListener {
                startActivity(Intent(this, LoginActivity::class.java))
            }

            binding.registerLink.setOnClickListener {
                startActivity(Intent(this, RegistrationActivity::class.java))
            }

            // Hide the sign-out button if the user is not signed in
            binding.signOutButton.visibility = android.view.View.GONE
        }
    }

    private fun signOut() {
        auth.signOut()

        // Redirect to MainActivity after signing out
        startActivity(Intent(this, MainActivity::class.java))
        finish() // Optional: Close the current activity to prevent going back to it after signing out
    }
}
