package com.ngarvey.chitchat.ui.main

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ngarvey.chitchat.R
import com.ngarvey.chitchat.models.Message
import com.ngarvey.chitchat.repository.MessageFetcher

class MessageListAdapter(val fetcher: MessageFetcher):
    PagedListAdapter<Message, MessageListAdapter.MessageHolder>(messageDiffCallback) {

    companion object {
        const val TAG = "MessageListAdapter"

        val messageDiffCallback= object : DiffUtil.ItemCallback<Message>() {
            override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class MessageHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val contents: TextView = itemView.findViewById(R.id.message_contents)
        private val client: TextView = itemView.findViewById(R.id.message_client)
        private val date: TextView = itemView.findViewById(R.id.message_date)
        private val likeButton: Button = itemView.findViewById(R.id.message_like)
        private val dislikeButton: Button = itemView.findViewById(R.id.message_dislike)
        private val likeTotal: TextView = itemView.findViewById(R.id.message_liked_total)
        private val dislikeTotal: TextView = itemView.findViewById(R.id.message_disliked_total)

        var message: Message? = null

        init {
            likeButton.setOnClickListener {
                if (message != null) {
                    fetcher.likeMessage(message!!.id)
                    likeTotal.text = message?.likes?.plus(1).toString()
                    likeButton.isEnabled = false
                }
            }

            dislikeButton.setOnClickListener {
                if (message != null) {
                    fetcher.dislikeMessage(message!!.id)
                    dislikeTotal.text = message?.dislikes?.plus(1).toString()
                    dislikeButton.isEnabled = false
                }
            }
        }

        fun bind(mes: Message?) {
            //Log.d(TAG, "Binding! Current message is: $mes")
            message = mes
            contents.text       = message?.message
            client.text         = message?.client
            date.text           = message?.date
            likeTotal.text      = message?.likes.toString()
            dislikeTotal.text   = message?.dislikes.toString()

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.message_holder, parent, false)

        return MessageHolder(view)
    }

    override fun onBindViewHolder(holder: MessageHolder, position: Int) {
        holder.bind(getItem(position))
    }
}