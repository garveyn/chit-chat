package com.ngarvey.chitchat.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import com.ngarvey.chitchat.api.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MessageFetcher(
    val key: String = "d51bad9f-24ec-4000-aaf2-e897bb829690",
    val client: String = "nicholas.garvey@mymail.champlain.edu"
) {

    private val TAG = "MessageFetcher"
    private val chatAPI: ChitChatAPI

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.stepoutnyc.com/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()

        chatAPI = retrofit.create(ChitChatAPI::class.java)
    }

    fun getMessages(skip: Int? = null, limit: Int? = null) : LiveData<MessageResponse> {
        val responseLiveData: MutableLiveData<MessageResponse> = MutableLiveData()
        val chatRequest: Call<MessageResponse> = chatAPI.getMessages(key, client, skip, limit)

        chatRequest.enqueue(object : Callback<MessageResponse> {

            override fun onResponse(call: Call<MessageResponse>, response: Response<MessageResponse>) {
                Log.d(TAG, "getMessage Response received: $response \n" +
                        "body: ${response.body()}")
                responseLiveData.value = response.body()
            }

            override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                Log.d(TAG, "Failed to fetch messages", t)
            }

        })

        return responseLiveData
    }

    fun sendMessage(
        message: String,
        latitude: Double?,
        longitude: Double?
    ): LiveData<NewMessageResponse> {

        val responseLiveData: MutableLiveData<NewMessageResponse> = MutableLiveData()
        val chatRequest: Call<NewMessageResponse>
                = chatAPI.sendMessage(key, client, message, latitude, longitude)

        chatRequest.enqueue(object : Callback<NewMessageResponse> {

            override fun onResponse(
                call: Call<NewMessageResponse>,
                response: Response<NewMessageResponse>
            ) {
                Log.d(TAG, "sendMessage Response received: $response \n" +
                        "body: ${response.body()}")
                responseLiveData.value = response.body()
            }

            override fun onFailure(call: Call<NewMessageResponse>, t: Throwable) {
                Log.d(TAG, "Failed to fetch messages", t)
            }

        })

        return responseLiveData
    }

    fun likeMessage(
        messageID: String
    ): LiveData<RatingResonse> {

        val responseLiveData: MutableLiveData<RatingResonse> = MutableLiveData()
        val chatRequest: Call<RatingResonse> = chatAPI.likeMessage(messageID, key, client)

        chatRequest.enqueue(object : Callback<RatingResonse> {

            override fun onResponse(call: Call<RatingResonse>, response: Response<RatingResonse>) {
                Log.d(TAG, "likeMessage Response received: $response \n" +
                        "body: ${response.body()}")
                responseLiveData.value = response.body()
            }

            override fun onFailure(call: Call<RatingResonse>, t: Throwable) {
                Log.d(TAG, "Failed to fetch messages", t)
            }

        })

        return responseLiveData
    }

    fun dislikeMessage(
        messageID: String
    ): LiveData<RatingResonse> {

        val responseLiveData: MutableLiveData<RatingResonse> = MutableLiveData()
        val chatRequest: Call<RatingResonse> = chatAPI.dislikeMessage(messageID, key, client)

        chatRequest.enqueue(object : Callback<RatingResonse> {

            override fun onResponse(call: Call<RatingResonse>, response: Response<RatingResonse>) {
                Log.d(TAG, "dislikeMessage Response received: $response \n" +
                        "body: ${response.body()}")
                responseLiveData.value = response.body()
            }

            override fun onFailure(call: Call<RatingResonse>, t: Throwable) {
                Log.d(TAG, "Failed to fetch messages", t)
            }

        })

        return responseLiveData
    }

}