package com.example.studysync

import Group
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.app.AppCompatActivity
import com.example.studysync.GroupAdapter
import com.example.studysync.R

class DashboardActivity : AppCompatActivity() {

    private lateinit var groupRecyclerView: RecyclerView
    private lateinit var groupAdapter: GroupAdapter
    private var groupList: MutableList<Group> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Initialize RecyclerView and adapter
        groupRecyclerView = findViewById(R.id.groupRecyclerView)
        groupAdapter = GroupAdapter(groupList)
        groupRecyclerView.adapter = groupAdapter
        groupRecyclerView.layoutManager = LinearLayoutManager(this)
        groupRecyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        // Add some sample group data (replace with actual data from Firestore)
        val sampleGroup1 = Group("Group 1", "Description 1", "Topic 1, Topic 2, Topic 3")
        val sampleGroup2 = Group("Group 2", "Description 2", "Topic 4, Topic 5, Topic 6")
        groupList.add(sampleGroup1)
        groupList.add(sampleGroup2)

        // Notify the adapter about the data change
        groupAdapter.notifyDataSetChanged()
    }
}
