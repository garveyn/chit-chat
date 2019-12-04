package com.ngarvey.chitchat.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.ngarvey.chitchat.models.Message
import com.ngarvey.chitchat.repository.MessageDataSource
import com.ngarvey.chitchat.repository.MessageDataSourceFactory
import com.ngarvey.chitchat.repository.MessageFetcher
import com.ngarvey.chitchat.repository.NetworkState

class MainViewModel : ViewModel() {

    private val config = PagedList.Config.Builder()
        .setPageSize(20)
        .setInitialLoadSizeHint(20*2)
        .setEnablePlaceholders(false)
        .build()
    private val messageDataSourceFactory = MessageDataSourceFactory()
    val messageList = LivePagedListBuilder<Int, Message>(messageDataSourceFactory, config).build()
    val fetcher = MessageFetcher()
    val listIsEmpty: Boolean
        get() = messageList.value?.isEmpty() ?: true

    fun getState() : LiveData<NetworkState> =
        Transformations.switchMap<MessageDataSource,
                NetworkState>(messageDataSourceFactory.messageDataSourceLiveData,
                                MessageDataSource::networkState)

    fun invalidate() =
        messageDataSourceFactory.messageDataSourceLiveData.value?.invalidate()


}
