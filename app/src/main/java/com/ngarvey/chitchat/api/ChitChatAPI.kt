package com.ngarvey.chitchat.api

import retrofit2.Retrofit
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Representation of the Chit Chat API used for [Retrofit]
 *
 * Much of this code was inspired by the BNR 4th Edition, Chapter 24. They implement the flickr
 * gallery using retrofit to create the API calls.
 *
 * Figuring out how to use [GET] sourced from:
 *   https://stackoverflow.com/a/24100442/12369045
 *
 * Figuring out how to use [Path] sourced from:
 *   https://stackoverflow.com/a/39102544/12369045
 *
 */
interface ChitChatAPI  {

    @GET("/chitchat")
    fun getMessages(
        @Query("key") key: String,
        @Query("client") client: String,
        @Query("skip") skip: Int? = null,
        @Query("limit") limit: Int? = null
    ): Call<MessageResponse>

    @POST("/chitchat")
    fun sendMessage(
        @Query("key") key: String,
        @Query("client") client: String,
        @Query("message") message: String,
        @Query("lat") latitude: Double?,
        @Query("lon") longitude: Double?
    ): Call<NewMessageResponse>

    @GET("/chitchat/like/{messageID}")
    fun likeMessage(
        @Path("messageID") messageID: String,
        @Query("key") key: String,
        @Query("client") client: String
    ): Call<RatingResonse>

    @GET("/chitchat/dislike/{messageID}")
    fun dislikeMessage(
        @Path("messageID") messageID: String,
        @Query("key") key: String,
        @Query("client") client: String
    ): Call<RatingResonse>

}