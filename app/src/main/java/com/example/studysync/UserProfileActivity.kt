package com.example.studysync

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class UserProfileActivity : AppCompatActivity() {

    // Footer icons
    private lateinit var dashboardIcon: ImageButton // Dashboard
    private lateinit var plusIcon: ImageButton // Create Groups
    private lateinit var profileIcon: ImageButton // User Profile

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        // Footer icons navigation initialization
        dashboardIcon = findViewById(R.id.dashboardIcon)
        plusIcon = findViewById(R.id.plusIcon)
        profileIcon = findViewById(R.id.profileIcon)

        // Footer icons navigation click listeners
        // - Dashboard Navigation
        dashboardIcon.setOnClickListener {
            // Start DashboardActivity
            startActivity(Intent(this, DashboardActivity::class.java))
        }

        // - Create Group Navigation
        plusIcon.setOnClickListener {
            // Start CreateGroupActivity
            startActivity(Intent(this, CreateGroupActivity::class.java))
        }

        // - Create Group Navigation
        profileIcon.setOnClickListener {
            // Start CreateGroupActivity
            startActivity(Intent(this, UserProfileActivity::class.java))
        }

        // Find views
        val profileInfoButton: Button = findViewById(R.id.profileInfoButton)
        val manageGroupsButton: Button = findViewById(R.id.manageGroupsButton)
        val signOutButton: Button = findViewById(R.id.signOutButton)

        // Set click listeners
        profileInfoButton.setOnClickListener {
            startActivity(Intent(this, ProfileInformationActivity::class.java))
        }

        manageGroupsButton.setOnClickListener {
            // Open Manage Groups Activity
            // Replace ManageGroupsActivity::class.java with the actual name of your Manage Groups Activity
            //val intent = Intent(this, ManageGroupsActivity::class.java)
            //startActivity(intent)
        }

        signOutButton.setOnClickListener {
            // Sign out from Firebase Authentication
            FirebaseAuth.getInstance().signOut()

            // Navigate back to the main activity (or login activity)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Optional: finish the current activity to prevent returning to it on back press
        }
    }
}