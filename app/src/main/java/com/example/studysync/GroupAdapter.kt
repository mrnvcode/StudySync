package com.example.studysync

import Group
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class GroupAdapter(private val groups: List<Group>) : RecyclerView.Adapter<GroupAdapter.GroupViewHolder>() {

    private lateinit var context: Context
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_group, parent, false)
        return GroupViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val group = groups[position]
        holder.groupNameTextView.text = group.name
        holder.groupDescriptionTextView.text = group.description
        holder.majorTopicsTextView.text = group.majorTopics

        // Set OnClickListener for Learn More button
        holder.learnMoreButton.setOnClickListener {
            // Launch GroupDetailsActivity and pass group information
            val intent = Intent(context, GroupDetailsActivity::class.java).apply {
                putExtra("groupName", group.name)
                putExtra("description", group.description)
                putExtra("topics", group.majorTopics)
            }
            context.startActivity(intent)
        }

        // Set OnClickListener for Message button
        holder.messageButton.setOnClickListener {
            // Fetch creator's email address from Firestore
            fetchCreatorEmail(group.name)
        }
    }

    override fun getItemCount(): Int {
        return groups.size
    }

    private fun fetchCreatorEmail(groupName: String) {
        firestore.collection("groups")
            .whereEqualTo("groupName", groupName)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val creatorId = documents.documents[0].getString("creatorId")
                    if (creatorId != null) {
                        fetchCreatorEmailFromUsersCollection(creatorId)
                    } else {
                        Toast.makeText(context, "Creator ID not found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Group not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, "Failed to fetch group details: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun fetchCreatorEmailFromUsersCollection(creatorId: String) {
        firestore.collection("users").document(creatorId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val creatorEmail = documentSnapshot.getString("email")
                if (creatorEmail != null) {
                    // Send email
                    sendEmail(creatorEmail)
                } else {
                    Toast.makeText(context, "Creator email not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, "Failed to fetch creator details: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun sendEmail(recipientEmail: String) {
        val subject = "Dear Group Creator,\n\nI would like to join your group as I find it interesting. Kindly respond back to this email.\n\nSincerely,\nPotential Member"
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(recipientEmail))
            putExtra(Intent.EXTRA_SUBJECT, "Join Group")
            putExtra(Intent.EXTRA_TEXT, subject)
        }
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(Intent.createChooser(intent, "Send Email"))
        } else {
            Toast.makeText(context, "No email app found", Toast.LENGTH_SHORT).show()
        }
    }

    class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val groupNameTextView: TextView = itemView.findViewById(R.id.groupNameTextView)
        val groupDescriptionTextView: TextView = itemView.findViewById(R.id.groupDescriptionTextView)
        val majorTopicsTextView: TextView = itemView.findViewById(R.id.majorTopicsTextView)
        val learnMoreButton: Button = itemView.findViewById(R.id.learnMoreButton)
        val messageButton: Button = itemView.findViewById(R.id.messageButton)
    }
}
