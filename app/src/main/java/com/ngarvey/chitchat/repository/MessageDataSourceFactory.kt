package com.ngarvey.chitchat.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.ngarvey.chitchat.models.Message

class MessageDataSourceFactory : DataSource.Factory<Int, Message>() {

    val messageDataSourceLiveData = MutableLiveData<MessageDataSource>()
    var latestSource: MessageDataSource? = null

    override fun create(): DataSource<Int, Message> {
        latestSource = MessageDataSource()
        messageDataSourceLiveData.postValue(latestSource)
        return latestSource!!
    }
}