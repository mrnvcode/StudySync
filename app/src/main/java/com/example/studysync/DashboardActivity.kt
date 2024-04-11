package com.example.studysync

import Group
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class DashboardActivity : AppCompatActivity() {

    private lateinit var groupRecyclerView: RecyclerView
    private lateinit var groupAdapter: GroupAdapter
    private lateinit var noGroupTextView: TextView

    // Footer icons
    private lateinit var dashboardIcon: ImageButton // Dashboard
    private lateinit var plusIcon: ImageButton // Create Groups
    private lateinit var profileIcon: ImageButton // User Profile

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var groupList: MutableList<Group> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Initialize views
        groupRecyclerView = findViewById(R.id.groupRecyclerView)
        noGroupTextView = findViewById(R.id.noGroupTextView)

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

        // Initialize RecyclerView and adapter
        groupAdapter = GroupAdapter(groupList)
        groupRecyclerView.adapter = groupAdapter
        groupRecyclerView.layoutManager = LinearLayoutManager(this)
        groupRecyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )

        // Fetch group data from Firestore
        fetchGroupData()
    }

    private fun fetchGroupData() {
        firestore.collection("groups")
            .get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) {
                    // If no groups available, show message
                    noGroupTextView.text = getString(R.string.no_groups_message)
                } else {
                    // If groups available, hide message and show RecyclerView
                    noGroupTextView.text = ""
                    result.forEach { document ->
                        val groupName = document.getString("groupName") ?: ""
                        val groupDescription = document.getString("shortDescription") ?: ""
                        val groupTopics = document.getString("topics") ?: ""
                        val group = Group(groupName, groupDescription, groupTopics)
                        groupList.add(group)
                    }
                    // Notify the adapter about the data change
                    groupAdapter.notifyDataSetChanged()
                }
            }
            .addOnFailureListener { exception ->
                // Handle failure to fetch data
                noGroupTextView.text = getString(R.string.fetch_data_error)
            }
    }
}
