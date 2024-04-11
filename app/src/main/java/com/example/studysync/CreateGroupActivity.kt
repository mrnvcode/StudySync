package com.example.studysync

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CreateGroupActivity : AppCompatActivity() {

    // UI elements
    private lateinit var groupNameEditText: EditText
    private lateinit var shortDescriptionEditText: EditText
    private lateinit var longDescriptionEditText: EditText
    private lateinit var topicsEditText: EditText
    private lateinit var deadlineEditText: EditText
    private lateinit var createGroupButton: Button

    // Footer icons
    private lateinit var dashboardIcon: ImageButton
    private lateinit var plusIcon: ImageButton
    private lateinit var profileIcon: ImageButton

    // Firebase
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_group)

        // Initialize UI elements
        groupNameEditText = findViewById(R.id.groupNameEditText)
        shortDescriptionEditText = findViewById(R.id.shortDescriptionEditText)
        longDescriptionEditText = findViewById(R.id.longDescriptionEditText)
        topicsEditText = findViewById(R.id.topicsEditText)
        deadlineEditText = findViewById(R.id.deadlineEditText)
        createGroupButton = findViewById(R.id.createGroupButton)

        // Initialize Firebase
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Initialize footer icons
        dashboardIcon = findViewById(R.id.dashboardIcon)
        plusIcon = findViewById(R.id.plusIcon)
        profileIcon = findViewById(R.id.profileIcon)

        // Set click listeners for footer icons
        dashboardIcon.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
        }

        plusIcon.setOnClickListener {
            startActivity(Intent(this, CreateGroupActivity::class.java))
        }

        profileIcon.setOnClickListener {
            startActivity(Intent(this, UserProfileActivity::class.java))
        }

        // Set click listener for create group button
        createGroupButton.setOnClickListener {
            createGroup()
        }
    }

    // Function to create a new group
    private fun createGroup() {
        // Get input values from EditText fields
        val groupName = groupNameEditText.text.toString().trim()
        val shortDescription = shortDescriptionEditText.text.toString().trim()
        val longDescription = longDescriptionEditText.text.toString().trim()
        val topics = topicsEditText.text.toString().trim()
        val deadline = deadlineEditText.text.toString().trim()

        // Check if any field is empty
        if (TextUtils.isEmpty(groupName) || TextUtils.isEmpty(shortDescription) || TextUtils.isEmpty(longDescription)
            || TextUtils.isEmpty(topics) || TextUtils.isEmpty(deadline)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Get current user's ID
        val userId = firebaseAuth.currentUser?.uid

        // Create a map of group data
        val groupData = hashMapOf(
            "groupName" to groupName,
            "shortDescription" to shortDescription,
            "longDescription" to longDescription,
            "topics" to topics,
            "deadline" to deadline,
            "creatorId" to userId
        )

        // Add group data to Firestore
        firestore.collection("groups")
            .add(groupData)
            .addOnSuccessListener {
                Toast.makeText(this, "Group created successfully", Toast.LENGTH_SHORT).show()
                // Redirect to dashboard activity
                // You can implement this part based on your navigation setup
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error creating group: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
