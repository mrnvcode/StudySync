package com.example.studysync

import Group
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GroupAdapter(private val groups: List<Group>) : RecyclerView.Adapter<GroupAdapter.GroupViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_group, parent, false)
        return GroupViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val group = groups[position]
        holder.groupNameTextView.text = group.name
        holder.groupDescriptionTextView.text = group.description
        holder.majorTopicsTextView.text = group.majorTopics

        // Set OnClickListener for buttons if needed
        holder.learnMoreButton.setOnClickListener {
            // Handle Learn More button click
        }

        holder.pingButton.setOnClickListener {
            // Handle Ping button click
        }

        holder.messageButton.setOnClickListener {
            // Handle Message button click
        }
    }

    override fun getItemCount(): Int {
        return groups.size
    }

    class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val groupNameTextView: TextView = itemView.findViewById(R.id.groupNameTextView)
        val groupDescriptionTextView: TextView = itemView.findViewById(R.id.groupDescriptionTextView)
        val majorTopicsTextView: TextView = itemView.findViewById(R.id.majorTopicsTextView)
        val learnMoreButton: Button = itemView.findViewById(R.id.learnMoreButton)
        val pingButton: Button = itemView.findViewById(R.id.pingButton)
        val messageButton: Button = itemView.findViewById(R.id.messageButton)
    }
}
