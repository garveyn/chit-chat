package com.ngarvey.chitchat.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PositionalDataSource
import com.google.gson.GsonBuilder
import com.ngarvey.chitchat.api.ChitChatAPI
import com.ngarvey.chitchat.api.ChitChatResponse
import com.ngarvey.chitchat.api.MessageResponse
import com.ngarvey.chitchat.models.Message
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Implementation guided by:
 * https://medium.com/@antobeslie25/android-recyclerview-pagination-with-paging-library-positionaldatasource-using-retrofit-mvvm-de811ef56b07
 * https://github.com/android/architecture-components-samples/blob/master/PagingWithNetworkSample/
 */
class MessageDataSource : PositionalDataSource<Message>() {

    // Constants
    companion object {
        const val LOAD_COUNT = 20
        const val PAGE_SIZE = 20
        const val KEY = "d51bad9f-24ec-4000-aaf2-e897bb829690"
        const val CLIENT = "nicholas.garvey@mymail.champlain.edu"
        const val TAG = "MessageDataSource"
    }

    // Member Variables
    val networkState = MutableLiveData<NetworkState>()
    private val chatAPI: ChitChatAPI

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.stepoutnyc.com/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()

        chatAPI = retrofit.create(ChitChatAPI::class.java)
    }

    override fun loadInitial(
        params: LoadInitialParams,
        callback: LoadInitialCallback<Message>
    ) {
        val position = computeInitialLoadPosition(params, LOAD_COUNT)
        val loadSize = computeInitialLoadSize(params, position, PAGE_SIZE)

        val result = mutableListOf<Message>()
        val chatRequest: Call<MessageResponse> = chatAPI.getMessages(KEY, CLIENT, position, loadSize)

        networkState.postValue(NetworkState.LOADING)
        Log.d(TAG, "loadInitial startPosition: $position \n" +
                "                           loadSize: $loadSize")

        chatRequest.enqueue(object : Callback<MessageResponse> {

            override fun onResponse(call: Call<MessageResponse>, response: Response<MessageResponse>) {
                try {
                    result.addAll(response.body()?.messages!!)
                    networkState.postValue(NetworkState.DONE)
                    Log.d(TAG, "loadInitial Response body: ${response.body()}")
                    callback.onResult(result, position)
                } catch (e: Exception) {
                    Log.d(TAG, "Messages not found!", e)
                    networkState.postValue(NetworkState.ERROR)
                }
            }

            override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                Log.d(TAG, "Failed to fetch messages", t)
                networkState.postValue(NetworkState.ERROR)
            }

        })



    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Message>) {
        val chatRequest = chatAPI.getMessages(KEY, CLIENT, params.startPosition, params.loadSize)

        networkState.postValue(NetworkState.LOADING)
        Log.d(TAG, "loadRange startPosition: ${params.startPosition} \n" +
                   "                           loadSize: ${params.loadSize}")

        chatRequest.enqueue(object : Callback<MessageResponse> {

            override fun onResponse(
                call: Call<MessageResponse>,
                response: Response<MessageResponse>
            ) {

                try {
                    callback.onResult(response.body()?.messages!!)
                    networkState.postValue(NetworkState.DONE)
                    Log.d(TAG, "loadRange Response body: ${response.body()}")
                } catch (e: Exception) {
                    Log.d(TAG, "Messages not found!", e)
                    networkState.postValue(NetworkState.ERROR)
                }
            }

            override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                Log.d(TAG, "Failed to fetch messages", t)
                networkState.postValue(NetworkState.ERROR)
            }

        })

    }

}