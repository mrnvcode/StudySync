package com.example.studysync

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class GroupDetailsActivity : AppCompatActivity() {

    // Firebase Firestore instance
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    // Footer icons
    private lateinit var dashboardIcon: ImageButton
    private lateinit var plusIcon: ImageButton
    private lateinit var profileIcon: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_details)

        // Retrieve group information from intent extras
        val groupName = intent.getStringExtra("groupName")
        val description = intent.getStringExtra("description")
        val topics = intent.getStringExtra("topics")

        // Set group details in TextViews
        findViewById<TextView>(R.id.groupNameTextView).text = "Group Name: $groupName"
        findViewById<TextView>(R.id.topicsTextView).text = "Topics: $topics"

        // Fetch long description from Firestore based on group name
        fetchLongDescription(groupName)

        // Fetch deadline from Firestore based on group name
        fetchDeadline(groupName)

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

        // - User Profile Navigation
        profileIcon.setOnClickListener {
            // Start UserProfileActivity
            startActivity(Intent(this, UserProfileActivity::class.java))
        }
    }

    // Fetch deadline from Firestore based on group name
    private fun fetchDeadline(groupName: String?) {
        if (groupName != null) {
            firestore.collection("groups")
                .whereEqualTo("groupName", groupName)
                .get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        val deadline = documents.documents[0].getString("deadline")
                        if (deadline != null) {
                            // Display deadline in TextView
                            findViewById<TextView>(R.id.deadlineTextView).text = "Deadline: $deadline"
                        } else {
                            // Handle case where deadline is null
                            findViewById<TextView>(R.id.deadlineTextView).text = "Deadline not available"
                        }
                    } else {
                        // Handle case where no documents found
                        findViewById<TextView>(R.id.deadlineTextView).text = "No group details found"
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle failure to fetch deadline
                    findViewById<TextView>(R.id.deadlineTextView).text = "Failed to fetch deadline"
                }
        }
    }

    // Fetch long description from Firestore based on group name
    private fun fetchLongDescription(groupName: String?) {
        if (groupName != null) {
            firestore.collection("groups")
                .whereEqualTo("groupName", groupName)
                .get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        val longDescription = documents.documents[0].getString("longDescription")
                        if (longDescription != null) {
                            // Display long description in TextView
                            findViewById<TextView>(R.id.longDescriptionTextView).text = "Long Description: $longDescription"
                        } else {
                            // Handle case where long description is null
                            findViewById<TextView>(R.id.longDescriptionTextView).text = "Long Description not available"
                        }
                    } else {
                        // Handle case where no documents found
                        findViewById<TextView>(R.id.longDescriptionTextView).text = "No group details found"
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle failure to fetch long description
                    findViewById<TextView>(R.id.longDescriptionTextView).text = "Failed to fetch long description"
                }
        }
    }
}
