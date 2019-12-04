package com.ngarvey.chitchat.ui.main

import android.content.DialogInterface
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ngarvey.chitchat.R
import com.ngarvey.chitchat.repository.NetworkState

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var messageRecyclerView: RecyclerView
    private lateinit var errorTextView: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: MessageListAdapter
    private lateinit var newMessageButton: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.main_fragment, container, false)
        messageRecyclerView = view.findViewById(R.id.messages_recyclerView)
        errorTextView = view.findViewById(R.id.error_textView)
        progressBar = view.findViewById(R.id.progressBar)
        swipeRefreshLayout = view.findViewById(R.id.swipe_to_refresh)
        newMessageButton = view.findViewById(R.id.floatingActionButton)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        adapter = MessageListAdapter(viewModel.fetcher)
        messageRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        messageRecyclerView.adapter = adapter

        viewModel.messageList.observe(this, Observer {
            adapter.submitList(it)
        })

        viewModel.getState().observe(this, Observer { state ->
            progressBar.visibility = if (viewModel.listIsEmpty && state == NetworkState.LOADING)
                View.VISIBLE else View.GONE

            errorTextView.visibility = if (viewModel.listIsEmpty && state == NetworkState.ERROR)
                View.VISIBLE else View.GONE
        })

        swipeRefreshLayout.setOnRefreshListener {
            viewModel.invalidate()
            swipeRefreshLayout.isRefreshing = false
        }

        newMessageButton.setOnClickListener {
            /**
             * some code sourced from:
             *      https://stackoverflow.com/a/7161140/12369045
             */
            val messageDialog = AlertDialog.Builder(requireContext())
            val editText = EditText(messageDialog.context)
            messageDialog.setView(editText)

            messageDialog.setPositiveButton("Send", DialogInterface.OnClickListener { dialog, _ ->
                viewModel.fetcher.sendMessage(editText.text.toString(), null, null)
            })
        }
    }

}
