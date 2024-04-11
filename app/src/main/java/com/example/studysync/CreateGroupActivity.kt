package com.example.studysync

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.studysync.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CreateGroupActivity : AppCompatActivity() {

    private lateinit var groupNameEditText: EditText
    private lateinit var shortDescriptionEditText: EditText
    private lateinit var longDescriptionEditText: EditText
    private lateinit var topicsEditText: EditText
    private lateinit var deadlineEditText: EditText
    private lateinit var createGroupButton: Button

    // Footer icons
    private lateinit var dashboardIcon: ImageButton // Dashboard
    private lateinit var plusIcon: ImageButton // Create Groups
    private lateinit var profileIcon: ImageButton // User Profile

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_group)

        groupNameEditText = findViewById(R.id.groupNameEditText)
        shortDescriptionEditText = findViewById(R.id.shortDescriptionEditText)
        longDescriptionEditText = findViewById(R.id.longDescriptionEditText)
        topicsEditText = findViewById(R.id.topicsEditText)
        deadlineEditText = findViewById(R.id.deadlineEditText)
        createGroupButton = findViewById(R.id.createGroupButton)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

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

        createGroupButton.setOnClickListener {
            createGroup()
        }
    }

    private fun createGroup() {
        val groupName = groupNameEditText.text.toString().trim()
        val shortDescription = shortDescriptionEditText.text.toString().trim()
        val longDescription = longDescriptionEditText.text.toString().trim()
        val topics = topicsEditText.text.toString().trim()
        val deadline = deadlineEditText.text.toString().trim()

        if (TextUtils.isEmpty(groupName) || TextUtils.isEmpty(shortDescription) || TextUtils.isEmpty(
                longDescription
            ) || TextUtils.isEmpty(topics) || TextUtils.isEmpty(deadline)
        ) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = firebaseAuth.currentUser?.uid
        val groupData = hashMapOf(
            "groupName" to groupName,
            "shortDescription" to shortDescription,
            "longDescription" to longDescription,
            "topics" to topics,
            "deadline" to deadline,
            "creatorId" to userId
        )

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
